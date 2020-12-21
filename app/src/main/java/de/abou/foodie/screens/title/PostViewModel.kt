package de.abou.foodie.screens.title

import android.content.ActivityNotFoundException
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.drawable.BitmapDrawable
import android.net.Uri
import android.os.Build
import android.util.Log
import android.view.View
import android.widget.ImageView
import androidx.core.app.ActivityCompat.startActivityForResult
import androidx.core.content.ContextCompat
import androidx.lifecycle.*
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import de.abou.foodie.FirebaseUserLiveData
import de.abou.foodie.database.MyFirestore
import de.abou.foodie.storage.MyFirebaseStorage
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.io.ByteArrayOutputStream
import java.util.jar.Manifest
import kotlin.coroutines.coroutineContext


class PostViewModel :ViewModel(){

    private companion object{
        const val TAG = "PostViewModel"
        const val maxLength : Int = 200
    }

    val imageUrlLiveData = MutableLiveData<Uri>()



    val checkPermissionLiveData = MutableLiveData<Boolean>()


    private val _isImageUploaded = MutableLiveData<Boolean>()
    val isImageUploaded:LiveData<Boolean>
        get() = _isImageUploaded


    private lateinit var bitmap : Bitmap
    private var _resultLiveData = MutableLiveData<Bitmap>()
    val resultLiveData : LiveData<Bitmap>
    get() = _resultLiveData

    private var aspectRatio : Double = 0.0
    private var targetWidth: Int = 0
    private var targetHeight : Int = 0

    private lateinit var resizedImage : Bitmap

    init {
        checkPermissionLiveData.value = false
    }




    fun modifyImage(imageView : ImageView){

        bitmap = (imageView.drawable as BitmapDrawable).bitmap
        resizedImage = resizeBitmap(bitmap, maxLength)

        Log.e(TAG, imageView.id.toString())

    }

    fun resizeBitmap(source: Bitmap, maxLength: Int): Bitmap {
        try {
            if (source.height >= source.width) {
                if (source.height <= maxLength) { // if image height already smaller than the required height
                    return source
                }

                aspectRatio = source.width.toDouble() / source.height.toDouble()
                targetWidth = (maxLength * aspectRatio).toInt()
                _resultLiveData.value = Bitmap.createScaledBitmap(source, targetWidth, maxLength, false)


                return resultLiveData.value!!
            } else {
                if (source.width <= maxLength) { // if image width already smaller than the required width
                    return source
                }

                aspectRatio = source.height.toDouble() / source.width.toDouble()
                targetHeight = (maxLength * aspectRatio).toInt()

                _resultLiveData.value = Bitmap.createScaledBitmap(source, maxLength, targetHeight, false)
                return resultLiveData.value!!
            }
        } catch (e: Exception) {
            return source
        }
    }

    fun uploadImageToStorage(bitmap: Bitmap){
        val myFirebaseStorage = MyFirebaseStorage(
                "Post",
                FirebaseAuth.getInstance().currentUser!!.uid,
                "Title",
                bitmap)
        viewModelScope.launch (Dispatchers.Main) {
            myFirebaseStorage.uploadImageInStorage()
            _isImageUploaded.value = true
        }

        _isImageUploaded.value = false
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