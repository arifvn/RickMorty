package com.squareit.rickmorty.ui

import android.os.Bundle
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.get
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.NavigationUI
import androidx.navigation.ui.setupWithNavController
import com.squareit.rickmorty.R
import com.squareit.rickmorty.databinding.ActivityMainBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity(R.layout.activity_main) {
    private val binding: ActivityMainBinding by lazy {
        ActivityMainBinding.bind(findViewById<ViewGroup>(android.R.id.content)[0])
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setupNavController()
    }

    private fun setupNavController() {
        setSupportActionBar(binding.toolbar)

        val appBarConfiguration = AppBarConfiguration.Builder(
            R.id.favoriteFragment,
            R.id.homeFragment
        ).build()

        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.navHostFragment) as NavHostFragment
        val navController = navHostFragment.findNavController()

        NavigationUI.setupActionBarWithNavController(
            this,
            navController,
            appBarConfiguration
        )

        binding.navBottom.setupWithNavController(navController)
    }

    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.navHostFragment)
        return navController.navigateUp()
    }
}