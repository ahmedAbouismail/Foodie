package de.abou.foodie.database

import android.util.Log
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.navigation.fragment.NavHostFragment
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.FirebaseFirestoreException
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.firestore.ktx.toObjects
import com.google.protobuf.Empty
import com.squareup.okhttp.Dispatcher
import de.abou.foodie.screens.regestration.signIn.SignInFragmentDirections
import kotlinx.coroutines.*
import kotlinx.coroutines.tasks.await
import java.lang.NullPointerException

class MyFirestore {

    companion object {
        const val TAG: String = "MyFirestore"
    }

    private val _Firestore = FirebaseFirestore.getInstance()

    private var _postIdLiveData = MutableLiveData<String>()
    val postIdsLiveData: LiveData<String>
        get() = _postIdLiveData


    suspend fun insertUser(user: User) {
        try {
            withContext(Dispatchers.IO) {
                _Firestore.collection(Constants.USERS)
                        .document(getCurrentUserId())
                        .set(user).await()
            }
        } catch (e: FirebaseFirestoreException) {
            Log.w(TAG, e)
        }
    }

    suspend fun insertPost(post: Post):Boolean {
        return try {
            withContext(Dispatchers.IO) {
                var postRef = _Firestore.collection(Constants.POSTS).add(post).await()
                var postId = postRef.id
                postRef.update("postId", postId).await()

                insertPostIdToUser(postId)
                withContext(Dispatchers.Main) {
                    _postIdLiveData.value = postId
                    Log.i(TAG, _postIdLiveData.value.toString())
                }
                postRef != null
            }

        } catch (e: FirebaseFirestoreException) {
            Log.w(TAG, e)
            false
        }

    }

    suspend fun insertPostIdToUser(postId: String) {
        try {
            withContext(Dispatchers.IO) {
                var UserRef = _Firestore.collection(Constants.USERS)
                        .document(getCurrentUserId())
                UserRef.update("idsOfPosts", FieldValue.arrayUnion(postId)).await()
            }

        } catch (e: FirebaseFirestoreException) {
            Log.w(TAG, e)
        }


    }

    fun getAllPosts(): LiveData<List<Post>> {
        var liveData = MutableLiveData<List<Post>>()

        _Firestore.collection(Constants.POSTS).get().addOnSuccessListener { result ->
            liveData.value = result.toObjects<Post>()


        }.addOnFailureListener { exception ->
            Log.d(TAG, "Error getting documents: ", exception)
        }

        return liveData
    }

    fun getPostsOfOthers(): LiveData<List<Post>> {
        var liveData = MutableLiveData<List<Post>>()

        _Firestore.collection(Constants.POSTS)
                .whereNotEqualTo("owner", getCurrentUserId())
                .get().addOnSuccessListener { result ->
                    liveData.value = result.toObjects<Post>()


                }.addOnFailureListener { exception ->
                    Log.d(TAG, "Error getting documents: ", exception)
                }

        return liveData
    }

    suspend fun getUserByUserId(userId: String): User? {
        return try {
            withContext(Dispatchers.IO) {
                _Firestore.collection(Constants.USERS)
                        .document(getCurrentUserId())
                        .get().await().toObject<User>()

            }
        } catch (e: FirebaseFirestoreException) {
            Log.w(TAG, e)

            return User("", "", "", "", )
        }
    }

    suspend fun getPostsByUserId(userId: String): List<Post> {
        return try {
            withContext(Dispatchers.IO) {
                _Firestore.collection(Constants.POSTS)
                        .whereEqualTo("owner", userId)
                        .get().await().toObjects<Post>()
            }
        } catch (e: FirebaseFirestoreException) {
            Log.w(TAG, e)

            return emptyList()
        }
    }

    suspend fun updatePost(postId: String, title: String, description: String, photo: String):Boolean {
        return try {
            withContext(Dispatchers.IO) {
                var task = _Firestore.collection(Constants.POSTS)
                        .document(postId)
                        .update(mapOf("title" to title, "description" to description, "photo" to photo)).await()
                true
            }
        } catch (e: FirebaseFirestoreException) {
            Log.w(TAG, e)
            false
        }
    }

    suspend fun addOrDeleteSubscriberToPost(postOwnerId: String, postId: String, subscriberId: String, checked: Boolean) {
        withContext(Dispatchers.IO) {
            try {
                if (checked) {
                    _Firestore.collection(Constants.POSTS)
                        .document(postId)
                        .update("idsOfSubscribers", FieldValue.arrayUnion(subscriberId)).await()
                } else {
                    _Firestore.collection(Constants.POSTS)
                        .document(postId)
                            .update("idsOfSubscribers", FieldValue.arrayRemove(subscriberId)).await()
                }

            } catch (e: FirebaseFirestoreException) {
                Log.w(TAG, e)
            }
        }
    }

    suspend fun updateSubscribedPostsInUser(postId: String, checked: Boolean) {
        withContext(Dispatchers.IO) {
            if (checked) {
                _Firestore.collection(Constants.USERS)
                        .document(getCurrentUserId())
                        .update("idsOfSubscribedPosts", FieldValue.arrayUnion(postId)).await()
            } else {
                _Firestore.collection(Constants.USERS)
                        .document(getCurrentUserId())
                        .update("idsOfSubscribers", FieldValue.arrayRemove(postId)).await()
            }

        }
    }

    suspend fun deletePost(postId: String):Boolean{
        return try {
            withContext(Dispatchers.IO){
                _Firestore.collection(Constants.POSTS)
                        .document(postId).delete().await()
                _Firestore.collection(Constants.USERS)
                        .document(getCurrentUserId())
                        .update("idsOfPosts", FieldValue.arrayRemove(postId)).await()
            }
            true
        }catch (e: FirebaseFirestoreException){
            Log.w(TAG, e)
            false
        }

    }

    suspend fun updateUser (firstName : String, lastName:String):Boolean{
        return try {
            withContext(Dispatchers.IO){
                _Firestore.collection(Constants.USERS)
                    .document(getCurrentUserId())
                    .update(mapOf("firstName" to firstName,
                    "lastName" to lastName)).await()
                true
            }
        }catch (e: FirebaseFirestoreException){
            Log.w(TAG, e)
            false
        }
    }

    fun getCurrentUserId(): String {
        Log.i(TAG, FirebaseAuth.getInstance().currentUser!!.uid)
        return FirebaseAuth.getInstance().currentUser!!.uid
    }
}
