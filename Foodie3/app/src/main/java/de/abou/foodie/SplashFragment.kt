package de.abou.foodie

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import com.firebase.ui.auth.AuthUI
import de.abou.foodie.Firebase.Firestore
import de.abou.foodie.databinding.FragmentSplashBinding
import de.abou.foodie.login.LoginFragmentViewModel


class SplashFragment : Fragment() {
    private val viewModel by viewModels<LoginFragmentViewModel>()

    private lateinit var navController: NavController
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = DataBindingUtil.inflate<FragmentSplashBinding>(
            inflater, R.layout.fragment_splash, container, false
        )
//        findNavController().navigate(R.id.action_splashFragment_to_loginFragment)
//        Handler().postDelayed({observeAuthenticationState()}, 3000)
        navController = findNavController()
        Handler().postDelayed({

            var currentUserID = Firestore().getCurrentUserId()

            if(currentUserID.isNotEmpty()){
                navController.navigate(R.id.testFragment)
            }else{
                navController.navigate(R.id.myProfileFragment)
            }
        }, 2500)

        return binding.root
    }

    private fun observeAuthenticationState() {

        viewModel.authenticationState.observe(viewLifecycleOwner, Observer { authenticationState ->
            when (authenticationState) {
                LoginFragmentViewModel.AuthenticationState.AUTHENTICATED -> {
                    navController.navigate(R.id.testFragment)
                }
                else -> {
                    navController.navigate(R.id.myProfileFragment)
                }
            }
        })
    }

}