package de.abou.foodie.screens.regestration.Signout

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.ContentProviderCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.NavHostFragment
import com.firebase.ui.auth.AuthUI
import de.abou.foodie.R
import de.abou.foodie.databinding.SignInFragmentBinding
import de.abou.foodie.screens.regestration.signIn.SignInFragmentDirections


class SignOutFragment : Fragment() {

    private lateinit var viewModel: SignOutViewModel




    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        viewModel = ViewModelProvider(this).get(SignOutViewModel::class.java)
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_signout, container, false)
    }



    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        moveToSignIn()
    }
    private fun moveToSignIn(){
        val actionToSignUp = SignOutFragmentDirections.actionSignOutFragmentToSignInFragment()
        NavHostFragment.findNavController(this).navigate(actionToSignUp)
        signOut()
    }
    private fun signOut(){
        AuthUI.getInstance().signOut(requireContext())
    }

}