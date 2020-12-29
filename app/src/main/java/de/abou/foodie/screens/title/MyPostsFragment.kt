package de.abou.foodie.screens.title

import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import de.abou.foodie.R
import de.abou.foodie.databinding.MyPostsFragmentBinding
import de.abou.foodie.databinding.PostsListFragmentBinding

class MyPostsFragment : Fragment() {

    private lateinit var viewModel: MyPostsViewModel

    private lateinit var binding : PostsListFragmentBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment

        viewModel = ViewModelProvider(this).get(MyPostsViewModel::class.java)



        binding = DataBindingUtil.inflate<de.abou.foodie.databinding.PostsListFragmentBinding>(
                inflater,
                R.layout.posts_list_fragment,
                container, false
        )

        val adapter = PostAdapter()
        binding.postList.adapter = adapter



        viewModel.myPostsLiveData.observe(viewLifecycleOwner, Observer {
            it?.let {
                adapter.submitList(it)
            }
        })

        setHasOptionsMenu(true)
        return binding.root
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.navdrawer_menu, menu)
    }


}