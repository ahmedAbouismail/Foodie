  package de.abou.foodie.screens.regestration.signUp

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
import de.abou.foodie.databinding.SignInFragmentBinding
import de.abou.foodie.databinding.SignUpFragmentBinding
import de.abou.foodie.screens.regestration.signIn.SignInFragmentDirections
import de.abou.foodie.screens.regestration.signIn.SignInViewModel

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

        signUp()

        return binding.root
    }

    private fun signUp() {
        val action = SignUpFragmentDirections.actionSignUpFragmentToPostFragment()

        viewModel.eventSignIn.observe(viewLifecycleOwner, Observer { hasSignedIn ->
            if (hasSignedIn) {
                Toast.makeText(activity, "Authentication succeed.",
                    Toast.LENGTH_SHORT).show()
                NavHostFragment.findNavController(this).navigate(action)
            } else {
                Toast.makeText(activity, "Authentication failed.",
                    Toast.LENGTH_SHORT).show()
            }
        })
    }
}