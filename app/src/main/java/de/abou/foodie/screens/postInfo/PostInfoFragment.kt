package de.abou.foodie.screens.postInfo

import android.Manifest
import android.content.ActivityNotFoundException
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.NavHostFragment
import com.google.firebase.auth.FirebaseAuth
import com.squareup.picasso.Picasso
import de.abou.foodie.R
import de.abou.foodie.databinding.PostInfoFragmentBinding
import de.abou.foodie.screens.post.PostFragment
import de.abou.foodie.screens.postInfo.PostInfoViewModel

/**
 * In this Fragment can the user see the details of each post and edit it if he is the owner of thr post
 */

class PostInfoFragment : Fragment(){


    private lateinit var binding : PostInfoFragmentBinding

    private val viewModel : PostInfoViewModel by activityViewModels()


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {


        binding = DataBindingUtil.inflate<PostInfoFragmentBinding>(
                inflater,
                R.layout.post_info_fragment,
                container, false
        )


        updateUI()


        binding.viewModel = viewModel
        binding.lifecycleOwner = viewLifecycleOwner

        return binding.root
    }

    /**
     *Update the UI according to teh post and the owner
     * if the id exists in the postidsList of the current user then he can edit it but not subscribe it
     */
    private fun updateUI() {
        viewModel.userLiveData.observe(viewLifecycleOwner, Observer {
            for (id in it.idsOfPosts){
                if (id == viewModel.postIdLiveData.value){
                    Log.i("A7a", id)
                    binding.subscribe.visibility = View.INVISIBLE
                    binding.editBtn.visibility = View.VISIBLE
                    binding.deleteBtn.visibility = View.VISIBLE
                    binding.postInfoTitle.visibility = View.INVISIBLE
                    binding.postInfoDescription.visibility = View.INVISIBLE
                    binding.ownerPostTitle.visibility = View.VISIBLE
                    binding.ownerPostDescription.visibility = View.VISIBLE
                    binding.ownerNameLabel.visibility = View.INVISIBLE
                    binding.ownerEmailLabel.visibility = View.INVISIBLE
                    binding.ownerEmail.visibility = View.INVISIBLE
                    binding.ownerName.visibility = View.INVISIBLE
                }
            }
            //check if the post was subscribed  from the current used
            for(id in it.idsOfSubscribedPosts){
                binding.subscribe.isChecked = id == viewModel.postIdLiveData.value
            }
        })
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        //observe if the process was successful
        viewModel.updatePostLivedata.observe(viewLifecycleOwner, Observer {
            when{
                it->{
                    moveToHome()
                    viewModel.resetUpdateLivedate()
                }else->{
                    Toast.makeText(context, "Please fill the all fields", Toast.LENGTH_SHORT).show()
            }
            }
        })
        //observe if the process was successful
        viewModel.deleteLivedata.observe(viewLifecycleOwner, Observer {
            when{
                it->{
                    moveToMyPosts()
                    viewModel.resetDeleteLivedate()
                }else->{
                Toast.makeText(context, "Can't delete the Post", Toast.LENGTH_SHORT)
            }
            }
        })

        //put the image using third Lib. in the image view
        viewModel.imageLiveData.observe(viewLifecycleOwner, Observer {
            Picasso.get().load(it).into(binding.postInfoImage);
        })

        binding.postInfoImage.setOnClickListener{
            checkPermission()
        }

        viewModel.postOwnerIdLiveData.observe(viewLifecycleOwner, Observer {
            viewModel.getOwnerData()
        })
    }

    private fun moveToMyPosts() {
        var action = PostInfoFragmentDirections.actionPostInfoFragmentToMyPostsFragment()
        NavHostFragment.findNavController(this).navigate(action)
    }

    private fun moveToHome() {
        var action = PostInfoFragmentDirections.actionPostInfoFragmentToMyPostsFragment()
        NavHostFragment.findNavController(this).navigate(action)
    }

    private fun checkPermission() {
        Toast.makeText(activity, "Hello", Toast.LENGTH_SHORT).show()
        viewModel.checkPermissionLiveData.observe(viewLifecycleOwner, Observer {it
            when{
                it-> {
                    Toast.makeText(activity, "Bye", Toast.LENGTH_SHORT).show()
                    permissionGranted()
                    openGalleryForImage()
                }
                else->{
                    Toast.makeText(activity, "Bye Bye", Toast.LENGTH_SHORT).show()
                    requestPermissions(arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE), PostFragment.REQUEST_CODE)
                }
            }
        })

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        binding.postInfoImage.setImageURI(data?.data)

    }

    override fun onDestroy() {
        super.onDestroy()
        viewModel.postOwnerIdLiveData.value = ""
    }

    private fun permissionGranted() = ContextCompat.checkSelfPermission(
            requireActivity(),
            Manifest.permission.READ_EXTERNAL_STORAGE
    ) == PackageManager.PERMISSION_GRANTED

    override fun onRequestPermissionsResult(
            requestCode: Int,
            permissions: Array<String>, grantResults: IntArray) {
        if (requestCode == PostFragment.REQUEST_CODE) {
            if (permissions[0]  == Manifest.permission.READ_EXTERNAL_STORAGE &&
                    grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                viewModel.checkPermissionLiveData.value = true
            }
        }
    }

    private fun openGalleryForImage(){
        val intent = Intent(Intent.ACTION_PICK)
        intent.type = "image/*"
        try {
            startActivityForResult(intent, PostFragment.REQUEST_CODE)
        }catch (e: ActivityNotFoundException){
            Log.e(PostFragment.TAG, "Can't open the Gallery",e)
        }
    }

}