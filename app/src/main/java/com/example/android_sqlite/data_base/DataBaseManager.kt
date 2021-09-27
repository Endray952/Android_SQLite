package com.example.android_sqlite.data_base

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.provider.ContactsContract

class DataBaseManager(context: Context) {
    val DbHelper = DataBaseHelper(context)
    var db: SQLiteDatabase? = null
    fun openDb(){
        db = DbHelper.writableDatabase
    }
    fun insertToDB(title: String, remain: Int = 0, category_ID: Int = 0, cassette_price : Int = 0){
        val values = ContentValues().apply {
            put(DataBaseConsts.Films.COLUMN_NAME_TITLE, title)
           /* put(DataBaseConsts.Films.COLUMN_NAME_COPIES_REMAIN, remain)
            put(DataBaseConsts.Films.COLUMN_NAME_CATEGORY_ID, category_ID)
            put(DataBaseConsts.Films.COLUMN_NAME_CASSETTE_PRICE, cassette_price)*/
        }
        db?.insert(DataBaseConsts.Films.TABLE_NAME, null, values)
    }
    fun readDB() : ArrayList<String>{
        val dataList = ArrayList<String>()
        val cursor = db?.query(DataBaseConsts.Films.TABLE_NAME, null, null, null, null, null, null, null )

        while (cursor?.moveToNext()!!){
            val dataText = cursor?.getString(cursor.getColumnIndex(DataBaseConsts.Films.COLUMN_NAME_TITLE))
            dataList.add(dataText.toString())
        }
        cursor.close()
        return  dataList
    }

    fun closeDb(){
        DbHelper.close()
    }
}