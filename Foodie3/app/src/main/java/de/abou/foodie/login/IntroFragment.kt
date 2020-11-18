package de.abou.foodie.login

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import com.firebase.ui.auth.IdpResponse
import de.abou.foodie.R
import de.abou.foodie.databinding.FragmentIntroBinding
import com.google.firebase.auth.FirebaseAuth
import com.firebase.ui.auth.AuthUI


class IntroFragment : Fragment() {

    companion object {
        const val TAG = "IntroFragment"
        const val SIGN_IN_RESULT_CODE = 1001
    }

    private val viewModel by activityViewModels<LoginFragmentViewModel>()

    private lateinit var navController: NavController
    private lateinit var binding: FragmentIntroBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment


         binding = DataBindingUtil.inflate<FragmentIntroBinding>(
            inflater,
            R.layout.fragment_intro,
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
            ).build(), SIGN_IN_RESULT_CODE
        )
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == SIGN_IN_RESULT_CODE) {
            val response = IdpResponse.fromResultIntent(data)
            if (resultCode == Activity.RESULT_OK) {
                // Successfully signed in user.
                Log.i(
                    TAG,
                    "Successfully signed in user " +
                            "${FirebaseAuth.getInstance().currentUser?.displayName}!"
                )
            } else {
                Log.i(TAG, "Sign in unsuccessful ${response?.error?.errorCode}")
            }
        }
    }
}