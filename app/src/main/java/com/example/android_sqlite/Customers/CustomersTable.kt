package com.example.android_sqlite.Customers

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.android_sqlite.MainActivity
import com.example.android_sqlite.R
import com.example.android_sqlite.databinding.*


class CustomersTable : Fragment() {
    private lateinit var binding: FragmentCustomersTableBinding
    private val adapter = ClientsTableAdapter()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentCustomersTableBinding.inflate(inflater)
        return binding.root
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val botNav = (activity as MainActivity).findViewById<View>(R.id.botNav)
        botNav.visibility = View.GONE

        binding.RecyclerView.layoutManager = LinearLayoutManager(activity as MainActivity)
        binding.RecyclerView.adapter = adapter

        val data_list = ArrayList<CustomerType>(1)
        data_list.add(CustomerType())
        data_list.addAll((activity as MainActivity).data_base_manager.readClientsFromTable())

        //data_list.addAll(1,(activity as MainActivity).data_base_manager.readDB())
        adapter.addAll(data_list)
    }

    inner class ClientsTableAdapter : RecyclerView.Adapter<ClientsTableAdapter.ClientsTableHolder>() {

        val table_content: ArrayList<CustomerType> = arrayListOf()

        inner class ClientsTableHolder(item: View) : RecyclerView.ViewHolder(item) {
            val binding = CustomersTableBinding.bind(item)

            fun bind(table_content: CustomerType) = with(binding) {
                ID.text = table_content.ID.toString()
                FirstName.text = table_content.first_name
                SecondName.text = table_content.second_name
                Email.text = table_content.email
                PhoneNumber.text = table_content.phone_number.toString()
            }
        }


        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ClientsTableHolder {
            val view =
                LayoutInflater.from(parent.context).inflate(R.layout.customers_table, parent, false)
            return ClientsTableHolder(view)
        }

        override fun onBindViewHolder(holder: ClientsTableHolder, position: Int) {
            if (position != 0) {
                holder.bind(table_content[position])
            }
        }

        override fun getItemCount(): Int {
            return table_content.size
        }

        fun addAll(data: List<CustomerType>) {
            this.table_content.clear()
            this.table_content.addAll(data)
            notifyDataSetChanged()
        }
    }

}