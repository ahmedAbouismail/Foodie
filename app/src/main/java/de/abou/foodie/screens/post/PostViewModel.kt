package de.abou.foodie.screens.post

import android.app.Application
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.util.Log
import android.widget.ImageView
import androidx.lifecycle.*
import com.google.firebase.Timestamp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthException
import de.abou.foodie.FirebaseUserLiveData
import de.abou.foodie.database.Constants
import de.abou.foodie.database.MyFirestore
import de.abou.foodie.database.Post
import de.abou.foodie.storage.MyFirebaseStorage
import kotlinx.coroutines.*
import java.io.ByteArrayOutputStream


class PostViewModel(val database:MyFirestore,  application: Application) :AndroidViewModel(application){

    private companion object{
        const val TAG = "PostViewModel"
        const val maxLength : Int = 200

    }




    val checkPermissionLiveData = MutableLiveData<Boolean>()

    val titleLiveData = MutableLiveData<String>()
    val descriptionLiveData = MutableLiveData<String>()


    private var aspectRatio : Double = 0.0
    private var targetWidth: Int = 0
    private var targetHeight : Int = 0

    private lateinit var bitmap : Bitmap
    private lateinit var resizedImage : Bitmap
    private var baos = ByteArrayOutputStream()


    private  var _db = MyFirestore()

    private var _postIdLiveData = MutableLiveData<String>()
    get() = _postIdLiveData

    private var _updatePostLivedata = MutableLiveData<Boolean>()
    val updatePostLivedata : LiveData<Boolean>
    get() = _updatePostLivedata
    private var currentUser = FirebaseAuth.getInstance().currentUser!!.uid




    init {
        checkPermissionLiveData.value = false
    }


    private fun modifyImage(imageView : ImageView){

        bitmap = (imageView.drawable as BitmapDrawable).bitmap
        resizedImage = resizeBitmap(bitmap, maxLength)

        Log.e(TAG, imageView.id.toString())

    }

    private fun resizeBitmap(source: Bitmap, maxLength: Int): Bitmap {
        try {
            if (source.height >= source.width) {
                if (source.height <= maxLength) { // if image height already smaller than the required height
                    return source
                }

                aspectRatio = source.width.toDouble() / source.height.toDouble()
                targetWidth = (maxLength * aspectRatio).toInt()


                return Bitmap.createScaledBitmap(source, targetWidth, maxLength, false)
            } else {
                if (source.width <= maxLength) { // if image width already smaller than the required width
                    return source
                }

                aspectRatio = source.height.toDouble() / source.width.toDouble()
                targetHeight = (maxLength * aspectRatio).toInt()

                return Bitmap.createScaledBitmap(source, maxLength, targetHeight, false)
            }
        } catch (e: Exception) {
            return source
        }
    }



    fun onAddPost(imageView: ImageView){

        val ref = Constants.POSTS + "/"+ FirebaseAuth.getInstance().currentUser!!.uid + "/"+ titleLiveData.value!! + "/"+ Timestamp.now()
        val myFirebaseStorage = MyFirebaseStorage(ref)

        modifyImage(imageView)

        val data = convertToBytes(resizedImage)

        viewModelScope.launch {

            myFirebaseStorage.uploadImageOnStorage(data)

            var uri = myFirebaseStorage.getImageUri()
            var post = Post(owner = currentUser,
                    title = titleLiveData.value.toString()
                    ,description = descriptionLiveData.value.toString()
                    ,imageRef = ref
                    ,photo = uri)
            insertPostToDb(post)
        }
    }
    private suspend fun insertPostToDb(post:Post){
        try {
            viewModelScope.launch{
                _updatePostLivedata.value = _db.insertPost(post)
            }
        }catch (e: FirebaseAuthException){
            Log.w(TAG, e)
        }
    }

    private fun convertToBytes(bitmap:Bitmap): ByteArray{
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos)
        return baos.toByteArray()
    }


    enum class AuthenticationState {
        AUTHENTICATED, UNAUTHENTICATED, INVALID_AUTHENTICATION
    }

    val authenticationState = FirebaseUserLiveData().map { user ->

        if (user != null){
            AuthenticationState.AUTHENTICATED

        }else{
            AuthenticationState.UNAUTHENTICATED

        }
    }

}