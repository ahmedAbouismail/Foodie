package de.abou.foodie.login

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import com.firebase.ui.auth.AuthUI
import de.abou.foodie.R
import de.abou.foodie.databinding.FragmentLoginBinding
import com.google.firebase.auth.FirebaseAuth
import com.firebase.ui.auth.IdpResponse
import de.abou.foodie.databinding.FragmentIntroBinding


class LoginFragment : Fragment() {
    companion object {
        const val TAG = "IntroFragment"
        const val SIGN_IN_RESULT_CODE = 1001
    }

    private val viewModel by activityViewModels<LoginFragmentViewModel>()

    private lateinit var navController: NavController
    private lateinit var binding: FragmentLoginBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val binding = DataBindingUtil.inflate<FragmentLoginBinding>(
                inflater,
                R.layout.fragment_login,
                container, false
        )

        binding.authButton.setOnClickListener { launchSignInFlow() }

        return binding.root
    }

   override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        navController = findNavController()
    }


    private fun launchSignInFlow() {

        val providers = arrayListOf(
            AuthUI.IdpConfig.EmailBuilder().build(), AuthUI.IdpConfig.GoogleBuilder().build()
        )

        startActivityForResult(
            AuthUI.getInstance().createSignInIntentBuilder().setAvailableProviders(
                providers
            ).build(), LoginFragment.SIGN_IN_RESULT_CODE
        )
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == IntroFragment.SIGN_IN_RESULT_CODE) {
            val response = IdpResponse.fromResultIntent(data)
            if (resultCode == Activity.RESULT_OK) {
                // Successfully signed in user.
                //findNavController().navigate(R.id.testFragment)
                navController.navigate(R.id.testFragment)
                Log.i(
                    IntroFragment.TAG,
                    "Successfully signed in user " +
                            "${FirebaseAuth.getInstance().currentUser?.displayName}!"
                )
            } else {
                navController.navigate(R.id.myProfileFragment)
                Log.i(IntroFragment.TAG, "Sign in unsuccessful ${response?.error?.errorCode}")
            }
        }
    }
}