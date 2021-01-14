package de.abou.foodie.screens.postInfo

import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.net.Uri
import android.util.Log
import android.widget.ImageView
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import de.abou.foodie.database.MyFirestore
import de.abou.foodie.database.User
import de.abou.foodie.storage.MyFirebaseStorage
import kotlinx.coroutines.launch
import java.io.ByteArrayOutputStream

class PostInfoViewModel:ViewModel() {

    private companion object{
        const val TAG = "PostInfoViewModel"
        const val maxLength : Int = 200
    }
    var postIdLiveData = MutableLiveData<String>()
    var titleLiveData = MutableLiveData<String>()
    var descriptionLiveData = MutableLiveData<String>()
    var imageRefLiveData= MutableLiveData<String>()
    var imageLiveData = MutableLiveData<Uri>()
    var postOwnerLiveData = MutableLiveData<String>()

    var switchCheckedLiveData =  MutableLiveData<Boolean>()
    private val _userLiveData = MutableLiveData<User>()
    val userLiveData : LiveData<User>
    get() = _userLiveData

    private var _updatePostLivedata = MutableLiveData<Boolean>()
    val updatePostLivedata : LiveData<Boolean>
    get() = _updatePostLivedata
    var checkPermissionLiveData = MutableLiveData<Boolean>()

    private var _deleteLivedata = MutableLiveData<Boolean>()
    val deleteLivedata : LiveData<Boolean>
    get() = _deleteLivedata


    private val _db = MyFirestore()
    private lateinit var myFirebaseStorage : MyFirebaseStorage



    private var aspectRatio : Double = 0.0
    private var targetWidth: Int = 0
    private var targetHeight : Int = 0

    private lateinit var bitmap : Bitmap
    private lateinit var resizedImage : Bitmap
    private var baos = ByteArrayOutputStream()

    init {
        getPosts()

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

    private fun convertToBytes(bitmap:Bitmap): ByteArray{
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos)
        return baos.toByteArray()
    }

    private fun getPosts(){
        viewModelScope.launch {
            _userLiveData.value = _db.getUserByUserId(getCurrentUser())
        }
    }

    fun onEditClick(imageView: ImageView){
        modifyImage(imageView = imageView)

        var ref = imageRefLiveData.value
        val data = convertToBytes(resizedImage)
        Log.i(TAG, ref.toString())
        myFirebaseStorage = MyFirebaseStorage(ref!!)
        viewModelScope.launch {
            myFirebaseStorage.uploadImageOnStorage(data)
            var uri = myFirebaseStorage.getImageUri()

            updatePost(titleLiveData.value.toString(), descriptionLiveData.value.toString(), uri)
        }
    }

    private suspend fun updatePost(title:String, description:String, photo:String){
        viewModelScope.launch {
            Log.i("InfoPost", postIdLiveData.value.toString())
            _updatePostLivedata.value = _db.updatePost(postIdLiveData.value.toString(),title, description, photo) }
    }


    fun onSwitchClick(){
        viewModelScope.launch {
            addSubscriber()
            addSubscribedPostToUser()
        }
    }

    private suspend fun addSubscriber(){
        viewModelScope.launch {
            _db.addOrDeleteSubscriberToPost(postOwnerLiveData.value!!
                    , postIdLiveData.value!!
                    , getCurrentUser(), switchCheckedLiveData.value!!)
        }
    }

    private suspend fun addSubscribedPostToUser(){
        viewModelScope.launch {
            _db.updateSubscribedPostsInUser(postIdLiveData.value!!
                    , switchCheckedLiveData.value!!)
        }
    }

    private fun getCurrentUser():String{
        return FirebaseAuth.getInstance().currentUser!!.uid
    }

    fun onClickDelete(){
        viewModelScope.launch {
            deletePost()
        }
    }
    private suspend fun deletePost(){
        viewModelScope.launch {
            var ref = imageRefLiveData.value
            myFirebaseStorage = MyFirebaseStorage(ref!!)
            _deleteLivedata.value = _db.deletePost(postIdLiveData.value!!)
            myFirebaseStorage.deleteImageFromStorage(imageRefLiveData.value!!)
        }
    }

    fun resetUpdateLivedate(){
        _updatePostLivedata.value = false
    }
    fun resetDeleteLivedate(){
        _deleteLivedata.value = false
    }
}