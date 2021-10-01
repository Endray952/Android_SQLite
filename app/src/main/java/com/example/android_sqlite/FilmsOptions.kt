package com.example.android_sqlite

import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.navigation.fragment.findNavController
import com.example.android_sqlite.databinding.ActivityMainBinding
import com.example.android_sqlite.databinding.AddFilmBinding
import com.example.android_sqlite.databinding.FragmentFilmsOptionsBinding


class FilmsOptions : Fragment() {
    private lateinit var binding: FragmentFilmsOptionsBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        binding = FragmentFilmsOptionsBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val botNav = (activity as MainActivity).findViewById<View>(R.id.botNav)
        botNav.visibility = View.VISIBLE
        with(binding){
            AddFilm.setOnClickListener{
                alertDialog(activity as MainActivity, layoutInflater)
            }
            FilmsTable.setOnClickListener{
                findNavController().navigate(R.id.action_filmsOptions_to_filmsTable)
            }
        }
    }
    private fun alertDialog(context: Context, inflater: LayoutInflater){
        val mBuilder = AlertDialog.Builder(context)
        val mView = inflater.inflate(R.layout.add_film, null)
        val binding = AddFilmBinding.bind(mView)
        mBuilder.setView(mView)
        val dialog: AlertDialog = mBuilder.create()
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog.show()
        binding.CancelBtn.setOnClickListener {
            dialog.dismiss()
        }
        binding.AddBtn.setOnClickListener {
            (activity as MainActivity).data_base_manager.insertToDB(binding.Title.text.toString(), binding.Remain.text.toString().toInt(), binding.Category.text.toString().toInt(), binding.Price.text.toString().toDouble())
        }
    }
}