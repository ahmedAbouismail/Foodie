package de.abou.foodie.screens.title

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.map
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import de.abou.foodie.FirebaseUserLiveData
import de.abou.foodie.database.MyFirestore
import de.abou.foodie.database.Post
import kotlinx.coroutines.launch

class MyPostsViewModel:ViewModel(){

    private val _db = MyFirestore()
    var myPostsLiveData = MutableLiveData<List<Post>>()

    init {
        getPosts()
    }

    private fun getPosts(){
        viewModelScope.launch {
            getPostsForUser()
        }

    }
    private suspend fun getPostsForUser(){
        viewModelScope.launch {
            myPostsLiveData.value = _db.getPostsByUserId(getCurrentUserId())
        }
    }

    private fun getCurrentUserId():String{
        return FirebaseAuth.getInstance().currentUser!!.uid
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