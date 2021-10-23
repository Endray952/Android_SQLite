package com.example.android_sqlite.Films

import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.android_sqlite.MainActivity
import com.example.android_sqlite.R
import android.content.Context.INPUT_METHOD_SERVICE
import android.view.inputmethod.InputMethodManager
import android.widget.*
import com.example.android_sqlite.databinding.*


class FilmsOptions : Fragment(), AdapterView.OnItemSelectedListener {
    private lateinit var binding: FragmentFilmsOptionsBinding
    private val adapter = FilmsFoundAdapter()
    //private val add_film_cat_spinner = Spinner(requireActivity());
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        binding = FragmentFilmsOptionsBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val botNav = (activity as MainActivity).findViewById<View>(R.id.botNav)
        botNav.visibility = View.VISIBLE

        binding.FoundFilmsRV.layoutManager = LinearLayoutManager(activity as MainActivity)
        binding.FoundFilmsRV.adapter = adapter

        with(binding){
            AddFilm.setOnClickListener{
                addFilmDialog(activity as MainActivity, layoutInflater)
            }
            FilmsTable.setOnClickListener{
                findNavController().navigate(R.id.action_filmsOptions_to_filmsTable)
            }
            AddCategory.setOnClickListener {
                addCategoryDialog(activity as MainActivity, layoutInflater)
            }
            CategoryTable.setOnClickListener {
                findNavController().navigate(R.id.action_filmsOptions_to_categoryTable)
            }
            FindFilmBtn.setOnClickListener {
                val found_films : ArrayList<FindFilmType> = arrayListOf()
                if(FindFilmName.text != null) {
                    found_films.addAll((activity as MainActivity).data_base_manager.findFilmWithTitle(FindFilmName.text.toString()))
                }
                if (found_films.isEmpty()){
                    FindFilmName.setText("No such film")
                    FoundFilmsRV.visibility = View.GONE
                }
                else{
                    FoundFilmsRV.visibility = View.VISIBLE
                    found_films.add(0, FindFilmType())
                    adapter.addAll(found_films)
                }
                //убрать клаву
                val view: View? = requireActivity().currentFocus
                if(view != null) {
                    val imm: InputMethodManager =
                        requireActivity().getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
                    imm.hideSoftInputFromWindow(requireActivity().currentFocus!!.windowToken, 0)
                }


            }
        }
    }
    override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
        selected_cat_pos = parent?.selectedItemPosition
        //selected_cat_pos = if (selected_cat_pos!= null ) selected_cat_pos!! + 1 else null

    }
    private var selected_cat_pos: Int? = null
    override fun onNothingSelected(parent: AdapterView<*>?) {
        TODO("Not yet implemented")
    }
    private fun addFilmDialog(context: Context, inflater: LayoutInflater){
        val mBuilder = AlertDialog.Builder(context)
        val mView = inflater.inflate(R.layout.add_film, null)
        val binding = AddFilmBinding.bind(mView)
        mBuilder.setView(mView)
        val dialog: AlertDialog = mBuilder.create()

        val categories_list = (activity as MainActivity).data_base_manager.readCategoriesFromTable()
        val categories_titles = ArrayList<String>()
        categories_titles.add("Выберите категорию")
        for (elem in categories_list){
            categories_titles.add(elem.title)
        }

        val spinner_adapter = ArrayAdapter(activity as MainActivity, android.R.layout.simple_expandable_list_item_1, categories_titles)
        binding.Category.adapter = spinner_adapter
        binding.Category.onItemSelectedListener = this

        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog.show()
        binding.CancelBtn.setOnClickListener {
            dialog.dismiss()
        }
        binding.AddBtn.setOnClickListener {
            if(selected_cat_pos != null && selected_cat_pos != 0) {
                (activity as MainActivity).data_base_manager.insertFilmToDB(
                    title = binding.Title.text.toString(),
                    remain = binding.Remain.text.toString().toInt(), category_ID = selected_cat_pos!!,
                    cassette_price = binding.Price.text.toString().toDouble()
                )
            }
            else{
                Toast.makeText(activity, "Ошибка", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun addCategoryDialog(context: Context, inflater: LayoutInflater){
        val mBuilder = AlertDialog.Builder(context)
        val mView = inflater.inflate(R.layout.add_category, null)
        val binding = AddCategoryBinding.bind(mView)
        mBuilder.setView(mView)
        val dialog: AlertDialog = mBuilder.create()
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog.show()
        binding.CancelBtn.setOnClickListener {
            dialog.dismiss()
        }
        binding.AddBtn.setOnClickListener {
            (activity as MainActivity).data_base_manager.insertCategoryToDB(binding.Category.text.toString(), binding.Tariff.text.toString().toDouble())
        }
    }
    inner class FilmsFoundAdapter : RecyclerView.Adapter<FilmsFoundAdapter.FilmsFoundHolder>() {

        val table_content: ArrayList<FindFilmType> = arrayListOf()

        inner class FilmsFoundHolder(item: View) : RecyclerView.ViewHolder(item) {
            val binding = FoundFilmsTableBinding.bind(item)

            fun bind(table_content: FindFilmType) = with(binding) {
                //ID.text = table_content.ID.toString()
                Title.text = table_content.title
                Category.text = table_content.category
                Remain.text = table_content.remain.toString()
                WillBeAvailable.text = "-"//table_content.category_ID.toString()
                Price.text = table_content.price.toString()

            }
        }


        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FilmsFoundHolder {
            val view =
                LayoutInflater.from(parent.context).inflate(R.layout.found_films_table, parent, false)
            return FilmsFoundHolder(view)
        }

        override fun onBindViewHolder(holder: FilmsFoundHolder, position: Int) {
            if (position != 0) {
                holder.bind(table_content[position])
            }
        }

        override fun getItemCount(): Int {
            return table_content.size
        }

        fun addAll(data: List<FindFilmType>) {
            this.table_content.clear()
            this.table_content.addAll(data)
            notifyDataSetChanged()
        }
    }


}