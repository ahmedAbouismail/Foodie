package de.abou.foodie.screens.mainActivity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import androidx.constraintlayout.widget.Placeholder
import androidx.databinding.DataBindingUtil
import androidx.drawerlayout.widget.DrawerLayout
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.NavigationUI
import com.firebase.ui.auth.AuthUI
import com.google.android.material.navigation.NavigationView
import de.abou.foodie.R
import de.abou.foodie.databinding.ActivityMainBinding
import de.abou.foodie.screens.title.MyPostsFragment

class MainActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {
    private lateinit var drawerLayout: DrawerLayout
    private lateinit var navController: NavController
    private lateinit var viewModel: MainActivityViewModel


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        viewModel = ViewModelProvider(this).get(MainActivityViewModel::class.java)
        val binding = DataBindingUtil.setContentView<ActivityMainBinding>(this, R.layout.activity_main)

        // Create Drawer Button
        drawerLayout = binding.drawerLayout
        //Add navView To MainActivity
        navController = this.findNavController(R.id.myNavHostFragment)


        //Add Drawer to Action Bar
        NavigationUI.setupActionBarWithNavController(this, navController, drawerLayout)
        NavigationUI.setupWithNavController(binding.navView, navController)

        //To hide the Bar
        supportActionBar?.hide()

        //Authentication Check
        observeAuthenticationState()

        binding.navView.setNavigationItemSelectedListener(this)
    }


    override fun onSupportNavigateUp(): Boolean {
        val navController = this.findNavController(R.id.myNavHostFragment)
        return NavigationUI.navigateUp(navController, drawerLayout)
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.signOutFragment -> {
                AuthUI.getInstance().signOut(this)

            }
            R.id.myProfileFragment ->{

            }
            R.id.my_posts->{
//                NavHostFragment.findNavController(this.fra).navigate(R.id.my_post_list)
//                this.findNavController(this.taskId).navigate(R.id.my_post_list)
            }
        }
        return true
    }

    private fun observeAuthenticationState() {
        viewModel.authenticationState.observe(this, Observer { authenticationState ->
            when(authenticationState){
                MainActivityViewModel.AuthenticationState.AUTHENTICATED->{
                    drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED)
                }else->{
                drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED)
            }
            }
        })
    }
}