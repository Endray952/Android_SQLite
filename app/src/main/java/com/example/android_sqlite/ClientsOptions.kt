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
import com.example.android_sqlite.databinding.AddCategoryBinding
import com.example.android_sqlite.databinding.FragmentClientsOptionsBinding
import com.example.android_sqlite.databinding.FragmentFilmsOptionsBinding


class ClientsOptions : Fragment() {
    private lateinit var binding: FragmentClientsOptionsBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentClientsOptionsBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        with(binding){
            AddClient.setOnClickListener {

            }
        }
    }

    private fun addClientDialog(context: Context, inflater: LayoutInflater){
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