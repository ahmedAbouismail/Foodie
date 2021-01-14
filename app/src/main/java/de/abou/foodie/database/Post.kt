package de.abou.foodie.database

import android.net.Uri
import com.google.firebase.ktx.Firebase
import java.net.URL

data class Post(
        var postId : String = "",
        var owner:String = "",
        var title:String = "",
        var photo:String = "",
        var imageRef:String = "",
        var description: String = "",
        var subscribe:Boolean = false,
        var idsOfSubscribers: MutableList<String> = mutableListOf())
