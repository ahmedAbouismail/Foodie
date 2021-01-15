package de.abou.foodie.screens.userInfo

import android.Manifest
import android.content.ActivityNotFoundException
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.NavHostFragment
import de.abou.foodie.R
import de.abou.foodie.databinding.UserInfoFragmentBinding
import de.abou.foodie.screens.post.PostFragment


class UserInfoFragment : Fragment() {

    private lateinit var viewModel: UserInfoViewModel
    private lateinit var binding : UserInfoFragmentBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate<UserInfoFragmentBinding>(
            inflater,
            R.layout.user_info_fragment,
            container, false
        )
        viewModel = ViewModelProvider(this).get(UserInfoViewModel::class.java)


        binding.lifecycleOwner = this
        binding.viewModel = viewModel
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.taskLiveData.observe(viewLifecycleOwner, Observer {
            var action = UserInfoFragmentDirections.actionUserInfoToPostsListFragment()
            when{
                it-> {
                    NavHostFragment.findNavController(this).navigate(action)
                }else->{
                Toast.makeText(context, "Please check that all fields are not empty", Toast.LENGTH_SHORT).show()
            }
            }
        })

    }


}
