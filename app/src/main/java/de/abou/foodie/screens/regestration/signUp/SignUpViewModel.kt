package de.abou.foodie.screens.regestration.signUp

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class SignUpViewModel:ViewModel() {
    companion object {
        const val TAG = "Sign Up"
        const val SIGN_IN_RESULT_CODE = 1001
    }

    private var auth: FirebaseAuth = Firebase.auth

    //Live Data
    private val _eventSignUp = MutableLiveData<Boolean>()
    val eventSignIn : LiveData<Boolean>
        get() = _eventSignUp

    fun onSignUp(email : String, password: String){
        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful){
                    Log.d(TAG, "createUserWithEmail:success")
                    _eventSignUp.value = true
                }else{
                    Log.w(TAG, "createUserWithEmail:failure", task.exception)
                    _eventSignUp.value = false
                }
            }

    }
}