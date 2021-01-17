package de.abou.foodie.screens.regestration.signIn


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.NavHostFragment

import de.abou.foodie.R
import de.abou.foodie.databinding.SignInFragmentBinding
import de.abou.foodie.screens.postInfo.PostInfoViewModel
import de.abou.foodie.screens.regestration.signUp.SignUpViewModel


class SignInFragment : Fragment() {


    private lateinit var viewModel: SignInViewModel

    private lateinit var binding: SignInFragmentBinding

    private val signUpViewModel : SignUpViewModel by activityViewModels()


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {

        viewModel = ViewModelProvider(this).get(SignInViewModel::class.java)

        binding = DataBindingUtil.inflate<SignInFragmentBinding>(
                inflater,
                R.layout.sign_in_fragment,
                container, false
        )

        binding.signInViewModel = viewModel
        binding.lifecycleOwner = viewLifecycleOwner




        return binding.root
    }



    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.eventSignIn.observe(viewLifecycleOwner, Observer { signedIn ->
            when {
                signedIn -> moveToHome()
                else -> {
                    if (viewModel.email.value.isNullOrEmpty() || viewModel.password.value.isNullOrEmpty()) {
                        Toast.makeText(context, "please fill the fields", Toast.LENGTH_SHORT).show()
                    }else{
                        moveToSignUp()
                        Toast.makeText(activity, "Please sign up", Toast.LENGTH_SHORT).show()
                    }

                }
            }
        })
    }

    private fun moveToSignUp() {
        val actionToSignUp = SignInFragmentDirections.actionSignInFragmentToSignUpFragment()
        NavHostFragment.findNavController(this).navigate(actionToSignUp)
    }

    private fun moveToHome() {
        val actionToPost = SignInFragmentDirections.actionSignInFragmentToPostsListFragment()
        NavHostFragment.findNavController(this).navigate(actionToPost)
    }

}





