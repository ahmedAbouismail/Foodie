package de.abou.foodie.screens.title


import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import de.abou.foodie.R
import de.abou.foodie.database.Post



class postAdapter:RecyclerView.Adapter<postAdapter.ViewHolder>() {


    var data = listOf<Post>()
    set(value) {
        field = value
        notifyDataSetChanged()
    }
    override fun getItemCount() = data.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = data[position]
        holder.bind(item)
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder.from(parent)
    }


    class ViewHolder(private val itemView: View):RecyclerView.ViewHolder(itemView){
        val title : TextView = itemView.findViewById(R.id.postTitle)

        fun bind(item: Post){
            val res = itemView.context.resources
            title.text = "Ahmed"
        }

        companion object{
            public fun from(parent: ViewGroup):ViewHolder{
                val layoutInflater = LayoutInflater.from(parent.context)
                val view = layoutInflater
                        .inflate(R.layout.posts_list_fragment, parent, false)

                return ViewHolder(view)
            }
    }


    }






}