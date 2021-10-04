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
import com.example.android_sqlite.databinding.*


class CustomersOptions : Fragment() {
    private lateinit var binding: FragmentCustomersOptionsBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentCustomersOptionsBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        with(binding){
            UpdateDate.setOnClickListener {
                (activity as MainActivity).current_date.day = Day.text.toString().toInt()
                (activity as MainActivity).current_date.month = Month.text.toString().toInt()
                (activity as MainActivity).current_date.year = Year.text.toString().toInt()
            }
            AddClient.setOnClickListener {
                addClientDialog((activity as MainActivity), layoutInflater)
            }
            ClientsTable.setOnClickListener {
                findNavController().navigate(R.id.action_clientsOptions_to_clientsTable)
            }
            AddOrder.setOnClickListener {
                addOrderDialog((activity as MainActivity), layoutInflater)
            }
            OrdersTable.setOnClickListener {
                findNavController().navigate(R.id.action_clientsOptions_to_ordersTable)
            }
        }
    }

    private fun addClientDialog(context: Context, inflater: LayoutInflater){
        val mBuilder = AlertDialog.Builder(context)
        val mView = inflater.inflate(R.layout.add_client, null)
        val binding = AddClientBinding.bind(mView)
        mBuilder.setView(mView)
        val dialog: AlertDialog = mBuilder.create()
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog.show()
        binding.Cancel.setOnClickListener {
            dialog.dismiss()
        }
        binding.Add.setOnClickListener {
            (activity as MainActivity).data_base_manager.insertClientToDB(binding.FisrstName.text.toString(), binding.SecondName.text.toString(), binding.Email.text.toString(), binding.PhoneNumber.text.toString().trim().toInt())
        }
    }
    private fun addOrderDialog(context: Context, inflater: LayoutInflater){
        val mBuilder = AlertDialog.Builder(context)
        val mView = inflater.inflate(R.layout.add_order, null)
        val binding = AddOrderBinding.bind(mView)
        mBuilder.setView(mView)
        val dialog: AlertDialog = mBuilder.create()
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog.show()
        binding.StartOfRent.setText("${(activity as MainActivity).current_date.day}${(activity as MainActivity).current_date.month}${(activity as MainActivity).current_date.year}")
        binding.Cancel.setOnClickListener {
            dialog.dismiss()
        }
        binding.Add.setOnClickListener {
            (activity as MainActivity).data_base_manager.insertOrderToDB(binding.FilmID.text.toString().toInt(), binding.CustomerID.text.toString().toInt(),
                binding.StartOfRent.text.toString().toInt(), binding.EndOfRent.text.toString().toInt())
        }
    }
}