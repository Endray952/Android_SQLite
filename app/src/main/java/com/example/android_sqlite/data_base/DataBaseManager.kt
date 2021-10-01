package com.example.android_sqlite.data_base

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteDatabase.deleteDatabase
import android.provider.BaseColumns

import android.provider.ContactsContract

class DataBaseManager(context: Context) {
    val DbHelper = DataBaseHelper(context)
    var db: SQLiteDatabase? = null
    fun openDb(){
        db = DbHelper.writableDatabase
        //db?.delete(DataBaseConsts.Films.TABLE_NAME, null, null);
    }
    fun insertFilmToDB(title: String, remain: Int = 0, category_ID: Int = 0, cassette_price : Double = 0.0){
        val values = ContentValues().apply {
            put(DataBaseConsts.Films.COLUMN_NAME_TITLE, title)
            put(DataBaseConsts.Films.COLUMN_NAME_COPIES_REMAIN, remain)
            put(DataBaseConsts.Films.COLUMN_NAME_CATEGORY_ID, category_ID)
            put(DataBaseConsts.Films.COLUMN_NAME_CASSETTE_PRICE, cassette_price)
        }
        db?.insert(DataBaseConsts.Films.TABLE_NAME, null, values)
    }
    fun readFilmsFromTable() : ArrayList<FilmsType>{
        //db?.execSQL("DROP TABLE "+ DataBaseConsts.Films.TABLE_NAME)
        val dataList = ArrayList<FilmsType>()
        val cursor = db?.query(DataBaseConsts.Films.TABLE_NAME, null, null, null, null, null, null, null )
        while (cursor?.moveToNext()!!){
            val data = FilmsType()
            data.ID = cursor?.getInt(cursor.getColumnIndex(BaseColumns._ID))
            data.title = cursor?.getString(cursor.getColumnIndex(DataBaseConsts.Films.COLUMN_NAME_TITLE))
            data.remain = cursor?.getInt(cursor.getColumnIndex(DataBaseConsts.Films.COLUMN_NAME_COPIES_REMAIN))
            data.price = cursor?.getDouble(cursor.getColumnIndex(DataBaseConsts.Films.COLUMN_NAME_CASSETTE_PRICE))
            dataList.add(data)
        }
        cursor.close()
        return  dataList
    }

    fun insertCategoryToDB(title: String = "", tariff : Double = 0.0){
        val values = ContentValues().apply {
            put(DataBaseConsts.Categories.COLUMN_NAME_TITLE, title)
            put(DataBaseConsts.Categories.COLUMN_NAME_TARIFF, tariff)
        }
        db?.insert(DataBaseConsts.Categories.TABLE_NAME, null, values)
    }
    fun readCategoriesFromTable() : ArrayList<CategoryType>{
        //db?.execSQL("DROP TABLE "+ DataBaseConsts.Films.TABLE_NAME)
        val dataList = ArrayList<CategoryType>()
        val cursor = db?.query(DataBaseConsts.Categories.TABLE_NAME, null, null, null, null, null, null, null )
        while (cursor?.moveToNext()!!){
            val data = CategoryType()
            data.ID = cursor?.getInt(cursor.getColumnIndex(BaseColumns._ID))
            data.title = cursor?.getString(cursor.getColumnIndex(DataBaseConsts.Categories.COLUMN_NAME_TITLE))
            data.tariff = cursor?.getDouble(cursor.getColumnIndex(DataBaseConsts.Categories.COLUMN_NAME_TARIFF))
            dataList.add(data)
        }
        cursor.close()
        return  dataList
    }




    fun closeDb(){
        DbHelper.close()
    }
}