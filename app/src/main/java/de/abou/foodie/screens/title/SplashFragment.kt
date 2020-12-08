package de.abou.foodie.screens.title

import android.os.Bundle
import android.os.Handler
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.viewModels
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import de.abou.foodie.R
import de.abou.foodie.databinding.FragmentSplashBinding
import de.abou.foodie.screens.regestration.signIn.SignInViewModel


class SplashFragment : Fragment() {

    companion object {
        const val TAG = "SplashFragment"
        const val SIGN_IN_RESULT_CODE = 1001
    }

    private val viewModel by viewModels<SignInViewModel>()
    private lateinit var auth: FirebaseAuth
    private lateinit var navController: NavController


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = DataBindingUtil.inflate<FragmentSplashBinding>(
            inflater, R.layout.fragment_splash, container, false
        )
        auth = Firebase.auth
//        findNavController().navigate(R.id.action_splashFragment_to_loginFragment)
//        Handler().postDelayed({observeAuthenticationState()}, 3000)
        navController = findNavController()
//        Handler().postDelayed({
//            onStart()
////            var currentUserID = Firestore().getCurrentUserId()
////
////            if(currentUserID.isNotEmpty()){
////                navController.navigate(R.id.action_splashFragment_to_postsFragment)
////            }else{
////               navController.navigate(R.id.action_splashFragment_to_loginFragment)
////            }
//        }, 2500)

        return binding.root
    }

    override fun onStart() {
        super.onStart()
        val currentUser = auth.currentUser
        updateUI(currentUser)
    }

    private fun updateUI(currentUser: FirebaseUser?) {
        Handler().postDelayed({

            if(currentUser != null){

            }else{

            }
        }, 2500)
    }
}