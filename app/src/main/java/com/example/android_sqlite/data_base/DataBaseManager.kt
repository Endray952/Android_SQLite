package com.example.android_sqlite.data_base

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import com.example.android_sqlite.Films.FilmsType
import com.example.android_sqlite.Films.FindFilmType

class DataBaseManager(context: Context) {
    val DbHelper = DataBaseHelper(context)
    var db: SQLiteDatabase? = null
    fun openDb(){
        db = DbHelper.writableDatabase
        //db?.delete(DataBaseConsts.Films.TABLE_NAME, null, null);
    }
    fun insertFilmToDB(title: String, remain: Int = 0, category_ID: Int = 0, cassette_price : Double = 0.0){
        db?.execSQL("INSERT INTO " + DataBaseConsts.Films.TABLE_NAME+"(${DataBaseConsts.Films.COLUMN_NAME_TITLE}, " +
                "${DataBaseConsts.Films.COLUMN_NAME_COPIES_REMAIN}, ${DataBaseConsts.Films.COLUMN_NAME_CATEGORY_ID}, " +
                "${DataBaseConsts.Films.COLUMN_NAME_CASSETTE_PRICE})" + " VALUES('$title', '$remain', '$category_ID', '$cassette_price')")
    }

    fun readFilmsFromTable() : ArrayList<FilmsType>{
        //db?.execSQL("DROP TABLE "+ DataBaseConsts.Films.TABLE_NAME)
        val dataList = ArrayList<FilmsType>()
        val cursor = db?.rawQuery("SELECT * FROM ${DataBaseConsts.Films.TABLE_NAME}", null)
        while (cursor?.moveToNext()!!){
            val data = FilmsType()
            data.ID = cursor?.getInt(cursor.getColumnIndex(DataBaseConsts.Films.ID))
            data.title = cursor?.getString(cursor.getColumnIndex(DataBaseConsts.Films.COLUMN_NAME_TITLE))
            data.category_ID = cursor?.getInt(cursor.getColumnIndex(DataBaseConsts.Films.COLUMN_NAME_CATEGORY_ID))
            data.remain = cursor?.getInt(cursor.getColumnIndex(DataBaseConsts.Films.COLUMN_NAME_COPIES_REMAIN))
            data.price = cursor?.getDouble(cursor.getColumnIndex(DataBaseConsts.Films.COLUMN_NAME_CASSETTE_PRICE))
            dataList.add(data)
        }
        cursor.close()
        return  dataList
    }

    fun insertCategoryToDB(title: String = "", tariff : Double = 0.0){
        db?.execSQL("INSERT INTO " + DataBaseConsts.Categories.TABLE_NAME+"(${DataBaseConsts.Categories.COLUMN_NAME_TITLE}, " +
                "${DataBaseConsts.Categories.COLUMN_NAME_TARIFF})" + " VALUES('$title', '$tariff')")
    }

    fun readCategoriesFromTable() : ArrayList<CategoryType>{
        //db?.execSQL("DROP TABLE "+ DataBaseConsts.Films.TABLE_NAME)
        val dataList = ArrayList<CategoryType>()
        val cursor = db?.rawQuery("SELECT * FROM ${DataBaseConsts.Categories.TABLE_NAME}", null)
        while (cursor?.moveToNext()!!){
            val data = CategoryType()
            data.ID = cursor?.getInt(cursor.getColumnIndex(DataBaseConsts.Categories.ID))
            data.title = cursor?.getString(cursor.getColumnIndex(DataBaseConsts.Categories.COLUMN_NAME_TITLE))
            data.tariff = cursor?.getDouble(cursor.getColumnIndex(DataBaseConsts.Categories.COLUMN_NAME_TARIFF))
            dataList.add(data)
        }
        cursor.close()
        return  dataList
    }

    fun findFilmWithTitle(title: String) : ArrayList<FindFilmType>{
        var found_films = arrayListOf<FindFilmType>()
        val cursor = db?.rawQuery("SELECT * FROM ${DataBaseConsts.Films.TABLE_NAME} WHERE TRIM(${DataBaseConsts.Films.COLUMN_NAME_TITLE}) = '${title.trim()}'", null)

        while (cursor?.moveToNext()!!){
            val data = FindFilmType()
            data.title = cursor?.getString(cursor.getColumnIndex(DataBaseConsts.Films.COLUMN_NAME_TITLE))
            data.remain = cursor?.getInt(cursor.getColumnIndex(DataBaseConsts.Films.COLUMN_NAME_COPIES_REMAIN))
            data.price = cursor?.getDouble(cursor.getColumnIndex(DataBaseConsts.Films.COLUMN_NAME_CASSETTE_PRICE))

            val category_ID = cursor?.getInt(cursor.getColumnIndex(DataBaseConsts.Films.COLUMN_NAME_CATEGORY_ID))
            //Join category to film
            val join_cursor = db?.rawQuery("SELECT * FROM ${DataBaseConsts.Categories.TABLE_NAME} WHERE ${DataBaseConsts.Categories.ID} = '${category_ID}'", null)
            join_cursor?.moveToFirst()
            data.category = join_cursor?.getString(join_cursor.getColumnIndex(DataBaseConsts.Categories.COLUMN_NAME_TITLE)).toString()
            found_films.add(data)
            join_cursor?.close()
        }
        cursor.close()
        return found_films
    }


    fun closeDb(){
        DbHelper.close()
    }
}