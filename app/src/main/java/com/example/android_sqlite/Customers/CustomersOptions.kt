package com.example.android_sqlite.Customers

import android.app.DatePickerDialog
import android.content.Context
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
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

import com.example.android_sqlite.DateType
import com.example.android_sqlite.MainActivity
import com.example.android_sqlite.R
import com.example.android_sqlite.databinding.*
import java.util.*


class CustomersOptions : Fragment(), AdapterView.OnItemSelectedListener {
    private lateinit var binding: FragmentCustomersOptionsBinding
    private var selected_customer: Int? = null
    private var selected_film: Int? = null
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
            AddReturn.setOnClickListener {
                addReturnDialog((activity as MainActivity), layoutInflater)
            }
            DebtTable.setOnClickListener {
                findNavController().navigate(R.id.action_clientsOptions_to_debtorTable2)
            }
            OrdersList.setOnClickListener {
                findNavController().navigate(R.id.action_clientsOptions_to_ordersList2)
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
            (activity as MainActivity).data_base_manager.insertClientToDB(binding.FisrstName.text.toString(), binding.SecondName.text.toString(), binding.Email.text.toString(), binding.PhoneNumber.text.toString())
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
        val spinner_customer_list = arrayListOf<String>()
        for(customer in customers_list){
            spinner_customer_list.add(customer.first_name + " " + customer.second_name + "\n" + customer.email + "\n" + customer.phone_number )
        }
        binding.CustomerID.adapter = ArrayAdapter<String>(activity as MainActivity, android.R.layout.simple_expandable_list_item_1,spinner_customer_list)
        binding.CustomerID.onItemSelectedListener = this
        /** Searchable spinner for films */
        val films_list = (activity as MainActivity).data_base_manager. getFilmAndCategory()
        val spinner_films_list = arrayListOf<String>()
        for(film in films_list){
            spinner_films_list.add(film.title + "\n" + film.category + "\n" + film.remain)
        }
        binding.FilmID.adapter = ArrayAdapter<String>(activity as MainActivity, android.R.layout.simple_expandable_list_item_1,spinner_films_list)
        binding.FilmID.onItemSelectedListener = this


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
                date.month - 1,
                date.day).show()
        }
        binding.Add.setOnClickListener {
            /*(activity as MainActivity).data_base_manager.insertOrderToDB(binding.FilmID.text.toString().toInt(), binding.CustomerID.text.toString().toInt(),
                (activity as MainActivity).current_date, date)*/
            if(selected_customer != null && selected_film != null){
                Log.d("MyLog", selected_film.toString())
                (activity as MainActivity).data_base_manager.insertOrderToDB(films_list[selected_film!!].id ,
                    customers_list[selected_customer!!].ID,
                    (activity as MainActivity).current_date, date)
                (activity as MainActivity).data_base_manager.updateFilmsRemain(films_list[selected_film!!].id, increment = -1)
                dialog.dismiss()
                Toast.makeText(activity, "Успешно", Toast.LENGTH_SHORT).show()
            }
            else{
                Toast.makeText(activity, "Ошибка", Toast.LENGTH_SHORT).show()
            }
        }
    }
    private val adapter = ReturnDialogAdapter()
    private val checkbox_list = arrayListOf<CheckBoxType>()
    private fun addReturnDialog(context: Context, inflater: LayoutInflater){
        val mBuilder = AlertDialog.Builder(context)
        val mView = inflater.inflate(R.layout.add_return, null)
        val binding = AddReturnBinding.bind(mView)
        mBuilder.setView(mView)
        val dialog: AlertDialog = mBuilder.create()
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog.show()

        binding.ReturnRV.layoutManager = LinearLayoutManager(activity as MainActivity)
        binding.ReturnRV.adapter = adapter

        /** Searchable spinner for clients */
        val customers_list = (activity as MainActivity).data_base_manager. readClientsFromTable()
        val spinner_customer_list = arrayListOf<String>()
        for(customer in customers_list){
            spinner_customer_list.add(customer.first_name + " " + customer.second_name + "\n" + customer.email + "\n" + customer.phone_number )
        }
        binding.ReturnSpinner.adapter = ArrayAdapter<String>(activity as MainActivity, android.R.layout.simple_expandable_list_item_1,spinner_customer_list)
        binding.ReturnSpinner.onItemSelectedListener = this


        selected_customer = 0
        addReturnUpdate()

        binding.ReturnConfirm.setOnClickListener {
            for(elem in checkbox_list) {
                (activity as MainActivity).data_base_manager.updateDBafterReturn(elem.order_id)
            }
            dialog.dismiss()
        }
        binding.Cancel.setOnClickListener {
            dialog.dismiss()
        }

    }
    private fun addReturnUpdate(){
        val orders_of_customer = (activity as MainActivity).data_base_manager.getOrdersOfCustomer(selected_customer!! + 1)
        adapter.addAll(orders_of_customer)
    }

    override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
        when(parent?.id){
            R.id.FilmID -> selected_film = parent?.selectedItemPosition
            R.id.CustomerID -> selected_customer = parent?.selectedItemPosition
            R.id.ReturnSpinner -> {
                selected_customer = parent?.selectedItemPosition
                checkbox_list.clear()
                Log.d("MyLog", checkbox_list.toString())
                addReturnUpdate()
            }
        }
        //selected_customer = parent?.selectedItemPosition
        // selected_customer = if (selected_customer!= null ) selected_customer!! + 1 else null
    }

    override fun onNothingSelected(parent: AdapterView<*>?) {
        TODO("Not yet implemented")
    }



    inner class ReturnDialogAdapter : RecyclerView.Adapter<ReturnDialogAdapter.ReturnDialogHolder>() {

        val orders_list: ArrayList<CustomerOrderType> = arrayListOf()

        inner class ReturnDialogHolder(item: View) : RecyclerView.ViewHolder(item) {
            val binding = ReturnElemBinding.bind(item)

            fun bind(content: CustomerOrderType) = with(binding) {
                RadioBt.text = "${content.film}\nкатегория: ${content.category}\n${content.start_of_rent} - ${content.end_of_rent}"
                /*if(!checkbox_list.contains(CheckBoxType(RadioBt.isChecked, content.order_id))){
                    RadioBt.isChecked = false
                }*/
                RadioBt.setOnCheckedChangeListener { buttonView, isChecked ->

                    val data = CheckBoxType(!isChecked, content.order_id)
                    if(!checkbox_list.contains(data)){
                        data.isChecked = !data.isChecked
                        checkbox_list.add(data)
                        Log.d("MyLog", checkbox_list.toString())
                    }
                    else{
                        val index = checkbox_list.indexOf(data)
                        checkbox_list.removeAt(index)
                        Log.d("MyLog", checkbox_list.toString() + "lel")
                    }
                }
            }
        }


        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ReturnDialogHolder {
            val view =
                LayoutInflater.from(parent.context).inflate(R.layout.return_elem, parent, false)
            return ReturnDialogHolder(view)
        }

        override fun onBindViewHolder(holder: ReturnDialogHolder, position: Int) {
                holder.bind(orders_list[position])

        }

        override fun getItemCount(): Int {
            return orders_list.size
        }

        fun addAll(data: List<CustomerOrderType>) {
            this.orders_list.clear()
            this.orders_list.addAll(data)
            notifyDataSetChanged()
        }
    }
}
