package com.app.bookstoreapp

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences

import androidx.navigation.findNavController
import androidx.navigation.ui.setupWithNavController
import com.app.bookstoreapp.databinding.ActivityMainBinding



class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
        initView()
    }
    private fun initView() {

        val navController = this.findNavController(R.id.nav_host_fragment)
        // add bottom nav
        binding.apply {
            bottomNav.setupWithNavController(navController)
        }
        // hide bottom nav when fragment not need
        navController.addOnDestinationChangedListener { _, des, _ ->
            if (des.id == R.id.billDetailScreenFragment ||des.id == R.id.profileScreenFragment || des.id == R.id.loginScreenFragment || des.id == R.id.signinScreenFragment || des.id == R.id.bookDetailScreenFragment2 || des.id == R.id.cartScreenFragment2) {
                binding.bottomNav.visibility = View.GONE
            } else {
                binding.bottomNav.visibility = View.VISIBLE
            }
        }
    }
}
