package de.abou.foodie.main

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.viewModels
import com.firebase.ui.auth.IdpResponse
import com.google.firebase.auth.FirebaseAuth
import de.abou.foodie.R
import de.abou.foodie.databinding.FragmentMainBinding
import de.abou.foodie.login.LoginFragmentViewModel


class MainFragment : Fragment() {

    companion object {
        const val TAG = "MainFragment"
        const val SIGN_IN_RESULT_CODE = 1001
    }
    private val viewModel by viewModels<LoginFragmentViewModel>()
    private lateinit var binding: FragmentMainBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
         binding = DataBindingUtil.inflate(inflater,R.layout.fragment_main, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        observeAuthenticationState()

//        binding.authButton.setOnClickListener {  }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(requestCode == SIGN_IN_RESULT_CODE){
            val response = IdpResponse.fromResultIntent(data)
            if(resultCode == Activity.RESULT_OK){
                Log.i(
                        TAG,
                        "Successfully signed in user ${FirebaseAuth.getInstance().currentUser?.displayName}!"
                )
            } else {
                Log.i(TAG, "Sign in unsuccessful ${response?.error?.errorCode}")
            }
        }
    }

    private fun observeAuthenticationState(){

    }
}