package de.abou.foodie.screens.userInfo

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import de.abou.foodie.R
import de.abou.foodie.databinding.MyProfileFragmentBinding
import de.abou.foodie.databinding.PostFragmentBinding
import de.abou.foodie.screens.myPosts.MyPostsViewModel
import de.abou.foodie.screens.post.PostViewModel


class MyProfileFragment : Fragment() {


    private lateinit var viewModel: MyProfileViewModel
    private lateinit var binding : MyProfileFragmentBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {

        binding = DataBindingUtil.inflate<MyProfileFragmentBinding>(
                inflater,
                R.layout.my_profile_fragment,
                container, false
        )
        viewModel = ViewModelProvider(this).get(MyProfileViewModel::class.java)


        binding.lifecycleOwner = this
        binding.viewModel = viewModel
        return binding.root
    }


}