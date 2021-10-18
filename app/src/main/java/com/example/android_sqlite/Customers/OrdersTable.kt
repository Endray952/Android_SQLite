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
import com.example.android_sqlite.databinding.FragmentOrdersTableBinding
import com.example.android_sqlite.databinding.OrdersTableBinding


class OrdersTable : Fragment() {
    private lateinit var binding: FragmentOrdersTableBinding
    private val adapter = OrdersTableAdapter()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentOrdersTableBinding.inflate(inflater)
        return binding.root
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val botNav = (activity as MainActivity).findViewById<View>(R.id.botNav)
        botNav.visibility = View.GONE

        binding.RecyclerView.layoutManager = LinearLayoutManager(activity as MainActivity)
        binding.RecyclerView.adapter = adapter

        val data_list = ArrayList<OrderType>(1)
        data_list.add(OrderType())
        data_list.addAll((activity as MainActivity).data_base_manager.readOrdersFromTable())

        adapter.addAll(data_list)
    }

    inner class OrdersTableAdapter : RecyclerView.Adapter<OrdersTableAdapter.OrdersTableHolder>() {

        val table_content: ArrayList<OrderType> = arrayListOf()

        inner class OrdersTableHolder(item: View) : RecyclerView.ViewHolder(item) {
            val binding = OrdersTableBinding.bind(item)

            fun bind(table_content: OrderType) = with(binding) {
                ID.text = table_content.ID.toString()
                FilmID.text = table_content.film_ID.toString()
                CustomerID.text = table_content.customer_ID.toString()
                StartOfRent.text = table_content.start_of_rent.toString()
                EndOfRent.text = table_content.end_of_rent.toString()
                CloseDate.text = if (table_content.close_date != "") table_content.close_date else "null"
                FlagNotReturned.text = if(table_content.flag_not_returned == 1) "true" else "false"
            }
        }


        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): OrdersTableHolder {
            val view =
                LayoutInflater.from(parent.context).inflate(R.layout.orders_table, parent, false)
            return OrdersTableHolder(view)
        }

        override fun onBindViewHolder(holder: OrdersTableHolder, position: Int) {
            if (position != 0) {
                holder.bind(table_content[position])
            }
        }

        override fun getItemCount(): Int {
            return table_content.size
        }

        fun addAll(data: List<OrderType>) {
            this.table_content.clear()
            this.table_content.addAll(data)
            notifyDataSetChanged()
        }
    }
}