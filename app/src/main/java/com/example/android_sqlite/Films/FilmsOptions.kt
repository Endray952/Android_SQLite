package com.example.android_sqlite.Films

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
import com.example.android_sqlite.MainActivity
import com.example.android_sqlite.R
import com.example.android_sqlite.databinding.AddCategoryBinding
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
                addFilmDialog(activity as MainActivity, layoutInflater)
            }
            FilmsTable.setOnClickListener{
                findNavController().navigate(R.id.action_filmsOptions_to_filmsTable)
            }
            AddCategory.setOnClickListener {
                addCategoryDialog(activity as MainActivity, layoutInflater)
            }
            CategoryTable.setOnClickListener {
                findNavController().navigate(R.id.action_filmsOptions_to_categoryTable)
            }
        }
    }
    private fun addFilmDialog(context: Context, inflater: LayoutInflater){
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
            (activity as MainActivity).data_base_manager.insertFilmToDB(title = binding.Title.text.toString(),
                remain = binding.Remain.text.toString().toInt(), category_ID = binding.Category.text.toString().toInt(),
                cassette_price = binding.Price.text.toString().toDouble())
        }
    }
    private fun addCategoryDialog(context: Context, inflater: LayoutInflater){
        val mBuilder = AlertDialog.Builder(context)
        val mView = inflater.inflate(R.layout.add_category, null)
        val binding = AddCategoryBinding.bind(mView)
        mBuilder.setView(mView)
        val dialog: AlertDialog = mBuilder.create()
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog.show()
        binding.CancelBtn.setOnClickListener {
            dialog.dismiss()
        }
        binding.AddBtn.setOnClickListener {
            (activity as MainActivity).data_base_manager.insertCategoryToDB(binding.Category.text.toString(), binding.Tariff.text.toString().toDouble())
        }
    }
}