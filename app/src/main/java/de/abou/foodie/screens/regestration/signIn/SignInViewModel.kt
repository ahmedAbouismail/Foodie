package de.abou.foodie.screens.regestration.signIn


import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import de.abou.foodie.database.MyFirestore
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.supervisorScope


class SignInViewModel():ViewModel() {

    companion object {
        const val TAG = "LogInFragment"
        const val SIGN_IN_RESULT_CODE = 1001
    }


    private var auth: FirebaseAuth = Firebase.auth

    //Live Data
    private val _eventSignIn = MutableLiveData<Boolean>()
    val eventSignIn : LiveData<Boolean>
    get() = _eventSignIn

     fun onSignIn(email : String, password: String){
        auth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful){
                        Log.d(TAG, "signInWithEmail:success")
                        _eventSignIn.value = true
                    }else{
                        Log.w(TAG, "signInWithEmail:failure", task.exception)
                        _eventSignIn.value = false
                    }
                }

    }


}