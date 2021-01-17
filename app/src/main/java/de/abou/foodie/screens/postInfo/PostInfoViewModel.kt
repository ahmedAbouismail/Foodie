package de.abou.foodie.screens.postInfo

import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.net.Uri
import android.util.Log
import android.widget.ImageView
import android.widget.Switch
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
import java.lang.IllegalArgumentException

class PostInfoViewModel:ViewModel() {

    private companion object{
        const val TAG = "PostInfoViewModel"
        const val maxLength : Int = 200
    }
    private val _db = MyFirestore()
    private lateinit var myFirebaseStorage : MyFirebaseStorage

    //Live Data to get save the information from clicked post in the Recyclerview
    var postIdLiveData = MutableLiveData<String>()
    var titleLiveData = MutableLiveData<String>()
    var descriptionLiveData = MutableLiveData<String>()
    var imageRefLiveData= MutableLiveData<String>()
    var imageLiveData = MutableLiveData<Uri>()
    var postOwnerIdLiveData = MutableLiveData<String>()
    var priceLiveData = MutableLiveData<String>()

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


    var ownerNameLiveData = MutableLiveData<String>()
    var ownerEmailLiveData = MutableLiveData<String>()




    //Variables to change the dimensions of the piked image and convert it to bitmap
    private var aspectRatio : Double = 0.0
    private var targetWidth: Int = 0
    private var targetHeight : Int = 0

    private lateinit var bitmap : Bitmap
    private lateinit var resizedImage : Bitmap
    private var baos = ByteArrayOutputStream()

    init {
        getPosts()
        //for the first use must be false to ask for the permission and then will be always true and the user will not be asked any more for the perm.
        checkPermissionLiveData.value = false
    }

    //get the post-owner data to show it in as info
    fun getOwnerData() {
        viewModelScope.launch {
            val user = _db.getUserByUserId(postOwnerIdLiveData.value.toString())
            ownerEmailLiveData.value = user?.email.toString()
            ownerNameLiveData.value = user?.firstName + " " + user?.lastName
        }
    }

    /**
     * @param imageView get the image view from the xml to convert the image to bitmap
     */
    private fun modifyImage(imageView : ImageView){

        bitmap = (imageView.drawable as BitmapDrawable).bitmap
        resizedImage = resizeBitmap(bitmap, maxLength)

        Log.e(TAG, imageView.id.toString())
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

    private fun convertToBytes(bitmap:Bitmap): ByteArray{
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos)
        return baos.toByteArray()
    }


    /**
     * to edit the post with the new entries
     */
    fun onEditClick(imageView: ImageView){
        try {
            //modify and then convert to Byt to upload it in the Storage
            modifyImage(imageView = imageView)
            var ref = imageRefLiveData.value
            val data = convertToBytes(resizedImage)

            //create obj of the storage with defined ref
            myFirebaseStorage = MyFirebaseStorage(ref!!)

            viewModelScope.launch {
                //upload the image in the storage
                myFirebaseStorage.uploadImageOnStorage(data)
                //get the uri-download of the image
                var uri = myFirebaseStorage.getImageUri()
                //check if any field is empty if empty then show toast message
                if (titleLiveData.value.toString().isNullOrEmpty()){
                    _updatePostLivedata.value = false
                }else{
                    updatePost(titleLiveData.value.toString(), descriptionLiveData.value.toString(), uri)
                }
            }
        }catch (e: NullPointerException){
            _updatePostLivedata.value = false
        }catch (e: IllegalArgumentException){
            _updatePostLivedata.value = false
        }

    }

    /**
     * send the status of the switch to the db to delete or add the subscribe
     */
    fun onSwitchClick(view: Switch){
        viewModelScope.launch {
            addSubscriber(view.isChecked)
            addSubscribedPostToUser(view.isChecked)
        }
    }

    fun onDeleteClick(){
        viewModelScope.launch {
            deletePost()
        }
    }

    private fun getPosts(){
        viewModelScope.launch {
            _userLiveData.value = _db.getUserByUserId(getCurrentUser())
        }
    }

    private suspend fun updatePost(title:String, description:String, photo:String){
        viewModelScope.launch {
            _updatePostLivedata.value = _db.updatePost(
                    postIdLiveData.value.toString()
                    ,title
                    , description, photo
                    , priceLiveData.value.toString()) }
    }


    private suspend fun addSubscriber(checked:Boolean){
        viewModelScope.launch {
            _db.addOrDeleteSubscriberToPost(postIdLiveData.value!!
                    , getCurrentUser(), checked)
        }
    }

    private suspend fun addSubscribedPostToUser(checked:Boolean){
        viewModelScope.launch {
            _db.updateSubscribedPostsInUser(postIdLiveData.value!!
                    ,checked)
        }
    }

    private fun getCurrentUser():String{
        return FirebaseAuth.getInstance().currentUser!!.uid
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