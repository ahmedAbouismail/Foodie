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
            users.value = _db.getUsersList(subscribersId.value!!)
        }
    }
}

