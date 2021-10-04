package com.example.android_sqlite

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.android_sqlite.Films.FilmsType
import com.example.android_sqlite.Films.FindFilmType
import com.example.android_sqlite.databinding.ClientsTableBinding
import com.example.android_sqlite.databinding.FoundFilmsTableBinding
import com.example.android_sqlite.databinding.FragmentClientsTableBinding
import com.example.android_sqlite.databinding.FragmentFilmsTableBinding


class ClientsTable : Fragment() {
    private lateinit var binding: FragmentClientsTableBinding
    private val adapter = ClientsTableAdapter()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentClientsTableBinding.inflate(inflater)
        return binding.root
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val botNav = (activity as MainActivity).findViewById<View>(R.id.botNav)
        botNav.visibility = View.GONE

        binding.RecyclerView.layoutManager = LinearLayoutManager(activity as MainActivity)
        binding.RecyclerView.adapter = adapter

        val data_list = ArrayList<ClientType>(1)
        data_list.add(ClientType())
        data_list.addAll((activity as MainActivity).data_base_manager.readClientsFromTable())

        //data_list.addAll(1,(activity as MainActivity).data_base_manager.readDB())
        adapter.addAll(data_list)
    }

    inner class ClientsTableAdapter : RecyclerView.Adapter<ClientsTableAdapter.ClientsTableHolder>() {

        val table_content: ArrayList<ClientType> = arrayListOf()

        inner class ClientsTableHolder(item: View) : RecyclerView.ViewHolder(item) {
            val binding = ClientsTableBinding.bind(item)

            fun bind(table_content: ClientType) = with(binding) {
                ID.text = table_content.ID.toString()
                FirstName.text = table_content.first_name
                SecondName.text = table_content.second_name
                Email.text = table_content.email
                PhoneNumber.text = table_content.phone_number.toString()
            }
        }


        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ClientsTableHolder {
            val view =
                LayoutInflater.from(parent.context).inflate(R.layout.clients_table, parent, false)
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

        fun addAll(data: List<ClientType>) {
            this.table_content.clear()
            this.table_content.addAll(data)
            notifyDataSetChanged()
        }
    }

}