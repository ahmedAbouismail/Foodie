package de.abou.foodie.User

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import de.abou.foodie.database.MyFirestore
import de.abou.foodie.database.User
import kotlinx.coroutines.launch

class SubscriberListViewModel:ViewModel() {

    private val _db = MyFirestore()
    var subscribersId = MutableLiveData<MutableList<String>>()
//    var user = MutableLiveData<User>()
    var user = mutableListOf<User>()
    var users = MutableLiveData<List<User>>()


    fun getSubscribersInfo(){
        viewModelScope.launch {
            for (subscriber in subscribersId.value!!){
                user.add(_db.getUserByUserId(subscriber)!!)
                Log.i("A7med", user[0]!!.firstName.toString())
                users.value = user
//                users.value?.add(user.value!!)
            }
        }
    }
}