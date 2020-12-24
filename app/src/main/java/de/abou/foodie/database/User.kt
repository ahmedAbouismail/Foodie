package de.abou.foodie.database

data class User(
        val firstName:String?=null,
        val lastName:String? = null,
        val email:String,
        val photo:String? = null,
        val idsOfPosts : List<String> = emptyList())
