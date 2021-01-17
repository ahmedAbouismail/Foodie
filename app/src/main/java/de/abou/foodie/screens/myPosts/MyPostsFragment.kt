package de.abou.foodie.screens.myPosts

import android.net.Uri
import android.os.Bundle
import android.view.*
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.NavHostFragment
import de.abou.foodie.R
import de.abou.foodie.database.Post
import de.abou.foodie.databinding.PostsListFragmentBinding
import de.abou.foodie.screens.post.PostAdapter
import de.abou.foodie.screens.home.CellClickListener
import de.abou.foodie.screens.postInfo.PostInfoViewModel

/**
 * In this Fragment can the user see his own Posts
 */
class MyPostsFragment : Fragment(), CellClickListener {

    private lateinit var viewModel: MyPostsViewModel

    private lateinit var binding : PostsListFragmentBinding
    private val postInfoViewModel : PostInfoViewModel by activityViewModels()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment

        viewModel = ViewModelProvider(this).get(MyPostsViewModel::class.java)



        binding = DataBindingUtil.inflate<de.abou.foodie.databinding.PostsListFragmentBinding>(
                inflater,
                R.layout.posts_list_fragment,
                container, false
        )

        val adapter = PostAdapter(this)
        binding.postsListFragment.adapter = adapter



        viewModel.myPostsLiveData.observe(viewLifecycleOwner, Observer {
            it?.let {
                adapter.submitList(it)
            }
        })

        binding.addPostBtn.visibility = View.INVISIBLE

        return binding.root
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.navdrawer_menu, menu)
    }



    /**
     * Send the data of the clicked post and nav to the postInfoFragment
     */
    override fun onCellClickListener(data: Post) {
        postInfoViewModel.postIdLiveData.value = data.postId
        postInfoViewModel.titleLiveData.value = data.title
        postInfoViewModel.descriptionLiveData.value = data.description
        postInfoViewModel.imageLiveData.value = Uri.parse(data.photo)
        postInfoViewModel.imageRefLiveData.value = data.imageRef
        postInfoViewModel.postOwnerIdLiveData.value = data.owner
        postInfoViewModel.switchCheckedLiveData.value = data.subscribe
        moveToPostInfo()
    }

    private fun moveToPostInfo() {
        var action = MyPostsFragmentDirections.actionMyPostsFragmentToPostInfoFragment()
        NavHostFragment.findNavController(this).navigate(action)
    }


}