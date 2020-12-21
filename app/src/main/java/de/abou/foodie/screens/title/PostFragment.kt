package de.abou.foodie.screens.title

import android.Manifest
import android.app.Activity
import android.content.ActivityNotFoundException
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.Canvas
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.NavHostFragment
import de.abou.foodie.R
import de.abou.foodie.databinding.PostFragmentBinding
import kotlin.math.log


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

        viewModel = ViewModelProvider(this).get(PostViewModel::class.java)

        observeAuthenticationState()


        viewModel.imageUrlLiveData.observe(viewLifecycleOwner, Observer {
            binding.addPostImageBtn.setImageURI(viewModel.imageUrlLiveData.value)
        })

        binding.addPostImageBtn.setOnClickListener{
            Toast.makeText(activity, "Image cilcked", Toast.LENGTH_SHORT).show()
            Log.e(TAG, viewModel.checkPermissionLiveData.value.toString() + "before")
            viewModel.checkPermissionLiveData.observe(viewLifecycleOwner, Observer {it
                Log.e(TAG, it.toString() + "after")
                when{
                    it-> {
                        permissionGranted()
                        openGalleryForImage()
                    }
                    else->{
                        requestPermissions(arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE),REQUEST_CODE)
                    }
                }
            })
        }

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)



        binding.addPostBtn.setOnClickListener {
            if(binding.addPostImageBtn.drawable != null){
                viewModel.modifyImage(binding.addPostImageBtn)
            }else{
                Toast.makeText(activity, "Please upload image of product first", Toast.LENGTH_SHORT).show()
            }


            viewModel.resultLiveData.observe(viewLifecycleOwner, Observer {result->
                viewModel.uploadImageToStorage(result)
                binding.addPostImageBtn.setImageBitmap(result)
            })

            viewModel.isImageUploaded.observe(viewLifecycleOwner, Observer {isUploaded->
                when{
                    isUploaded->binding.addPostBtn.isEnabled = true
                    else->{
                        binding.addPostBtn.isEnabled = false
                    }
                }
            })
            Toast.makeText(activity, "Post Cilcked", Toast.LENGTH_SHORT).show()
        }

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        viewModel.imageUrlLiveData.value = data?.data

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
                openGalleryForImage()
            }
        }
    }

    private fun openGalleryForImage(){
        val intent = Intent(Intent.ACTION_PICK)
        intent.type = "image/*"
        try {
            startActivityForResult(intent, REQUEST_CODE)
        }catch (e: ActivityNotFoundException){
            Log.e(TAG, "Can't open the Gallery",e)
        }
    }


    private fun observeAuthenticationState() {
        val action =PostFragmentDirections.actionPostFragmentToSignInFragment2()
        viewModel.authenticationState.observe(viewLifecycleOwner, Observer { authenticationState ->
            when(authenticationState){
                PostViewModel.AuthenticationState.UNAUTHENTICATED->{
                    NavHostFragment.findNavController(this).navigate(action)
                }
            }
        })
    }
}