package de.abou.foodie.screens.title

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.map
import de.abou.foodie.FirebaseUserLiveData

class PostsListViewModel:ViewModel() {

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