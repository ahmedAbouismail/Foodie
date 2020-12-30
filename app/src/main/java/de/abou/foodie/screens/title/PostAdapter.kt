package de.abou.foodie.screens.title


import android.net.Uri
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso
import de.abou.foodie.R
import de.abou.foodie.database.Post
import de.abou.foodie.databinding.PostItemViewBinding


class PostAdapter(private val cellClickListener: CellClickListener):androidx.recyclerview.widget.ListAdapter<Post, PostAdapter.ViewHolder>(PostDiffCallback()) {

    class PostDiffCallback : DiffUtil.ItemCallback<Post>(){
        override fun areItemsTheSame(oldItem: Post, newItem: Post): Boolean {
            return oldItem.postId == newItem.postId
        }

        override fun areContentsTheSame(oldItem: Post, newItem: Post): Boolean {
            return oldItem == newItem
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder.from(parent)!!
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = getItem(position)
        holder.bind(item)
        holder.itemView.setOnClickListener{cellClickListener.onCellClickListener(item)}
//        holder.itemView.setOnClickListener{
//            var action = PostsListFragmentDirections.actionPostListFragmentToPostFragment()
//            NavHostFragment().findNavController().navigate(action)
//        }
    }


    class ViewHolder private constructor (val binding: PostItemViewBinding):RecyclerView.ViewHolder(binding.root){


        fun bind(item: Post) {

            binding.item = item
            binding.executePendingBindings()
//            Picasso.get().load(Uri.parse(item.photo)).into(binding.postImage)
        }


        companion object {
            fun from(parent: ViewGroup): ViewHolder? {
                val layoutInflater = LayoutInflater.from(parent.context)

                val binding = PostItemViewBinding.inflate(layoutInflater, parent, false)
                return ViewHolder(binding)
            }
        }
    }
}