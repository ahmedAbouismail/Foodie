package de.abou.foodie.screens.userInfo

import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.widget.ImageView
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import de.abou.foodie.database.MyFirestore
import kotlinx.coroutines.launch
import java.io.ByteArrayOutputStream

class UserInfoViewModel:ViewModel() {

    private companion object{
        const val maxLength : Int = 200

    }

    private var _db = MyFirestore()
    private val currentUserId = FirebaseAuth.getInstance().currentUser?.uid

    var userFirstName = MutableLiveData<String>()
    var userLastName = MutableLiveData<String>()

    val checkPermissionLiveData = MutableLiveData<Boolean>()
    private var _taskLiveData = MutableLiveData<Boolean>()
    val taskLiveData : LiveData<Boolean>
    get() = _taskLiveData

    private lateinit var bitmap : Bitmap
    private lateinit var resizedImage : Bitmap
    private var baos = ByteArrayOutputStream()
    private var aspectRatio : Double = 0.0
    private var targetWidth: Int = 0
    private var targetHeight : Int = 0

    init {
        getUserInfo()
        checkPermissionLiveData.value = false
    }

    private fun getUserInfo() {
        viewModelScope.launch {
            val user = _db.getUserByUserId(currentUserId.toString())
            userFirstName.value = user?.firstName
            userLastName.value = user?.lastName
        }
    }



    fun onEditClick(){
        viewModelScope.launch {
            editUser()
        }
    }

    private suspend fun editUser() {
        viewModelScope.launch {
            if (userFirstName.value.isNullOrEmpty() || userLastName.value.isNullOrEmpty()){
                _taskLiveData.value = false
            }else{
                _taskLiveData.value = _db.updateUser(userFirstName.value.toString(), userLastName.value.toString())
            }

        }
    }
}