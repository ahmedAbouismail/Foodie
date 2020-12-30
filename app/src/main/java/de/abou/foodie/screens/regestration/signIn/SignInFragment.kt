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

        observeAuthenticationState()


        return binding.root
    }



    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.eventSignIn.observe(viewLifecycleOwner, Observer { signedIn->
            when{
                signedIn -> moveToPost()
                else->{
                    Toast.makeText(activity, "Please sign up", Toast.LENGTH_SHORT).show()
                    moveToSignUp()
                }
            }
        })
    }

    private fun observeAuthenticationState() {
        val action = SignInFragmentDirections.actionSignInFragmentToPostListFragment()
        viewModel.authenticationState.observe(viewLifecycleOwner, Observer { authenticationState ->
            when(authenticationState){
                SignInViewModel.AuthenticationState.AUTHENTICATED->{
                    NavHostFragment.findNavController(this).navigate(action)
                    Toast.makeText(activity, "SignedIn", Toast.LENGTH_SHORT).show()
                }else->{
                Toast.makeText(activity, "Sign In please", Toast.LENGTH_SHORT).show()
            }
            }
        })
    }

    private fun moveToSignUp() {
        val actionToSignUp = SignInFragmentDirections.actionSignInFragmentToSignUpFragment()
        NavHostFragment.findNavController(this).navigate(actionToSignUp)
    }

    private fun moveToPost() {

        val actionToPost = SignInFragmentDirections.actionSignInFragmentToPostListFragment()
        NavHostFragment.findNavController(this).navigate(actionToPost)
    }

}





