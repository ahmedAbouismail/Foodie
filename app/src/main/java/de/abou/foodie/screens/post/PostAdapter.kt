package de.abou.foodie.screens.post


import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import de.abou.foodie.database.Post
import de.abou.foodie.databinding.PostItemViewBinding
import de.abou.foodie.screens.home.CellClickListener


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
    }


    class ViewHolder private constructor (val binding: PostItemViewBinding):RecyclerView.ViewHolder(binding.root){


        fun bind(item: Post) {

            binding.item = item
            binding.executePendingBindings()
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