package de.abou.foodie.screens.regestration.signIn


import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.map
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import de.abou.foodie.FirebaseUserLiveData
import de.abou.foodie.database.MyFirestore
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.supervisorScope
import java.lang.Exception
import java.lang.IllegalArgumentException
import java.lang.NullPointerException


class SignInViewModel():ViewModel() {

    companion object {
        const val TAG = "LogInFragment"
        const val SIGN_IN_RESULT_CODE = 1001
    }

    var email = MutableLiveData<String>()
    var password = MutableLiveData<String>()

    private var auth: FirebaseAuth = Firebase.auth

    //Live Data
    private val _eventSignIn = MutableLiveData<Boolean>()
    val eventSignIn : LiveData<Boolean>
    get() = _eventSignIn

     fun onSignIn(){
         try {
             auth.signInWithEmailAndPassword(email.value!!, password.value!!)
                     .addOnCompleteListener { task ->
                         if (task.isSuccessful){
                             Log.d(TAG, "signInWithEmail:success")
                             _eventSignIn.value = true
                         }else{
                             Log.w(TAG, "signInWithEmail:failure", task.exception)
                             _eventSignIn.value = false
                         }
                     }
         }catch (e: IllegalArgumentException){
             _eventSignIn.value = false
         }catch (e: NullPointerException){
             _eventSignIn.value = false
         }

    }

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