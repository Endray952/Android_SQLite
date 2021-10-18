package com.example.android_sqlite

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.android_sqlite.databinding.FragmentFinancesOptionsBinding

import androidx.recyclerview.widget.RecyclerView
import com.example.android_sqlite.Films.FindFilmType
import com.example.android_sqlite.Finances.FinancesType
import com.example.android_sqlite.databinding.FinancesItemBinding
import com.example.android_sqlite.databinding.FoundFilmsTableBinding
import java.time.Year


class FinancesOptions : Fragment(), AdapterView.OnItemSelectedListener {
    private lateinit var binding: FragmentFinancesOptionsBinding
    private val adapter = FinancesAdapter()
    private var chosenYear = 2021
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentFinancesOptionsBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.FinancesRV.layoutManager = LinearLayoutManager(activity as MainActivity)
        binding.FinancesRV.adapter = adapter

        val yearsSpinner = arrayListOf<String>()
        if((activity as MainActivity).current_date.year >= 2021) {
            for (i in 2021..(activity as MainActivity).current_date.year) {
                yearsSpinner.add(i.toString())
            }
        }
        binding.YearPicker.adapter = ArrayAdapter<String>(activity as MainActivity, android.R.layout.simple_expandable_list_item_1,yearsSpinner)
        binding.YearPicker.onItemSelectedListener = this
        /*val report = (activity as MainActivity).data_base_manager.createFinancesReport(chosenYear)
        adapter.addAll(report)*/
    }
    inner class FinancesAdapter : RecyclerView.Adapter<FinancesAdapter.FinancesHolder>() {

        val table_content: ArrayList<FinancesType> = arrayListOf()

        inner class FinancesHolder(item: View) : RecyclerView.ViewHolder(item) {
            val binding = FinancesItemBinding.bind(item)

            fun bind(table_content: FinancesType) = with(binding) {
                FinancesMonth.text = table_content.month
                Income.text = table_content.income.toString()
            }
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FinancesHolder {
            val view =
                LayoutInflater.from(parent.context).inflate(R.layout.finances_item, parent, false)
            return FinancesHolder(view)
        }

        override fun onBindViewHolder(holder: FinancesHolder, position: Int) {
                holder.bind(table_content[position])

        }

        override fun getItemCount(): Int {
            return table_content.size
        }

        fun addAll(data: List<FinancesType>) {
            this.table_content.clear()
            this.table_content.addAll(data)
            notifyDataSetChanged()
        }
    }

    override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
        chosenYear = 2021+position
        val report = (activity as MainActivity).data_base_manager.createFinancesReport(chosenYear)
        adapter.addAll(report)
    }

    override fun onNothingSelected(parent: AdapterView<*>?) {
        TODO("Not yet implemented")
    }
}