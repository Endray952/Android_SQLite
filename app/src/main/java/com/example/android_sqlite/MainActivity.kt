package com.example.android_sqlite

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.navigation.ui.setupWithNavController
import com.example.android_sqlite.data_base.DataBaseManager
import com.example.android_sqlite.databinding.ActivityMainBinding


import androidx.navigation.NavController

import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import java.time.Month

class Date(var day: Int, var month: Int, var year: Int)

class MainActivity : AppCompatActivity() {
    lateinit var binding : ActivityMainBinding
    private lateinit var navController: NavController
    val data_base_manager = DataBaseManager(this)
    var current_date = Date(1, 1, 2021)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val navHostFragment = supportFragmentManager.findFragmentById(R.id.host) as NavHostFragment
        navController = navHostFragment.findNavController()
        binding.botNav.setupWithNavController(navController)

        data_base_manager.openDb()

        //setUpNavigation()
    }


    private fun setUpNavigation(){
        //val navController = findNavController(R.id.)
        //binding.botNav.setupWithNavController(navController)

    }

    override fun onStop() {
        super.onStop()
        data_base_manager.closeDb()
    }
}