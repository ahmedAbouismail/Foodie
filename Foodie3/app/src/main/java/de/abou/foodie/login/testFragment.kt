package de.abou.foodie.login

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import de.abou.foodie.R
import de.abou.foodie.databinding.FragmentLoginBinding
import de.abou.foodie.databinding.FragmentTestBinding


class testFragment : Fragment() {


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = DataBindingUtil.inflate<FragmentTestBinding>(
            inflater,
            R.layout.fragment_test,
            container, false
        )
        // Inflate the layout for this fragment
        return binding.root
    }

}