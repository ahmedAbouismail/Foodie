package de.abou.foodie.screens.regestration.signUp

import android.util.Log
import androidx.lifecycle.*
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthException
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import de.abou.foodie.database.MyFirestore
import de.abou.foodie.database.User
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

class SignUpViewModel:ViewModel() {
    companion object {
        const val TAG = "Sign Up"
        const val SIGN_IN_RESULT_CODE = 1001
    }

    private var auth: FirebaseAuth = Firebase.auth
    private val db = MyFirestore()
    private lateinit var user : User

    //Live Data
    private val _eventSignUp = MutableLiveData<Boolean>()
    val eventSignIn : LiveData<Boolean>
        get() = _eventSignUp

    fun onSignUp(email : String, password: String) {
        try {
            viewModelScope.launch{
                auth.createUserWithEmailAndPassword(email, password).await()
                db.insertUser(createUser(email))
                Log.d(TAG, "createUserWithEmail:success")
                _eventSignUp.value = true
            }
        } catch (e: FirebaseAuthException) {
            Log.w(TAG, "createUserWithEmail:failure", e)
            _eventSignUp.value = false
        }
    }
    private fun createUser(email:String):User{
        user = User(null,null,email,null)
        return user
    }


}