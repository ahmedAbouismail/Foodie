package de.abou.foodie.database

import android.database.CursorJoiner
import android.media.Session2Command
import android.net.Uri
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.FirebaseFirestoreException
import kotlinx.coroutines.*
import kotlinx.coroutines.tasks.await
import java.lang.Exception

class MyFirestore {

    companion object{
        const val TAG : String = "MyFirestore"
    }
    private val _Firetore = FirebaseFirestore.getInstance()

    private var _postIdsLiveData = MutableLiveData<String>()
    val postIdsLiveData : LiveData<String>
    get() = _postIdsLiveData

    suspend fun insertUser(user:User){
        try {
            withContext(Dispatchers.IO){
                _Firetore.collection(Constants.USERS)
                        .document(getCurrentUserId())
                        .set(user).await()
            }
        }catch (e: FirebaseFirestoreException){
            Log.w(TAG, e)
        }
    }

    suspend fun insertPost(post:Post){
        try {
            withContext(Dispatchers.IO){
                var id = _Firetore.collection(Constants.POSTS).add(post).await().id
                withContext(Dispatchers.Main){
                    _postIdsLiveData.value = id
                    Log.i(TAG, _postIdsLiveData.value.toString())
                }
            }

        }catch (e: FirebaseFirestoreException){
            Log.w(TAG, e)
        }

    }

    suspend fun insertPostIdToUser(postId:String){
        try {
            withContext(Dispatchers.IO){
                _Firetore.collection(Constants.USERS)
                        .document(getCurrentUserId())
                        .update("idsOfPosts", FieldValue.arrayUnion(postId))
            }

        }catch (e:FirebaseFirestoreException){
            Log.w(TAG, e)
        }


    }

    suspend fun updateImageUrlInPost(uri:Uri){
        Log.i(TAG, "HolAAAAAAAAAAAAAAAAAAAAAAAAAAAa")
        try {
            withContext(Dispatchers.IO){
                _Firetore.collection(Constants.POSTS)
                        .document(getCurrentUserId())
                        .update("photo", uri.toString()).await()
            }
        }catch (e: FirebaseFirestoreException){
            Log.w(TAG, e)
        }

    }

    private fun getCurrentUserId():String{
        return FirebaseAuth.getInstance().currentUser!!.uid
    }

}