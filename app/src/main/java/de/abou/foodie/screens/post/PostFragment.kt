package de.abou.foodie.screens.post

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
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.findFragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.NavHostFragment
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.LatLngBounds
import com.google.android.libraries.places.api.Places
import com.google.android.libraries.places.api.model.Place
import com.google.android.libraries.places.api.model.RectangularBounds
import com.google.android.libraries.places.api.model.TypeFilter
import com.google.android.libraries.places.api.net.PlacesClient
import com.google.android.libraries.places.widget.AutocompleteSupportFragment
import de.abou.foodie.R
import de.abou.foodie.database.MyFirestore
import de.abou.foodie.databinding.PostFragmentBinding
import de.abou.foodie.screens.MapsFragment

/**
 * In this Fragment can the user add a new post
 */
class PostFragment : Fragment() {

    companion object{
        const val TAG = "Post Fragment"
        const val REQUEST_CODE = 100
    }

    private lateinit var viewModel: PostViewModel
    private lateinit var binding : PostFragmentBinding



    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = DataBindingUtil.inflate<PostFragmentBinding>(
                inflater,
                R.layout.post_fragment,
                container, false
        )

        val application = requireNotNull(this.activity).application
        val dataSource = MyFirestore()
        val viewModelFactory = PostViewModelFactory(dataSource, application)
        viewModel = ViewModelProvider(this, viewModelFactory).get(PostViewModel::class.java)

        observeAuthenticationState()

        binding.lifecycleOwner = this
        binding.postViewModel = viewModel
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        //check if the User accepted the permission
        binding.addPostImageBtn.setOnClickListener{
            checkPermission()
        }

        viewModel.updatePostLivedata.observe(viewLifecycleOwner, Observer {
            var action = PostFragmentDirections.actionPostViewToMyPostsFragment()
            when{
                it->{
                    NavHostFragment.findNavController(this).navigate(action)
                    Toast.makeText(context, "Post added Successfully", Toast.LENGTH_SHORT).show()
                }else->{
                Toast.makeText(context, "A filed was empty", Toast.LENGTH_SHORT).show()
                }
            }
        })
    }

    private fun checkPermission() {
        viewModel.checkPermissionLiveData.observe(viewLifecycleOwner, Observer {it
            when{
                it-> {
                    // take the permission then open the gallery
                    permissionGranted()
                    openGalleryForImage()
                }
                else->{
                    requestPermissions(arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE), REQUEST_CODE)
                }
            }
        })

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        binding.addPostImageBtn.setBackgroundResource(R.drawable.ic_add_photo_post_70)
        //set the image in the view
        binding.addPostImageBtn.setImageURI(data?.data)
        if (data?.data != null){
            //remove the drawable icon if image was added from the gallery
            binding.addPostImageBtn.setBackgroundResource(0)
        }

    }

    private fun permissionGranted() = ContextCompat.checkSelfPermission(
            requireActivity(),
            Manifest.permission.READ_EXTERNAL_STORAGE
    ) == PackageManager.PERMISSION_GRANTED

    override fun onRequestPermissionsResult(
            requestCode: Int,
            permissions: Array<String>, grantResults: IntArray) {
        if (requestCode == REQUEST_CODE) {
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
            startActivityForResult(intent, REQUEST_CODE)
        }catch (e: ActivityNotFoundException){
            Toast.makeText(context, "Can't open the Gallery", Toast.LENGTH_SHORT).show()
        }
    }


    private fun observeAuthenticationState() {
        val action = PostFragmentDirections.actionPostViewToSignInFragment()
        viewModel.authenticationState.observe(viewLifecycleOwner, Observer { authenticationState ->
            when(authenticationState){
                PostViewModel.AuthenticationState.UNAUTHENTICATED ->{
                    NavHostFragment.findNavController(this).navigate(action)
                }
            }
        })
    }
}