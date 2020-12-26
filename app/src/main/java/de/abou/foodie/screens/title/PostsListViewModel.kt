package de.abou.foodie.screens.title

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.map
import androidx.lifecycle.viewModelScope
import de.abou.foodie.FirebaseUserLiveData
import de.abou.foodie.database.MyFirestore
import kotlinx.coroutines.launch

class PostsListViewModel:ViewModel() {


    private val _db = MyFirestore()



    val posts = _db.getAllPosts()

    enum class AuthenticationState {
        AUTHENTICATED, UNAUTHENTICATED, INVALID_AUTHENTICATION
    }

    init {
        Log.i("PostViewModel","PostViewModel Created")
    }
    val authenticationState = FirebaseUserLiveData().map { user ->

        if (user != null){
            AuthenticationState.AUTHENTICATED

        }else{
            AuthenticationState.UNAUTHENTICATED

        }
    }


}