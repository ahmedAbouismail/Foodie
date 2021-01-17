package de.abou.foodie.User

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import de.abou.foodie.R
import de.abou.foodie.databinding.PostsListFragmentBinding
import de.abou.foodie.databinding.SubscriberListFragmentBinding
import de.abou.foodie.screens.home.PostsListViewModel
import de.abou.foodie.screens.post.PostAdapter
import de.abou.foodie.screens.postInfo.PostInfoViewModel


class SubscriberListFragment : Fragment() {

//    private lateinit var viewModel: SubscriberListViewModel
//    private val postInfoViewModel : PostInfoViewModel by activityViewModels()
    private val viewModel : SubscriberListViewModel by activityViewModels()
    private lateinit var binding : SubscriberListFragmentBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {

        binding = DataBindingUtil.inflate<SubscriberListFragmentBinding>(
                inflater,
                R.layout.subscriber_list_fragment,
                container, false
        )



        val adapter = UserAdapter()
        binding.subscriberListFragment.adapter = adapter
        viewModel.users.observe(viewLifecycleOwner, Observer {
            it?.let {
                adapter.submitList(it)
            }
        })

        binding.lifecycleOwner = this
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.subscribersId.observe(viewLifecycleOwner, Observer {
            viewModel.getSubscribersInfo()
        })
    }

}