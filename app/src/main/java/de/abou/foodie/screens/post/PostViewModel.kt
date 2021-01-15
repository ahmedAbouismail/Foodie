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
import java.lang.IllegalArgumentException


class PostViewModel(val database:MyFirestore,  application: Application) :AndroidViewModel(application){

    private companion object{
        const val TAG = "PostViewModel"
        const val maxLength : Int = 200

    }


    private  var _db = MyFirestore()
    private var currentUser = FirebaseAuth.getInstance().currentUser!!.uid

    val checkPermissionLiveData = MutableLiveData<Boolean>()

    //elements of the post
    val titleLiveData = MutableLiveData<String>()
    val descriptionLiveData = MutableLiveData<String>()


    //Variables to change the dimensions of the piked image and convert it to bitmap
    private var aspectRatio : Double = 0.0
    private var targetWidth: Int = 0
    private var targetHeight : Int = 0

    private lateinit var bitmap : Bitmap
    private lateinit var resizedImage : Bitmap
    private var baos = ByteArrayOutputStream()





    //live data to check if the update process complete
    private var _updatePostLivedata = MutableLiveData<Boolean>()
    val updatePostLivedata : LiveData<Boolean>
    get() = _updatePostLivedata





    init {
        //for the first use must be false to ask for the permission and then will be always true and the user will not be asked any more for the perm.
        checkPermissionLiveData.value = false
    }

    /**
     * @param imageView get the image view from the xml to convert the image to bitmap
     */
    private fun modifyImage(imageView : ImageView){

        bitmap = (imageView.drawable as BitmapDrawable).bitmap
        resizedImage = resizeBitmap(bitmap, maxLength)

    }

    /**
     * To Change the dimension of the image
     * @param source the bitmap the we got from modifyImage()
     * @param maxLength the wanted length
     */
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

    /**
     * upload the Post in the Db when the user click add
     * @param imageView that we will send to  modifyImage()
     */
    fun onAddPost(imageView: ImageView){
        try {
            //the place where we want save the image in the storage
            val ref = Constants.POSTS + "/"+ FirebaseAuth.getInstance().currentUser!!.uid + "/"+ titleLiveData.value!! + "/"+ Timestamp.now()
            //create obj of the storage with defined ref
            val myFirebaseStorage = MyFirebaseStorage(ref)

            //modify and then convert to Byt to upload it in the Storage
            modifyImage(imageView)
            val data = convertToBytes(resizedImage)


            viewModelScope.launch {
                //upload the image in the storage
                myFirebaseStorage.uploadImageOnStorage(data)

                //get the uri-download of the image
                var uri = myFirebaseStorage.getImageUri()
                //create new post object and put the new data in it
                var post = Post(owner = currentUser,
                        title = titleLiveData.value.toString()
                        ,description = descriptionLiveData.value.toString()
                        ,imageRef = ref
                        ,photo = uri)
                //finally insert the post obj in the Db
                insertPostToDb(post)
            }
        }catch (e: NullPointerException){
            _updatePostLivedata.value = false
        }catch (e: IllegalArgumentException){
            _updatePostLivedata.value = false
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