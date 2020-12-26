  package de.abou.foodie.screens.regestration.signUp

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.NavHostFragment
import de.abou.foodie.R
import de.abou.foodie.databinding.SignUpFragmentBinding

  class SignUpFragment : Fragment() {


    private lateinit var viewModel: SignUpViewModel
    private lateinit var binding : SignUpFragmentBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment

        viewModel = ViewModelProvider(this).get(SignUpViewModel::class.java)
        binding = DataBindingUtil.inflate<SignUpFragmentBinding>(
            inflater,
            R.layout.sign_up_fragment,
            container, false
        )

        //Binding
        binding.signUpViewModel = viewModel
        binding.lifecycleOwner = viewLifecycleOwner



        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.eventSignUp.observe(viewLifecycleOwner, Observer { signedUp->
            when{
                signedUp -> moveToPost()
                else->{
                    moveToSignIn()
                }
            }
        })

    }

      private fun showError() {
          Toast.makeText(activity, "Authentication failed.",
                  Toast.LENGTH_SHORT).show()
      }

      private fun moveToPost() {
          Toast.makeText(activity, "User created", Toast.LENGTH_SHORT).show()
          val action = SignUpFragmentDirections.actionSignUpFragmentToPostFragment()
          NavHostFragment.findNavController(this).navigate(action)
      }

      private fun moveToSignIn(){
          Toast.makeText(activity, "Email already exists please sign in", Toast.LENGTH_SHORT).show()
          val action = SignUpFragmentDirections.actionSignUpFragmentToSignInFragment()
          NavHostFragment.findNavController(this).navigate(action)
      }
}