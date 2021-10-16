package com.example.android_sqlite.DataBase

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import com.example.android_sqlite.Customers.CustomerType
import com.example.android_sqlite.Customers.FilmOrderType
import com.example.android_sqlite.Films.FilmsType
import com.example.android_sqlite.Films.FindFilmType
import com.example.android_sqlite.Customers.OrderType
import com.example.android_sqlite.DateType
import com.example.android_sqlite.Films.CategoryType

class DataBaseManager(context: Context) {
    val DbHelper = DataBaseHelper(context)
    var db: SQLiteDatabase? = null
    fun openDb(){
        db = DbHelper.writableDatabase
        //db?.delete(DataBaseConsts.Films.TABLE_NAME, null, null);
    }
    fun setDate(day: Int, month: Int, year: Int){
        val date = "$day/$month/$year"
        //db?.execSQL("INSERT INTO  ${DataBaseConsts.Date.TABLE_NAME} (${DataBaseConsts.Date.DATE}) VALUES('$date')")
        //db?.execSQL("UPDATE  ${DataBaseConsts.Date.TABLE_NAME} SET (${DataBaseConsts.Date.DATE}) = '$date'")
        //db?.execSQL("REPLACE INTO ${DataBaseConsts.Date.TABLE_NAME}(${DataBaseConsts.Date.DATE}) VALUES($date)")
        /*db?.execSQL("INSERT OR IGNORE INTO  ${DataBaseConsts.Date.TABLE_NAME} (${DataBaseConsts.Date.DATE}) VALUES('$date')" +
                " UPDATE OR IGNORE ${DataBaseConsts.Date.TABLE_NAME} SET (${DataBaseConsts.Date.DATE}) = '$date'")*/
        //db?.execSQL("INSERT OR REPLACE INTO ${DataBaseConsts.Date.TABLE_NAME}(${DataBaseConsts.Date.DATE}) VALUES('$date')")
        /*db?.execSQL("INSERT INTO ${DataBaseConsts.Date.TABLE_NAME}(${DataBaseConsts.Date.ID}, ${DataBaseConsts.Date.DATE})" +
                "VALUES(1, '$date') ON CONFLICT(${DataBaseConsts.Date.ID}) DO UPDATE SET ${DataBaseConsts.Date.DATE} = '$date'")*/

        val cursor = db?.rawQuery("SELECT count(*) from ${DataBaseConsts.Date.TABLE_NAME}", null)
        cursor?.moveToFirst()
        val count = cursor?.getInt(0)
        if(count != 0){
            db?.execSQL("UPDATE  ${DataBaseConsts.Date.TABLE_NAME} SET (${DataBaseConsts.Date.DATE}) = '$date'")
        }
        else{
            db?.execSQL("INSERT INTO  ${DataBaseConsts.Date.TABLE_NAME} (${DataBaseConsts.Date.DATE}) VALUES('$date')")
        }
       /* db?.execSQL("INSERT INTO ${DataBaseConsts.Date.TABLE_NAME}(${DataBaseConsts.Date.ID}, ${DataBaseConsts.Date.DATE}) VALUES(1, '$date') " +
                "ON CONFLICT(${DataBaseConsts.Date.ID}) DO UPDATE SET ${DataBaseConsts.Date.DATE} = '$date'")*/
    }
    fun getDate(): DateType{
        val cursor = db?.rawQuery("SELECT * FROM ${DataBaseConsts.Date.TABLE_NAME}", null)
        cursor?.moveToFirst()
        var date = ""
        if(cursor != null && cursor.count > 0){
            date = cursor?.getString(cursor.getColumnIndex(DataBaseConsts.Date.DATE))
        }
        val int_date = DateType(0,0,0)

        if(date != ""){
            val lines: List<String> = date.split("/")
            int_date.day = lines[0].toInt()
            int_date.month = lines[1].toInt()
            int_date.year = lines[2].toInt()
        }
        cursor?.close()
        return int_date
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
       /* val cursor = db?.rawQuery("SELECT * FROM ${DataBaseConsts.Films.TABLE_NAME} WHERE TRIM(${DataBaseConsts.Films.COLUMN_NAME_TITLE}) = '${title.trim()}'"
            , null)*/
        val cursor = db?.rawQuery("SELECT * FROM ${DataBaseConsts.Films.TABLE_NAME} JOIN ${DataBaseConsts.Categories.TABLE_NAME} ON " +
                    "${DataBaseConsts.Films.COLUMN_NAME_CATEGORY_ID} = ${DataBaseConsts.Categories.TABLE_NAME}.${DataBaseConsts.Categories.ID} " +
                        "WHERE TRIM(${DataBaseConsts.Films.COLUMN_NAME_TITLE}) = '${title.trim()}'", null)

        while (cursor?.moveToNext()!!){
            val data = FindFilmType()
            data.title = cursor?.getString(cursor.getColumnIndex(DataBaseConsts.Films.COLUMN_NAME_TITLE))
            data.remain = cursor?.getInt(cursor.getColumnIndex(DataBaseConsts.Films.COLUMN_NAME_COPIES_REMAIN))
            data.price = cursor?.getDouble(cursor.getColumnIndex(DataBaseConsts.Films.COLUMN_NAME_CASSETTE_PRICE))
            data.category = cursor?.getString(cursor.getColumnIndex(DataBaseConsts.Categories.COLUMN_NAME_TITLE))
           // val category_ID = cursor?.getInt(cursor.getColumnIndex(DataBaseConsts.Films.COLUMN_NAME_CATEGORY_ID))
            //Join category to film
            //val join_cursor = db?.rawQuery("SELECT * FROM ${DataBaseConsts.Categories.TABLE_NAME} WHERE ${DataBaseConsts.Categories.ID} = '${category_ID}'", null)
            //join_cursor?.moveToFirst()
            //data.category = join_cursor?.getString(join_cursor.getColumnIndex(DataBaseConsts.Categories.COLUMN_NAME_TITLE)).toString()

            found_films.add(data)
            //join_cursor?.close()
        }
        cursor.close()
        return found_films
    }
    fun getFilmAndCategory() : ArrayList<FilmOrderType>{
        var found_films = arrayListOf<FilmOrderType>()
        val cursor = db?.rawQuery("SELECT * FROM ${DataBaseConsts.Films.TABLE_NAME} JOIN ${DataBaseConsts.Categories.TABLE_NAME} ON " +
                "${DataBaseConsts.Films.COLUMN_NAME_CATEGORY_ID} = ${DataBaseConsts.Categories.TABLE_NAME}.${DataBaseConsts.Categories.ID}", null)

        while (cursor?.moveToNext()!!){
            if( cursor?.getInt(cursor.getColumnIndex(DataBaseConsts.Films.COLUMN_NAME_COPIES_REMAIN)) != 0) {
                    val data = FilmOrderType(
                        cursor?.getInt(cursor.getColumnIndex(DataBaseConsts.Films.ID)),
                        cursor?.getString(cursor.getColumnIndex(DataBaseConsts.Films.COLUMN_NAME_TITLE)),
                        cursor?.getInt(cursor.getColumnIndex(DataBaseConsts.Films.COLUMN_NAME_COPIES_REMAIN)),
                        cursor?.getString(cursor.getColumnIndex(DataBaseConsts.Categories.COLUMN_NAME_TITLE))
                    )
                    /*data.title = cursor?.getString(cursor.getColumnIndex(DataBaseConsts.Films.COLUMN_NAME_TITLE))
            data.remain = cursor?.getInt(cursor.getColumnIndex(DataBaseConsts.Films.COLUMN_NAME_COPIES_REMAIN))
            data.price = cursor?.getDouble(cursor.getColumnIndex(DataBaseConsts.Films.COLUMN_NAME_CASSETTE_PRICE))
            data.category = cursor?.getString(cursor.getColumnIndex(DataBaseConsts.Categories.COLUMN_NAME_TITLE))*/
                    found_films.add(data)
                }
        }
        cursor.close()
        return found_films
    }
    fun insertClientToDB(first_name: String, second_name: String, email: String, phone_number: String){
        db?.execSQL("INSERT INTO " + DataBaseConsts.Customers.TABLE_NAME +"(${DataBaseConsts.Customers.COLUMN_NAME_CUSTOMER_FIRST_NAME}, " +
                "${DataBaseConsts.Customers.COLUMN_NAME_CUSTOMER_SECOND_NAME}, ${DataBaseConsts.Customers.COLUMN_NAME_CUSTOMER_EMAIL}, " +
                "${DataBaseConsts.Customers.COLUMN_NAME_CUSTOMER_PHONE_NUMBER})" + " VALUES('$first_name', '$second_name', '$email', '$phone_number')")
    }
    fun readClientsFromTable(): ArrayList<CustomerType>{
        val dataList = ArrayList<CustomerType>()
        val cursor = db?.rawQuery("SELECT * FROM ${DataBaseConsts.Customers.TABLE_NAME}", null)
        while (cursor?.moveToNext()!!){
            val data = CustomerType()
            data.ID = cursor?.getInt(cursor.getColumnIndex(DataBaseConsts.Customers.ID))
            data.first_name = cursor?.getString(cursor.getColumnIndex(DataBaseConsts.Customers.COLUMN_NAME_CUSTOMER_FIRST_NAME))
            data.second_name = cursor?.getString(cursor.getColumnIndex(DataBaseConsts.Customers.COLUMN_NAME_CUSTOMER_SECOND_NAME))
            data.email = cursor?.getString(cursor.getColumnIndex(DataBaseConsts.Customers.COLUMN_NAME_CUSTOMER_EMAIL))
            data.phone_number = cursor?.getString(cursor.getColumnIndex(DataBaseConsts.Customers.COLUMN_NAME_CUSTOMER_PHONE_NUMBER))
            dataList.add(data)
        }
        cursor.close()
        return  dataList
    }

