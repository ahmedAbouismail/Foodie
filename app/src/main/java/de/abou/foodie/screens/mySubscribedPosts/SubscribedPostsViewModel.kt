package de.abou.foodie.screens.mySubscribedPosts

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import de.abou.foodie.database.MyFirestore
import de.abou.foodie.database.Post
import kotlinx.coroutines.launch

class SubscribedPostsViewModel:ViewModel() {
    private val _db = MyFirestore()
    var myPostsLiveData = MutableLiveData<List<Post>>()

    init {
        getPosts()
    }

    private fun getCurrentUserId():String{
        return FirebaseAuth.getInstance().currentUser!!.uid
    }

    private fun getPosts(){
        viewModelScope.launch {
            getPostsForUser()
        }

    }
    private suspend fun getPostsForUser(){
        viewModelScope.launch {
            myPostsLiveData.value = _db.getOnlyPostsContainIdOfSubscribers()
        }
    }


}