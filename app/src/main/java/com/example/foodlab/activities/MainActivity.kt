package com.example.foodlab.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation
import androidx.navigation.ui.NavigationUI
import com.example.foodlab.DB.MealDatabase
import com.example.foodlab.R
import com.example.foodlab.databinding.ActivityMainBinding
import com.example.foodlab.databinding.FragmentHomeBinding
import com.example.foodlab.viewmodel.HomeViewModel
import com.example.foodlab.viewmodel.HomeViewModelFactory
import com.google.android.material.bottomnavigation.BottomNavigationView

class MainActivity : AppCompatActivity() {
private lateinit var binding: ActivityMainBinding

    val mainViewModel: HomeViewModel by lazy {
        val mealDatabase = MealDatabase.getInstance(this)
        val homeViewModelFactory = HomeViewModelFactory(mealDatabase)
        ViewModelProvider(this,homeViewModelFactory)[HomeViewModel::class.java]
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val bottomNavigation = binding.btmNav
        val navController = Navigation.findNavController(this, R.id.host_fragment)

        NavigationUI.setupWithNavController(bottomNavigation,navController)
    }
}