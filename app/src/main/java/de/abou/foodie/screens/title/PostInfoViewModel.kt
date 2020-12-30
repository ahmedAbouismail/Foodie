package de.abou.foodie.screens.title

import android.net.Uri
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import de.abou.foodie.database.MyFirestore
import de.abou.foodie.database.Post
import de.abou.foodie.database.User
import kotlinx.coroutines.launch

class PostInfoViewModel:ViewModel() {
    val postIdLiveData = MutableLiveData<String>()
    val titleLiveData = MutableLiveData<String>()
    val descriptionLiveData = MutableLiveData<String>()
    val imageLiveData = MutableLiveData<Uri>()
    private val _userLiveData = MutableLiveData<User>()
    val userLiveData : LiveData<User>
    get() = _userLiveData


    private var currentUser = FirebaseAuth.getInstance().currentUser!!.uid
    private val _db = MyFirestore()

    init {
        getPosts()
    }

    private fun getPosts(){
        viewModelScope.launch {
            _userLiveData.value = _db.getUserByUserId(currentUser)
        }
    }


}