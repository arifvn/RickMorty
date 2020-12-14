package com.squareit.rickmorty.ui

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.get
import androidx.navigation.Navigation
import androidx.navigation.findNavController
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
        val navController = findNavController(R.id.navHostFragment)
        binding.navBottom.setupWithNavController(navController)
    }
}