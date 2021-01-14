package de.abou.foodie.screens.home


import de.abou.foodie.database.Post

interface CellClickListener {
    fun onCellClickListener(data:Post)
}