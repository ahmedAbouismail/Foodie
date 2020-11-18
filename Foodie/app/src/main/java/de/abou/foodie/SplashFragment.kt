package de.abou.foodie

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.navigation.findNavController
import de.abou.foodie.databinding.FragmentSplashBinding


class SplashFragment : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = DataBindingUtil.inflate<FragmentSplashBinding>(
            inflater, R.layout.fragment_splash, container, false
        )
        binding.splashButton.setOnClickListener { view: View ->
            view.findNavController().navigate(R.id.action_splashFragment_to_introFragment)
        }
        return binding.root
    }
}