    fun insertOrderToDB(film_ID: Int, customer_ID: Int, _start_of_rent: DateType, _end_of_rent: DateType){
        val start_of_rent = "${_start_of_rent.day}/${_start_of_rent.month}/${_start_of_rent.year}"
        val end_of_rent = "${_end_of_rent.day}/${_end_of_rent.month}/${_end_of_rent.year}"
        db?.execSQL("INSERT INTO " + DataBaseConsts.Orders.TABLE_NAME +"(${DataBaseConsts.Orders.COLUMN_NAME_FILM_ID}, " +
                "${DataBaseConsts.Orders.COLUMN_NAME_CUSTOMER_ID}, ${DataBaseConsts.Orders.COLUMN_NAME_START_OF_RENT}, " +
                "${DataBaseConsts.Orders.COLUMN_NAME_END_OF_RENT})" + " VALUES('$film_ID', '$customer_ID', '$start_of_rent', '$end_of_rent')")

    }
    fun readOrdersFromTable(): ArrayList<OrderType>{
        val data_list = ArrayList<OrderType>()
        val cursor = db?.rawQuery("SELECT * FROM ${DataBaseConsts.Orders.TABLE_NAME}", null)
        while (cursor?.moveToNext()!!){
            val data = OrderType()
            data.ID = cursor?.getInt(cursor.getColumnIndex(DataBaseConsts.Orders.ID))
            data.film_ID = cursor?.getInt(cursor.getColumnIndex(DataBaseConsts.Orders.COLUMN_NAME_FILM_ID))
            data.customer_ID = cursor?.getInt(cursor.getColumnIndex(DataBaseConsts.Orders.COLUMN_NAME_CUSTOMER_ID))
            data.start_of_rent = cursor?.getString(cursor.getColumnIndex(DataBaseConsts.Orders.COLUMN_NAME_START_OF_RENT))
            data.end_of_rent = cursor?.getString(cursor.getColumnIndex(DataBaseConsts.Orders.COLUMN_NAME_END_OF_RENT))
            data_list.add(data)
        }
        cursor.close()
        return  data_list
    }



    fun closeDb(){
        DbHelper.close()
    }
}