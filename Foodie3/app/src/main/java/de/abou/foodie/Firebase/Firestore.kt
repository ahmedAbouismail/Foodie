package de.abou.foodie.Firebase

import com.google.firebase.auth.FirebaseAuth

class Firestore{

    fun getCurrentUserId():String{

        var currentUser = FirebaseAuth.getInstance().currentUser
        var currentUserID =""
        if (currentUser != null){
            currentUserID = currentUser.uid
        }
        return currentUserID
    }
}