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
   // private val adapter = FilmsFoundAdapter()
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
        //binding.FoundFilmsRV.adapter = adapter

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
                findFilmDialog(activity as MainActivity, layoutInflater)
            }
        }
    }
    override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
        when(parent?.id){
            R.id.Category -> selected_cat_pos = parent.selectedItemPosition
            R.id.Film ->{
                selected_film = parent.selectedItemPosition
                with(filmBinding){
                    CassettePrice.text = films_list[selected_film].price.toString()
                    TariffPrice.text = films_list[selected_film].tariff.toString()
                    if(films_list[selected_film].available == ""){
                        Available.text = films_list[selected_film].remain.toString()
                        AvailableText.text = "Доступно штук"
                    }
                    else{
                        Available.text = films_list[selected_film].available
                        AvailableText.text = "Будет доступно"
                }

            }}
        }

    }
    private var selected_cat_pos: Int? = null
    private var selected_film = 0
    private lateinit var filmBinding: FindFilmBinding
    override fun onNothingSelected(parent: AdapterView<*>?) {
        TODO("Not yet implemented")
    }
    var films_list = arrayListOf<FindFilmType>()
    private fun findFilmDialog(context: Context, inflater: LayoutInflater){
        val mBuilder = AlertDialog.Builder(context)
        val mView = inflater.inflate(R.layout.find_film, null)
        filmBinding = FindFilmBinding.bind(mView)
        mBuilder.setView(mView)
        val dialog: AlertDialog = mBuilder.create()

        films_list = (activity as MainActivity).data_base_manager.findFilm()
        val spinner_films_list = arrayListOf<String>()
        for(film in films_list){
            spinner_films_list.add(film.title + "\n" + film.category)
        }
        filmBinding.Film.adapter = ArrayAdapter<String>(activity as MainActivity, android.R.layout.simple_expandable_list_item_1,spinner_films_list)
        filmBinding.Film.onItemSelectedListener = this


        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog.show()

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
            if(selected_cat_pos != null && selected_cat_pos != 0 && binding.Title.text.isNotEmpty() && binding.Remain.text.isNotEmpty()) {
                (activity as MainActivity).data_base_manager.insertFilmToDB(
                    title = binding.Title.text.toString(),
                    remain = binding.Remain.text.toString().toInt(), category_ID = selected_cat_pos!!,
                    cassette_price = binding.Price.text.toString().toDouble()
                )
                dialog.dismiss()
                Toast.makeText(activity, "Успешно", Toast.LENGTH_SHORT).show()
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
            if(binding.Category.text.isNotEmpty() && binding.Tariff.text.isNotEmpty() ) {
                (activity as MainActivity).data_base_manager.insertCategoryToDB(binding.Category.text.toString(), binding.Tariff.text.toString().toDouble())
                dialog.dismiss()
                Toast.makeText(activity, "Успешно", Toast.LENGTH_SHORT).show()
            }
            else{
                Toast.makeText(activity, "Ошибка", Toast.LENGTH_SHORT).show()
            }
        }
    }

}