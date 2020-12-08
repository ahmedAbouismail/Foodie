package de.abou.foodie.screens.title

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
import com.firebase.ui.auth.AuthUI
import de.abou.foodie.R
import de.abou.foodie.databinding.PostFragmentBinding


class PostFragment : Fragment() {

    private lateinit var viewModel: PostViewModel

    private lateinit var binding : PostFragmentBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        viewModel = ViewModelProvider(this).get(PostViewModel::class.java)


        binding = DataBindingUtil.inflate<PostFragmentBinding>(
            inflater,
            R.layout.post_fragment,
            container, false
        )

        binding.signOutBtn.setOnClickListener {

            signOut()
            val action = PostFragmentDirections.actionPostFragmentToSignInFragment()
            NavHostFragment.findNavController(this).navigate(action)
        }
        // Inflate the layout for this fragment
        return binding.root
    }




    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        observeAuthenticationState()
    }

    private fun observeAuthenticationState() {
        val action = PostFragmentDirections.actionPostFragmentToSignInFragment()
        viewModel.authenticationState.observe(viewLifecycleOwner, Observer { authenticationState ->
            when(authenticationState){
                PostViewModel.AuthenticationState.AUTHENTICATED->{
                    Toast.makeText(activity, "SignedIn", Toast.LENGTH_SHORT).show()
                }else->{
                    NavHostFragment.findNavController(this).navigate(action)
                }
            }
        })
    }

    fun signOut(){
        AuthUI.getInstance().signOut(requireContext())
    }

}
















