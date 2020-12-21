package de.abou.foodie.database

import android.util.Log
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.FirebaseFirestoreException
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.tasks.await

class MyFirestore {

    companion object{
        const val TAG : String = "MyFirestore"
    }
    private val _Firetore = FirebaseFirestore.getInstance()

    suspend fun insertUser(user:User){
        try {
            _Firetore.collection(Constants.USERS)
                .document(getCurrentUserId())
                .set(user).await()
        }catch (e: FirebaseFirestoreException){
            Log.w(TAG, e)
        }
    }

    private fun getCurrentUserId():String{
        return FirebaseAuth.getInstance().currentUser!!.uid
    }

}