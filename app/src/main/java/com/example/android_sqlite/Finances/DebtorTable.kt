package com.example.android_sqlite.Finances

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.android_sqlite.Customers.DebtorType
import com.example.android_sqlite.Customers.OrderType
import com.example.android_sqlite.MainActivity
import com.example.android_sqlite.R
import com.example.android_sqlite.databinding.DebtorItemBinding
import com.example.android_sqlite.databinding.FragmentDebtorTableBinding
import com.example.android_sqlite.databinding.OrdersTableBinding


class DebtorTable : Fragment() {
  private lateinit var binding: FragmentDebtorTableBinding
  private val adapter = DebtorsTable()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentDebtorTableBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val botNav = (activity as MainActivity).findViewById<View>(R.id.botNav)
        botNav.visibility = View.GONE

        binding.DebtRV.layoutManager = LinearLayoutManager(activity as MainActivity)
        binding.DebtRV.adapter = adapter

        val data_list = ArrayList<DebtorType>(1)
        data_list.add(DebtorType("","","","","","","", 0.0, 0))
        data_list.addAll((activity as MainActivity).data_base_manager.getDebtors())

        adapter.addAll(data_list)
    }

    inner class DebtorsTable : RecyclerView.Adapter<DebtorsTable.DebtorsTableHolder>() {

        val table_content: ArrayList<DebtorType> = arrayListOf()

        inner class DebtorsTableHolder(item: View) : RecyclerView.ViewHolder(item) {
            val binding = DebtorItemBinding.bind(item)

            fun bind(table_content: DebtorType) = with(binding) {
                FirstName.text = table_content.first_name
                SecondName.text = table_content.second_name
                Email.text = table_content.email
                PhoneNumber.text = table_content.phone_number
                DebtFilm.text = table_content.film
                DebtStartOfRent.text = table_content.start_of_rent
                DebtEndOfRent.text = table_content.end_of_rent
                DebtOverdue.text = table_content.overdue.toString()
                DebtTotalDebt.text = table_content.total_debt.toString()
            }
        }


        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DebtorsTableHolder {
            val view =
                LayoutInflater.from(parent.context).inflate(R.layout.debtor_item, parent, false)
            return DebtorsTableHolder(view)
        }

        override fun onBindViewHolder(holder: DebtorsTableHolder, position: Int) {
            if (position != 0) {
                holder.bind(table_content[position])
            }
        }

        override fun getItemCount(): Int {
            return table_content.size
        }

        fun addAll(data: List<DebtorType>) {
            this.table_content.clear()
            this.table_content.addAll(data)
            notifyDataSetChanged()
        }
    }


}