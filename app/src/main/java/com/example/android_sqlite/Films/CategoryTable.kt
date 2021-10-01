package com.example.android_sqlite.Films

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.android_sqlite.MainActivity
import com.example.android_sqlite.R
import com.example.android_sqlite.data_base.CategoryType

import com.example.android_sqlite.databinding.FragmentCategoryTableBinding
import com.example.android_sqlite.databinding.CategoriesTableBinding


class CategoryTable : Fragment() {
    private lateinit var binding: FragmentCategoryTableBinding
    private val adapter = CategoryTableAdapter()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentCategoryTableBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val botNav = (activity as MainActivity).findViewById<View>(R.id.botNav)
        botNav.visibility = View.GONE
        binding.RecyclerView.layoutManager = LinearLayoutManager(activity as MainActivity)
        binding.RecyclerView.adapter = adapter

        val data_list = ArrayList<CategoryType>(1)
        data_list.add(CategoryType())
        data_list.addAll((activity as MainActivity).data_base_manager.readCategoriesFromTable())
        adapter.addAll(data_list)
    }


    inner class CategoryTableAdapter : RecyclerView.Adapter<CategoryTableAdapter.CategoryTableHolder>() {

        val table_content: ArrayList<CategoryType> = arrayListOf()

        inner class CategoryTableHolder(item: View) : RecyclerView.ViewHolder(item) {
            val binding = CategoriesTableBinding.bind(item)

            fun bind(table_content: CategoryType) = with(binding) {
                ID.text = table_content.ID.toString()
                CategoryName.text = table_content.title
                Tariff.text = table_content.tariff.toString()
            }
        }


        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CategoryTableHolder {
            val view =
                LayoutInflater.from(parent.context).inflate(R.layout.categories_table, parent, false)
            return CategoryTableHolder(view)
        }

        override fun onBindViewHolder(holder: CategoryTableHolder, position: Int) {
            if (position != 0) {
                holder.bind(table_content[position])
            }
        }

        override fun getItemCount(): Int {
            return table_content.size
        }

        fun addAll(data: List<CategoryType>) {
            this.table_content.clear()
            this.table_content.addAll(data)
            notifyDataSetChanged()
        }
    }
}
