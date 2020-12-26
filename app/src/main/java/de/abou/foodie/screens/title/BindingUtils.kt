package de.abou.foodie.screens.title

import android.net.Uri
import android.widget.ImageView
import android.widget.TextView
import androidx.databinding.BindingAdapter
import com.squareup.picasso.Picasso
import de.abou.foodie.database.Post

@BindingAdapter("postTitle")
fun TextView.setTitleFormatted(item: Post) {
    text = item.title
}

@BindingAdapter("postDescription")
fun TextView.setDescription(item: Post){
    text = item.description
}

@BindingAdapter("postImage")
fun ImageView.setImage(item: Post){
    Picasso.get().load(Uri.parse(item.photo)).into(this)
}