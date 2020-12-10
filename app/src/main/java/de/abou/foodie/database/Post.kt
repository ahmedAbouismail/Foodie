package de.abou.foodie.database

data class Post(
        val title:String,
        val photo:String,
        val description: String,
        val subscribe:Boolean,
        val idsOfSubscribers: List<Int>)
