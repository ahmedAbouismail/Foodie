package de.abou.foodie.database

data class User(
        val firstName:String="",
        val lastName:String = "",
        val email:String = "",
        val photo:String = "",
        val idsOfPosts : MutableList<String> = mutableListOf(),
        val idsOfSubscribedPosts : MutableList<String> = mutableListOf())
