package com.example.android_sqlite

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.navigation.ui.setupWithNavController
import com.example.android_sqlite.data_base.DataBaseManager
import com.example.android_sqlite.databinding.ActivityMainBinding


import androidx.navigation.NavController

import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController


class MainActivity : AppCompatActivity() {
    lateinit var binding : ActivityMainBinding
    private lateinit var navController: NavController
    val data_base_manager = DataBaseManager(this)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val navHostFragment = supportFragmentManager.findFragmentById(R.id.host) as NavHostFragment
        navController = navHostFragment.findNavController()
        binding.botNav.setupWithNavController(navController)
        //Thread {
            data_base_manager.openDb()
        //}.start()

        //setUpNavigation()
    }

    /*override fun onResume() {
        super.onResume()
        data_base_manager.openDb()
        val data_list = data_base_manager.readDB()
        for(item in data_list){
            binding.Test.append(item)
            binding.Test.append("\n")
        }
    }
    fun onClickSave(view: View){
        binding.Test.text = ""
        data_base_manager.openDb()
        data_base_manager.insertToDB(binding.Title.text.toString(), binding.Content.text.toString().toInt())
        val data_list = data_base_manager.readDB()
        for(item in data_list){
            binding.Test.append(item)
            binding.Test.append("\n")
        }
    }*/
    private fun setUpNavigation(){
        //val navController = findNavController(R.id.)
        //binding.botNav.setupWithNavController(navController)

    }

    override fun onStop() {
        super.onStop()
        data_base_manager.closeDb()
    }
}