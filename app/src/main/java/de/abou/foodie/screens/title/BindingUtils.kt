package de.abou.foodie.screens.title

import android.net.Uri
import android.widget.ImageView
import android.widget.TextView
import androidx.core.view.PointerIconCompat.load
import androidx.databinding.BindingAdapter
import androidx.fragment.app.findFragment
import com.bumptech.glide.Glide
import com.google.android.gms.ads.appopen.AppOpenAd.load
import com.google.android.gms.maps.model.Circle
import com.squareup.picasso.Picasso
import de.abou.foodie.R
import de.abou.foodie.database.Post
import de.abou.foodie.database.User
import java.lang.System.load

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
    Glide
        .with(this)
        .load(Uri.parse(item.photo))
        .circleCrop()
        .placeholder(R.drawable.vegetables)
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
