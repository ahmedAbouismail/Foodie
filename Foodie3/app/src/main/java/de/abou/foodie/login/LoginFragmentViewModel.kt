package de.abou.foodie.login


import androidx.lifecycle.ViewModel
import androidx.lifecycle.map
import de.abou.foodie.Firebase.FirebaseUserLiveData


class LoginFragmentViewModel:ViewModel() {

    private fun signInRegisteredUser(){

    }


    enum class AuthenticationState{
        AUTHENTICATED, UNAUTHENTICATED, INVALID_AUTHENTICATION
    }

    val authenticationState = FirebaseUserLiveData().map { user ->
        if (user != null) {
            AuthenticationState.AUTHENTICATED
        } else {
            AuthenticationState.UNAUTHENTICATED
        }
    }
}