package com.example.android_sqlite.Finances

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.android_sqlite.Customers.CustomerType
import com.example.android_sqlite.MainActivity
import com.example.android_sqlite.R
import com.example.android_sqlite.databinding.ChequeItemBinding
import com.example.android_sqlite.databinding.FinancesItemBinding
import com.example.android_sqlite.databinding.FragmentOrdersListBinding

class OrdersList : Fragment(), AdapterView.OnItemSelectedListener  {
    private lateinit var binding: FragmentOrdersListBinding
    private val adapter = OrdersAdapter()
    private var selected_customer = 0
    private var selected_cheque_type = 0
    private var customers_list = arrayListOf<CustomerType>()
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        binding = FragmentOrdersListBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val orders_oprions = arrayListOf<String>("Все", "Возвраты", "Взятия в аренду")
        val orders_adapter = ArrayAdapter(activity as MainActivity, android.R.layout.simple_expandable_list_item_1, orders_oprions)
        binding.ChequeType.adapter = orders_adapter
        binding.ChequeType.onItemSelectedListener = this

        binding.OrdersRV.layoutManager = LinearLayoutManager(activity as MainActivity)
        binding.OrdersRV.adapter = adapter

        customers_list = (activity as MainActivity).data_base_manager. readClientsFromTable()
        val spinner_customer_list = arrayListOf<String>()
        for(customer in customers_list){
            spinner_customer_list.add(customer.first_name + " " + customer.second_name + "\n" + customer.email + "\n" + customer.phone_number )
        }
        binding.Customer.adapter = ArrayAdapter<String>(activity as MainActivity, android.R.layout.simple_expandable_list_item_1,spinner_customer_list)
        binding.Customer.onItemSelectedListener = this

        //recyclerViewUpdate()
    }
    private fun recyclerViewUpdate(){
        if(customers_list.isNotEmpty()) {
            val cheques = (activity as MainActivity).data_base_manager.getChequesOfCustomer(customers_list[selected_customer].ID)
            adapter.addAll(cheques)
        }
    }
    override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
        when(parent?.id){
            R.id.ChequeType -> selected_cheque_type = parent.selectedItemPosition
            R.id.Customer -> selected_customer = parent.selectedItemPosition
        }
        recyclerViewUpdate()
    }

    override fun onNothingSelected(parent: AdapterView<*>?) {
        TODO("Not yet implemented")
    }
    inner class OrdersAdapter : RecyclerView.Adapter<OrdersAdapter.OrdersHolder>() {

        val table_content: ArrayList<ChequeType> = arrayListOf()

        inner class OrdersHolder(item: View) : RecyclerView.ViewHolder(item) {
            val binding = ChequeItemBinding.bind(item)

            fun bind(data: ChequeType) = with(binding) {
                if(data.is_getting){
                    CloseDateTextRV.visibility = View.GONE
                    CloseDateRV.visibility = View.GONE
                    ChequeTypeRV.text = "Взятие в аренду"
                }
                else{
                    ChequeTypeRV.text = "Возврат"
                    CloseDateRV.text = data.close_date
                }
                FilmTitleRV.text = data.film
                StartOfRentRV.text = data.start_of_rent
                EndOfRentRV.text = data.end_of_rent
                TariffRV.text = data.tariff.toString()
                CassettePriceRV.text = data.cassette_price.toString()
                CustomerPayment.text = data.customer_payment.toString()
                ShopPayment.text = data.shop_payment.toString()
            }
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): OrdersHolder {
            val view =
                LayoutInflater.from(parent.context).inflate(R.layout.cheque_item, parent, false)
            return OrdersHolder(view)
        }

        override fun onBindViewHolder(holder: OrdersHolder, position: Int) {
            holder.bind(table_content[position])

        }

        override fun getItemCount(): Int {
            return table_content.size
        }

        fun addAll(data: List<ChequeType>) {
            this.table_content.clear()
            this.table_content.addAll(data)
            notifyDataSetChanged()
        }
    }

}