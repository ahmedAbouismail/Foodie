package de.abou.foodie.database

import android.net.Uri
import java.net.URL

data class Post(
        var owner:String = "",
        var title:String = "",
        var photo:String = "",
        var description: String = "",
        var subscribe:Boolean = false,
        var idsOfSubscribers: List<Int> = emptyList())
