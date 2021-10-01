package com.example.android_sqlite

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.TextView
import com.example.android_sqlite.data_base.DataBaseManager
import com.example.android_sqlite.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    lateinit var binding : ActivityMainBinding
    val data_base_manager = DataBaseManager(this)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
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
}