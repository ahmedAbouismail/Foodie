package de.abou.foodie.screens.mySubscribedPosts

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import de.abou.foodie.R
import de.abou.foodie.database.Post
import de.abou.foodie.databinding.PostsListFragmentBinding
import de.abou.foodie.databinding.SubscribedPostsFragmentBinding
import de.abou.foodie.screens.home.CellClickListener
import de.abou.foodie.screens.myPosts.MyPostsViewModel
import de.abou.foodie.screens.post.PostAdapter



class SubscribedPostsFragment : Fragment(),CellClickListener {

    private lateinit var viewModel: SubscribedPostsViewModel

    private lateinit var binding : SubscribedPostsFragmentBinding


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {

        viewModel = ViewModelProvider(this).get(SubscribedPostsViewModel::class.java)



        binding = DataBindingUtil.inflate<SubscribedPostsFragmentBinding>(
                inflater,
                R.layout.subscribed_posts_fragment,
                container, false
        )

        val adapter = PostAdapter(this)
        binding.subscribedPostsList.adapter = adapter

        viewModel.myPostsLiveData.observe(viewLifecycleOwner, Observer {
            it?.let {
                adapter.submitList(it)
            }
        })

        return binding.root
    }

    override fun onCellClickListener(data: Post) {
        TODO("Not yet implemented")
    }

}