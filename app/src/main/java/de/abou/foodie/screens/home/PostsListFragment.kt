package de.abou.foodie.screens.home

import android.net.Uri
import android.os.Bundle
import android.util.Log
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
import de.abou.foodie.R
import de.abou.foodie.database.Post
import de.abou.foodie.databinding.PostsListFragmentBinding
import de.abou.foodie.screens.post.PostAdapter
import de.abou.foodie.screens.postInfo.PostInfoViewModel

/**
 * In this Fragment the can see the posts of the other users
 */

class PostsListFragment : Fragment(), CellClickListener {

    private lateinit var viewModel: PostsListViewModel
    private val postInfoViewModel : PostInfoViewModel by activityViewModels()
    private lateinit var binding : PostsListFragmentBinding


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {


        viewModel = ViewModelProvider(this).get(PostsListViewModel::class.java)
        //check if the current user authenticated
        observeAuthenticationState()


        binding = DataBindingUtil.inflate<PostsListFragmentBinding>(
            inflater,
            R.layout.posts_list_fragment,
            container, false
        )



        return binding.root
    }


    
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.addPostBtn.setOnClickListener{
            moveToPostForm()
        }
    }

    private fun moveToPostForm() {
        val action = PostsListFragmentDirections.actionPostsListFragmentToPostView()
        NavHostFragment.findNavController(this).navigate(action)
    }

    private fun observeAuthenticationState() {
        val action = PostsListFragmentDirections.actionPostsListFragmentToSignInFragment()
        viewModel.authenticationState.observe(viewLifecycleOwner, Observer { authenticationState ->
            when(authenticationState){
                PostsListViewModel.AuthenticationState.AUTHENTICATED ->{
                    updateUI()
                    Toast.makeText(activity, "SignedIn", Toast.LENGTH_SHORT).show()
                }else->{
                    NavHostFragment.findNavController(this).navigate(action)
                }
            }
        })
    }


    private fun updateUI(){
        val adapter = PostAdapter(this)
        binding.postsListFragment.adapter = adapter
        viewModel.posts.observe(viewLifecycleOwner, Observer {
            it?.let {
                adapter.submitList(it)
            }
        })
    }

    /**
     * Send the data of the clicked post and nav to the postInfoFragment
     */
    override fun onCellClickListener(data: Post) {
        postInfoViewModel.postIdLiveData.value = data.postId
        postInfoViewModel.titleLiveData.value = data.title
        postInfoViewModel.descriptionLiveData.value = data.description
        postInfoViewModel.imageLiveData.value =Uri.parse(data.photo)
        postInfoViewModel.imageRefLiveData.value = data.imageRef
        postInfoViewModel.switchCheckedLiveData.value = data.subscribe
        postInfoViewModel.postOwnerIdLiveData.value = data.owner
        moveToPostInfo()
    }

    private fun moveToPostInfo() {
        var action = PostsListFragmentDirections.actionPostsListFragmentToPostInfoFragment()
        NavHostFragment.findNavController(this).navigate(action)
    }


}
















