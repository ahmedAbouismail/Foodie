package de.abou.foodie.screens.userInfo

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.ktx.Firebase
import de.abou.foodie.database.MyFirestore
import kotlinx.coroutines.launch

class MyProfileViewModel:ViewModel() {

    private var _db = MyFirestore()
    private val currentUserId = FirebaseAuth.getInstance().currentUser?.uid

    var userFirstName = MutableLiveData<String>()
    var userLastName = MutableLiveData<String>()

    init {
        Log.i("MyProfileFragment",currentUserId.toString())
        getUserInfo()
    }

    private fun getUserInfo() {
        Log.i("MyProfileFragment",currentUserId.toString())
        viewModelScope.launch {
            val user = _db.getUserByUserId(currentUserId.toString())
            Log.i("MyProfileFragment",userFirstName.toString())
            Log.i("MyProfileFragment",currentUserId.toString())
            userFirstName.value = user?.firstName
            userLastName.value = user?.lastName
        }
    }
}