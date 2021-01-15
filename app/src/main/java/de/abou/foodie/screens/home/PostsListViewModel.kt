package de.abou.foodie.screens.home

import android.util.Log
import androidx.lifecycle.*
import de.abou.foodie.FirebaseUserLiveData
import de.abou.foodie.database.MyFirestore
import de.abou.foodie.database.Post
import java.lang.NullPointerException

class PostsListViewModel:ViewModel() {


    private var _db : MyFirestore = MyFirestore()

    lateinit var posts : LiveData<List<Post>>



    init {
        try {
            posts = _db.getPostsOfOthers()
        }catch (e:NullPointerException){

        }
        Log.i("PostViewModel","PostViewModel Created")
    }

    enum class AuthenticationState {
        AUTHENTICATED, UNAUTHENTICATED, INVALID_AUTHENTICATION
    }
    val authenticationState = FirebaseUserLiveData().map { user ->

        if (user != null){
            AuthenticationState.AUTHENTICATED

        }else{
            AuthenticationState.UNAUTHENTICATED

        }
    }


}