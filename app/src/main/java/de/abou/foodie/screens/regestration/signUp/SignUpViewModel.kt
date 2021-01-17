package de.abou.foodie.screens.regestration.signUp

import android.util.Log
import androidx.lifecycle.*
import com.google.firebase.auth.*
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

    var vaildPassowrdLiveData = MutableLiveData<Boolean>()
    var vaildEmailLiveData = MutableLiveData<Boolean>()
    var vaildInputLiveData = MutableLiveData<Boolean>()

    //Live Data
    private val _eventSignUp = MutableLiveData<Boolean>()
    val eventSignUp : LiveData<Boolean>
        get() = _eventSignUp


    fun createUser() {
        viewModelScope.launch {
            try {
                auth.createUserWithEmailAndPassword(email.value!!, password.value!!).await()
                user = User(firstName.value!!, lastName.value!!, email.value!!)
                db.insertUser(user)
                _eventSignUp.value = true
            } catch (e: FirebaseAuthUserCollisionException) {
                _eventSignUp.value = false
            }catch (e: FirebaseAuthWeakPasswordException){
                vaildPassowrdLiveData.value = false
            }catch (e: FirebaseAuthInvalidCredentialsException){
                vaildEmailLiveData.value = false
            }catch (e: NullPointerException){
                vaildInputLiveData.value = false
            }catch (e: IllegalArgumentException){
                vaildInputLiveData.value = false
            }
        }
    }

    fun resetVaildPassowrdLiveData() {
        vaildPassowrdLiveData.value = true
    }
    fun resetVaildEmailLiveData() {
        vaildPassowrdLiveData.value = true
    }

    fun resetVaildInputLiveData() {
        vaildInputLiveData.value = true
    }
}