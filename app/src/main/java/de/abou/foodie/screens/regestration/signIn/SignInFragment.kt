package de.abou.foodie.screens.regestration.signIn


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.navigation.fragment.NavHostFragment

import de.abou.foodie.R
import de.abou.foodie.databinding.SignInFragmentBinding
import de.abou.foodie.screens.regestration.signUp.SignUpFragmentDirections


class SignInFragment : Fragment() {


    private lateinit var viewModel: SignInViewModel

    private lateinit var binding: SignInFragmentBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {


        //Init for the lateinit var
        viewModel = ViewModelProvider(this).get(SignInViewModel::class.java)
        binding = DataBindingUtil.inflate<SignInFragmentBinding>(
                inflater,
                R.layout.sign_in_fragment,
                container, false
        )

        //Binding
        binding.signInViewModel = viewModel
        binding.lifecycleOwner = viewLifecycleOwner


        signIn()

        return binding.root
    }

    private fun signIn() {
        val actionToPost = SignInFragmentDirections.actionSignInFragmentToPostFragment()
        val actionToSignUp = SignInFragmentDirections.actionSignInFragmentToSignUpFragment()
        viewModel.eventSignIn.observe(viewLifecycleOwner, Observer { hasSignedIn ->
            if (hasSignedIn) {
                Toast.makeText(activity, "Authentication succeed.",
                        Toast.LENGTH_SHORT).show()
                NavHostFragment.findNavController(this).navigate(actionToPost)
            } else {
                Toast.makeText(activity, "Authentication failed.",
                        Toast.LENGTH_SHORT).show()
                NavHostFragment.findNavController(this).navigate(actionToSignUp)
            }
        })
    }

}





