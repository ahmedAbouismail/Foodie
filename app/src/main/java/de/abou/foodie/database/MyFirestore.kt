package de.abou.foodie.database

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.FirebaseFirestoreException
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.firestore.ktx.toObjects
import com.google.protobuf.Empty
import com.squareup.okhttp.Dispatcher
import kotlinx.coroutines.*
import kotlinx.coroutines.tasks.await

class MyFirestore {

    companion object{
        const val TAG : String = "MyFirestore"
    }
    private val _Firestore = FirebaseFirestore.getInstance()

    private var _postIdLiveData = MutableLiveData<String>()
    val postIdsLiveData : LiveData<String>
    get() = _postIdLiveData



    suspend fun insertUser(user:User){
        try {
            withContext(Dispatchers.IO){
                _Firestore.collection(Constants.USERS)
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
                var postRef = _Firestore.collection(Constants.POSTS).add(post).await()
                var postId = postRef.id
                postRef.update("postId", postId).await()

                    insertPostIdToUser(postId)
                withContext(Dispatchers.Main){
                    _postIdLiveData.value = postId
                    Log.i(TAG, _postIdLiveData.value.toString())
                }
            }

        }catch (e: FirebaseFirestoreException){
            Log.w(TAG, e)
        }

    }

    suspend fun insertPostIdToUser(postId:String){
        try {
            withContext(Dispatchers.IO){
                var UserRef = _Firestore.collection(Constants.USERS)
                        .document(getCurrentUserId())
                UserRef.update("idsOfPosts", FieldValue.arrayUnion(postId)).await()
            }

        }catch (e:FirebaseFirestoreException){
            Log.w(TAG, e)
        }


    }

     fun getAllPosts():LiveData<List<Post>>{
        var liveData = MutableLiveData<List<Post>>()

         _Firestore.collection(Constants.POSTS).get().addOnSuccessListener { result->
             liveData.value = result.toObjects<Post>()


         } .addOnFailureListener { exception ->
             Log.d(TAG, "Error getting documents: ", exception)
         }

        return liveData
    }
    fun getPostsOfOthers():LiveData<List<Post>>{
        var liveData = MutableLiveData<List<Post>>()

        _Firestore.collection(Constants.POSTS)
            .whereNotEqualTo("owner", getCurrentUserId())
            .get().addOnSuccessListener { result->
            liveData.value = result.toObjects<Post>()


        } .addOnFailureListener { exception ->
            Log.d(TAG, "Error getting documents: ", exception)
        }

        return liveData
    }
    suspend fun getUserByUserId(userId:String): User?{
        return try {
            withContext(Dispatchers.IO){
                _Firestore.collection(Constants.USERS)
                    .document(getCurrentUserId())
                    .get().await().toObject<User>()

            }
        }catch (e:FirebaseFirestoreException) {
            Log.w(TAG, e)

            return User("","","","",)
        }
    }
    suspend fun getPostsByUserId(userId:String):List<Post>{
        return try {
            withContext(Dispatchers.IO){
                _Firestore.collection(Constants.POSTS)
                        .whereEqualTo("owner", userId)
                        .get().await().toObjects<Post>()
            }
        }catch (e:FirebaseFirestoreException){
            Log.w(TAG, e)

            return emptyList()
        }


    }


    private fun getCurrentUserId():String{
        return FirebaseAuth.getInstance().currentUser!!.uid
    }

}