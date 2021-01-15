package de.abou.foodie.screens.title

import androidx.lifecycle.ViewModel
import androidx.lifecycle.map
import de.abou.foodie.FirebaseUserLiveData

class SplashViewModel:ViewModel() {

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