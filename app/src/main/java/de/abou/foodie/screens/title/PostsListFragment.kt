package de.abou.foodie.screens.title

import android.net.Uri
import android.os.Bundle
import android.text.Editable
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import com.squareup.picasso.Picasso
import de.abou.foodie.R
import de.abou.foodie.database.MyFirestore
import de.abou.foodie.database.Post
import de.abou.foodie.databinding.PostFragmentBinding
import de.abou.foodie.databinding.PostsListFragmentBinding


class PostsListFragment : Fragment(), CellClickListener {

    private lateinit var viewModel: PostsListViewModel

    private val postInfoViewModel : PostInfoViewModel by activityViewModels()
    private lateinit var binding : PostsListFragmentBinding


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        viewModel = ViewModelProvider(this).get(PostsListViewModel::class.java)
//        postViewModel = ViewModelProvider().get(PostViewModel::class.java)

        binding = DataBindingUtil.inflate<PostsListFragmentBinding>(
            inflater,
            R.layout.posts_list_fragment,
            container, false
        )


        val adapter = PostAdapter(this)
        binding.postList.adapter = adapter


        observeAuthenticationState()

        viewModel.posts.observe(viewLifecycleOwner, Observer {
            it?.let {
                adapter.submitList(it)
            }
        })

        // Inflate the layout for this fragment
        return binding.root
    }


    
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.addPostBtn.setOnClickListener{
            moveToPostForm()
        }
    }

    private fun moveToPostForm() {
        val action = PostsListFragmentDirections.actionPostListFragmentToPostFragment()
        NavHostFragment.findNavController(this).navigate(action)
    }

    private fun observeAuthenticationState() {
        val action = PostsListFragmentDirections.actionPostFragmentToSignInFragment()
        viewModel.authenticationState.observe(viewLifecycleOwner, Observer { authenticationState ->
            when(authenticationState){
                PostsListViewModel.AuthenticationState.AUTHENTICATED->{
                    Toast.makeText(activity, "SignedIn", Toast.LENGTH_SHORT).show()
                }else->{
                    NavHostFragment.findNavController(this).navigate(action)
                }
            }
        })
    }

    override fun onCellClickListener(data: Post) {
        postInfoViewModel.postIdLiveData.value = data.postId
        postInfoViewModel.titleLiveData.value = data.title
        postInfoViewModel.descriptionLiveData.value = data.description
        postInfoViewModel.imageLiveData.value =Uri.parse(data.photo)
        var action = PostsListFragmentDirections.actionPostListFragmentToPostInfoFragment()
        NavHostFragment.findNavController(this).navigate(action)
        Toast.makeText(context,data.postId, Toast.LENGTH_SHORT).show()
    }


}
















