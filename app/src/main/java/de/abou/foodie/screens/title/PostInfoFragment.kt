package de.abou.foodie.screens.title

import android.net.Uri
import android.os.Bundle
import android.text.Editable
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.squareup.picasso.Picasso
import de.abou.foodie.R
import de.abou.foodie.database.Post
import de.abou.foodie.databinding.PostInfoFragmentBinding


class PostInfoFragment : Fragment(){

//    private lateinit var viewModel: PostInfoViewModel
    private lateinit var binding : PostInfoFragmentBinding
    private val postInfoViewModel : PostInfoViewModel by activityViewModels()


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {


        binding = DataBindingUtil.inflate<PostInfoFragmentBinding>(
                inflater,
                R.layout.post_info_fragment,
                container, false
        )


        postInfoViewModel.userLiveData.observe(viewLifecycleOwner, Observer {
            for (id in it.idsOfPosts){
                if (id == postInfoViewModel.postIdLiveData.value){
                    binding.postInfoSwitch.visibility = View.INVISIBLE
                    binding.editBtn.visibility = View.VISIBLE
                    binding.postInfoTitle.visibility = View.INVISIBLE
                    binding.postInfoDescription.visibility = View.INVISIBLE
                    binding.ownerPostTitle.visibility = View.VISIBLE
                    binding.ownerPostDescription.visibility = View.VISIBLE
                }
            }
        })

        // Inflate the layout for this fragment
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        postInfoViewModel.titleLiveData.observe(viewLifecycleOwner, Observer {
            binding.ownerPostTitle.text = Editable.Factory.getInstance().newEditable(it)
        })
        postInfoViewModel.descriptionLiveData.observe(viewLifecycleOwner, Observer {
            binding.ownerPostDescription.text = Editable.Factory.getInstance().newEditable(it)
        })
        postInfoViewModel.imageLiveData.observe(viewLifecycleOwner, Observer {
            Picasso.get().load(it).into(binding.postInfoImage);

        })
    }

}