package de.abou.foodie.screens.title

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import de.abou.foodie.R
import de.abou.foodie.databinding.FragmentSplashBinding
import de.abou.foodie.databinding.MyProfileFragmentBinding
import de.abou.foodie.screens.home.PostsListFragmentDirections
import de.abou.foodie.screens.home.PostsListViewModel
import de.abou.foodie.screens.regestration.signIn.SignInViewModel
import de.abou.foodie.screens.userInfo.MyProfileViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


class SplashFragment : Fragment() {

    companion object {
        const val TAG = "SplashFragment"
        const val SIGN_IN_RESULT_CODE = 1001
    }

    private lateinit var viewModel: SplashViewModel
    private lateinit var binding : FragmentSplashBinding


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = DataBindingUtil.inflate<FragmentSplashBinding>(
                inflater,
                R.layout.fragment_splash,
                container, false
        )
        viewModel = ViewModelProvider(this).get(SplashViewModel::class.java)

        binding.lifecycleOwner = this
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        observeAuthenticationState()
    }
    private fun observeAuthenticationState() {

        viewModel.authenticationState.observe(viewLifecycleOwner, Observer { authenticationState ->
            when(authenticationState){
                SplashViewModel.AuthenticationState.AUTHENTICATED ->{
                    Handler(Looper.getMainLooper()).postDelayed({moveToHome()}, 2000)
                }else->{
                Handler(Looper.getMainLooper()).postDelayed({moveToSignIn()}, 2000)
            }
            }
        })
    }

    private fun moveToHome(){
        val action = SplashFragmentDirections.actionSplashFragmentToMyPostsFragment()
        NavHostFragment.findNavController(this).navigate(action)
    }
    private fun moveToSignIn(){
        val action = SplashFragmentDirections.actionSplashFragmentToSignInFragment()
        NavHostFragment.findNavController(this).navigate(action)
    }

}