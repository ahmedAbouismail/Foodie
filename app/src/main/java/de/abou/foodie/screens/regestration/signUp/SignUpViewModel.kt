package de.abou.foodie.screens.regestration.signUp

import android.util.Log
import androidx.lifecycle.*
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthException
import com.google.firebase.auth.FirebaseAuthUserCollisionException
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import de.abou.foodie.database.MyFirestore
import de.abou.foodie.database.User
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import kotlin.math.log

class SignUpViewModel:ViewModel() {
    companion object {
        const val TAG = "Sign Up"
        const val SIGN_IN_RESULT_CODE = 1001
    }

    private var auth: FirebaseAuth = Firebase.auth
    private val db = MyFirestore()
    private lateinit var user : User

    //Var to hold the data from the layout
    var email = MutableLiveData<String>()
    var password = MutableLiveData<String>()
    var firstName = MutableLiveData<String>()
    var lastName = MutableLiveData<String>()

    private var exist : Boolean = false


    //Live Data
    private val _eventSignUp = MutableLiveData<Boolean>()
    val eventSignUp : LiveData<Boolean>
        get() = _eventSignUp

    private var _isUserExistsLiveData = MutableLiveData<Boolean>()
    val isUserExistsLiveData : LiveData<Boolean>
    get() = _isUserExistsLiveData

    fun createUser() {
        viewModelScope.launch {
            try {
                auth.createUserWithEmailAndPassword(email.value!!, password.value!!).await()

                user = User(firstName.value!!, lastName.value!!, email.value!!)

                db.insertUser(user)

                Log.d(TAG, "createUserWithEmail:success")
                _eventSignUp.value = true
            } catch (e: FirebaseAuthUserCollisionException) {
                _eventSignUp.value = false
            }
        }
    }
}