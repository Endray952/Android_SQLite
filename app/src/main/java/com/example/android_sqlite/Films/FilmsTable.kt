package com.example.android_sqlite.Films

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.android_sqlite.MainActivity
import com.example.android_sqlite.R
import com.example.android_sqlite.databinding.FilmsTableBinding
import com.example.android_sqlite.databinding.FragmentFilmsTableBinding


class FilmsTable : Fragment() {
    private lateinit var binding: FragmentFilmsTableBinding
    private val adapter = FilmsTableAdapter()

    // private val data_base_manager = DataBaseManager(activity as MainActivity)
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentFilmsTableBinding.inflate(inflater)
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val botNav = (activity as MainActivity).findViewById<View>(R.id.botNav)
        botNav.visibility = View.GONE
        binding.RecyclerView.layoutManager = LinearLayoutManager(activity as MainActivity)
        binding.RecyclerView.adapter = adapter

        val data_list = ArrayList<FilmsType>(1)
        data_list.add(FilmsType())
        data_list.addAll((activity as MainActivity).data_base_manager.readFilmsFromTable())
        //data_list.addAll(1,(activity as MainActivity).data_base_manager.readDB())
        adapter.addAll(data_list)
    }


    inner class FilmsTableAdapter : RecyclerView.Adapter<FilmsTableAdapter.FilmsTableHolder>() {

        val table_content: ArrayList<FilmsType> = arrayListOf()

        inner class FilmsTableHolder(item: View) : RecyclerView.ViewHolder(item) {
            val binding = FilmsTableBinding.bind(item)

            fun bind(table_content: FilmsType) = with(binding) {
                ID.text = table_content.ID.toString()
                Title.text = table_content.title
                Remain.text = table_content.remain.toString()
                Category.text = table_content.category_ID.toString()
                Price.text = table_content.price.toString()
            }
        }


        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FilmsTableHolder {
            val view =
                LayoutInflater.from(parent.context).inflate(R.layout.films_table, parent, false)
            return FilmsTableHolder(view)
        }

        override fun onBindViewHolder(holder: FilmsTableHolder, position: Int) {
            if (position != 0) {
                holder.bind(table_content[position])
            }
        }

        override fun getItemCount(): Int {
            return table_content.size
        }

        fun addAll(data: List<FilmsType>) {
            this.table_content.clear()
            this.table_content.addAll(data)
            notifyDataSetChanged()
        }
    }
}