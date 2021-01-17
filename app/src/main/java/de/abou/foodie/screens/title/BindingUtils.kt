package de.abou.foodie.screens.title

import android.net.Uri
import android.widget.ImageView
import android.widget.TextView
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide
import de.abou.foodie.R
import de.abou.foodie.database.Post
import de.abou.foodie.database.User

@BindingAdapter("postTitle")
fun TextView.setTitleFormatted(item: Post) {
    text = item.title
}
@BindingAdapter("postPrice")
fun TextView.setPrice(item: Post) {
    text = item.price
}

@BindingAdapter("postDescription")
fun TextView.setDescription(item: Post){
    text = item.description
}

@BindingAdapter("postImage")
fun ImageView.setImage(item: Post){
    Glide
        .with(this)
        .load(Uri.parse(item.photo))
        .centerCrop()
        .placeholder(R.drawable.app_logo)
        .into(this);
//    Picasso.get().load(Uri.parse(item.photo)).into(this)
}

@BindingAdapter("subsFirstName")
fun TextView.setSubsFirstName(item: User) {
    text = item.firstName
}

@BindingAdapter("subsLastName")
fun TextView.setSubslastName(item: User){
    text = item.lastName
}
