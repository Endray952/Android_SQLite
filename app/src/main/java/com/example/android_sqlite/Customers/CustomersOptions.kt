package com.example.android_sqlite.Customers

import android.app.DatePickerDialog
import android.content.Context
import android.content.res.Resources
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.appcompat.app.AlertDialog
import androidx.navigation.fragment.findNavController
import com.example.android_sqlite.DateType
import com.example.android_sqlite.MainActivity
import com.example.android_sqlite.R
import com.example.android_sqlite.databinding.*
import java.util.*


class CustomersOptions : Fragment(), AdapterView.OnItemSelectedListener {
    private lateinit var binding: FragmentCustomersOptionsBinding
    private var selected_customer: Int? = null
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
        val botNav = (activity as MainActivity).findViewById<View>(R.id.botNav)
        botNav.visibility = View.VISIBLE
        with(binding){
            Day.setText((activity as MainActivity).current_date.day.toString())
            Month.setText((activity as MainActivity).current_date.month.toString())
            Year.setText((activity as MainActivity).current_date.year.toString())
            UpdateDate.setOnClickListener {
                (activity as MainActivity).current_date.day = Day.text.toString().toInt()
                (activity as MainActivity).current_date.month = Month.text.toString().toInt()
                (activity as MainActivity).current_date.year = Year.text.toString().toInt()
                (activity as MainActivity).data_base_manager.setDate(Day.text.toString().toInt(),Month.text.toString().toInt(), Year.text.toString().toInt())
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
        val date = DateType((activity as MainActivity).current_date.day, (activity as MainActivity).current_date.month,(activity as MainActivity).current_date.year)
        binding.StartOfRent.text = "${date.day}/${date.month}/${date.year}"
        binding.EndOfRent.text = "${date.day}/${date.month}/${date.year}"

        /** Searchable spinner for clients */
        val customers_list = (activity as MainActivity).data_base_manager. readClientsFromTable()
        val spinner_list = arrayListOf<String>()
        for(customer in customers_list){
            spinner_list.add(customer.first_name + " " + customer.second_name + "\n" + customer.email + "\n" + customer.phone_number )
        }
        binding.CustomerID.adapter = ArrayAdapter<String>(activity as MainActivity, android.R.layout.simple_expandable_list_item_1,spinner_list)
        binding.CustomerID.onItemSelectedListener = this
        //binding.CustomerID.setTitle("Найти клиента")

        binding.Cancel.setOnClickListener {
            dialog.dismiss()
        }
        binding.EndOfRentButton.setOnClickListener{

            val dateSetListener = DatePickerDialog.OnDateSetListener { view, year, month, day ->
               date.year = year
                date.month = month + 1
                date.day = day
                binding.EndOfRent.text = "${date.day}/${date.month}/${date.year}"
            }
            DatePickerDialog(activity as MainActivity, dateSetListener,
                date.year,
                date.month,
                date.day).show()
        }
        binding.Add.setOnClickListener {
            /*(activity as MainActivity).data_base_manager.insertOrderToDB(binding.FilmID.text.toString().toInt(), binding.CustomerID.text.toString().toInt(),
                (activity as MainActivity).current_date, date)*/
            if(selected_customer != null){
                    Log.d("MyLog", selected_customer.toString())
            (activity as MainActivity).data_base_manager.insertOrderToDB(binding.FilmID.text.toString().toInt(), customers_list[selected_customer!!].ID,
                (activity as MainActivity).current_date, date)
            }
        }
    }

    override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
        selected_customer = parent?.selectedItemPosition
        // selected_customer = if (selected_customer!= null ) selected_customer!! + 1 else null
    }

    override fun onNothingSelected(parent: AdapterView<*>?) {
        TODO("Not yet implemented")
    }
}