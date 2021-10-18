package com.example.android_sqlite.DataBase

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.util.Log
import com.example.android_sqlite.Customers.*
import com.example.android_sqlite.DateType
import com.example.android_sqlite.Films.CategoryType
import com.example.android_sqlite.Films.FilmsType
import com.example.android_sqlite.Films.FindFilmType
import com.example.android_sqlite.Finances.FinancesType
import java.lang.Math.abs
import java.util.*
import kotlin.collections.ArrayList

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

        var cursor = db?.rawQuery("SELECT count(*) from ${DataBaseConsts.Date.TABLE_NAME}", null)
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
        cursor?.close()

        cursor = db?.rawQuery("SELECT * from ${DataBaseConsts.Orders.TABLE_NAME}", null)
        while (cursor?.moveToNext()!!){
            if(cursor.isNull(cursor.getColumnIndex(DataBaseConsts.Orders.COLUMN_NAME_FLAG_NOT_RETURNED)) &&
                cursor.isNull(cursor.getColumnIndex(DataBaseConsts.Orders.COLUMN_NAME_CLOSE_DATE)) ){
                val end_of_rent = reparseDate(cursor.getString(cursor.getColumnIndex(DataBaseConsts.Orders.COLUMN_NAME_END_OF_RENT)))
                val current_day = getDate()
                val diff = calculateDays(end_of_rent,current_day)
                if(diff >= 365){
                    //val date_of_write_off =
                    val end_calendar: Calendar = Calendar.getInstance()
                    end_calendar.set(Calendar.DAY_OF_MONTH, end_of_rent.day)
                    end_calendar.set(Calendar.MONTH, end_of_rent.month)
                    end_calendar.set(Calendar.YEAR, end_of_rent.year)
                    end_calendar.add(Calendar.DATE, 365)
                    val date_of_write_off = DateType(end_calendar.get(Calendar.DAY_OF_MONTH),end_calendar.get(Calendar.MONTH), end_calendar.get(Calendar.YEAR))
                    Log.d("MyLog", date_of_write_off.toString())
                    val close_date = "${date_of_write_off.day}/${date_of_write_off.month}/${date_of_write_off.year}"
                    val id = cursor?.getInt(cursor.getColumnIndex(DataBaseConsts.Orders.ID))
                    db?.execSQL("UPDATE  ${DataBaseConsts.Orders.TABLE_NAME} SET ${DataBaseConsts.Orders.COLUMN_NAME_CLOSE_DATE} = '$close_date', " +
                            "${DataBaseConsts.Orders.COLUMN_NAME_FLAG_NOT_RETURNED} = 1 WHERE ${DataBaseConsts.Orders.ID} = $id")
                }
            }
        }

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
        val cursor = db?.rawQuery("SELECT * FROM ${DataBaseConsts.Films.TABLE_NAME} INNER JOIN ${DataBaseConsts.Categories.TABLE_NAME} ON " +
                "${DataBaseConsts.Films.COLUMN_NAME_CATEGORY_ID} = ${DataBaseConsts.Categories.TABLE_NAME}.${DataBaseConsts.Categories.ID}", null)

        while (cursor?.moveToNext()!!){
            if( cursor?.getInt(cursor.getColumnIndex(DataBaseConsts.Films.COLUMN_NAME_COPIES_REMAIN)) != 0) {
                    val data = FilmOrderType(
                        cursor?.getInt(cursor.getColumnIndex(DataBaseConsts.Films.ID)),
                        cursor?.getString(cursor.getColumnIndex(DataBaseConsts.Films.COLUMN_NAME_TITLE)),
                        cursor?.getInt(cursor.getColumnIndex(DataBaseConsts.Films.COLUMN_NAME_COPIES_REMAIN)),
                        cursor?.getString(cursor.getColumnIndex(DataBaseConsts.Categories.COLUMN_NAME_TITLE))
                    )
                    found_films.add(data)
                }
        }
        cursor.close()
        return found_films
    }
    fun updateFilmsRemain(id: Int, increment: Int){
        db?.execSQL("UPDATE  ${DataBaseConsts.Films.TABLE_NAME} SET ${DataBaseConsts.Films.COLUMN_NAME_COPIES_REMAIN} = ${DataBaseConsts.Films.COLUMN_NAME_COPIES_REMAIN} + $increment " +
                "WHERE ${DataBaseConsts.Films.ID} = $id")
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
            data.close_date = cursor?.getString(cursor.getColumnIndex(DataBaseConsts.Orders.COLUMN_NAME_CLOSE_DATE)) ?: ""
            data.flag_not_returned = cursor?.getInt(cursor.getColumnIndex(DataBaseConsts.Orders.COLUMN_NAME_FLAG_NOT_RETURNED))
           /* val flag: Int? = cursor?.getInt(cursor.getColumnIndex(DataBaseConsts.Orders.COLUMN_NAME_FLAG_NOT_RETURNED))
            if(flag != null) {
                data.flag_not_returned = flag
                    //cursor?.getInt(cursor.getColumnIndex(DataBaseConsts.Orders.COLUMN_NAME_FLAG_NOT_RETURNED))
            }*/
            data_list.add(data)
        }
        cursor.close()
        return  data_list
    }

    fun getOrdersOfCustomer(id: Int): ArrayList<CustomerOrderType>{
        val data_list = ArrayList<CustomerOrderType>()
        val cursor = db?.rawQuery("SELECT * FROM ${DataBaseConsts.Orders.TABLE_NAME} " +
                "INNER JOIN ${DataBaseConsts.Films.TABLE_NAME} ON ${DataBaseConsts.Orders.COLUMN_NAME_FILM_ID} = ${DataBaseConsts.Films.ID} INNER JOIN " +
                "${DataBaseConsts.Categories.TABLE_NAME} ON ${DataBaseConsts.Films.COLUMN_NAME_CATEGORY_ID} = ${DataBaseConsts.Categories.ID} WHERE " +
                "${DataBaseConsts.Orders.COLUMN_NAME_CUSTOMER_ID} = $id AND ${DataBaseConsts.Orders.COLUMN_NAME_CLOSE_DATE} IS NULL"  , null)
        while (cursor?.moveToNext()!!){
            val data = CustomerOrderType(cursor?.getInt(cursor.getColumnIndex(DataBaseConsts.Orders.ID)),
                    cursor?.getString(cursor.getColumnIndex(DataBaseConsts.Films.COLUMN_NAME_TITLE)),
                    cursor?.getString(cursor.getColumnIndex(DataBaseConsts.Categories.COLUMN_NAME_TITLE)),
                    cursor?.getString(cursor.getColumnIndex(DataBaseConsts.Orders.COLUMN_NAME_START_OF_RENT)),
                    cursor?.getString(cursor.getColumnIndex(DataBaseConsts.Orders.COLUMN_NAME_END_OF_RENT)))
            data_list.add(data)
        }
        cursor.close()
        return  data_list
    }
    fun updateDBafterReturn(order_id: Int){
        val date = getDate()
        val stringDate = "${date.day}/${date.month}/${date.year}"
        db?.execSQL("UPDATE  ${DataBaseConsts.Orders.TABLE_NAME} SET ${DataBaseConsts.Orders.COLUMN_NAME_CLOSE_DATE} = '$stringDate' " +
                "WHERE ${DataBaseConsts.Orders.ID} = $order_id")
        val cursor = db?.rawQuery("SELECT ${DataBaseConsts.Orders.COLUMN_NAME_FILM_ID} FROM ${DataBaseConsts.Orders.TABLE_NAME} " +
                "WHERE ${DataBaseConsts.Orders.ID} = $order_id", null)
        cursor?.moveToFirst()
        if(cursor != null && cursor.count != 0){
            val film_id = cursor.getInt(cursor.getColumnIndex(DataBaseConsts.Orders.COLUMN_NAME_FILM_ID))
            updateFilmsRemain(film_id, 1)
        }
        cursor?.close()
    }

    fun getDebtors(): ArrayList<DebtorType>{
        val data_list = arrayListOf<DebtorType>()
        val cursor = db?.rawQuery("SELECT * FROM ${DataBaseConsts.Orders.TABLE_NAME} " +
                "INNER JOIN ${DataBaseConsts.Customers.TABLE_NAME} ON ${DataBaseConsts.Orders.COLUMN_NAME_CUSTOMER_ID} = ${DataBaseConsts.Customers.ID} " +
                "INNER JOIN ${DataBaseConsts.Films.TABLE_NAME} ON ${DataBaseConsts.Orders.COLUMN_NAME_FILM_ID} = ${DataBaseConsts.Films.ID} " +
                "INNER JOIN ${DataBaseConsts.Categories.TABLE_NAME} ON ${DataBaseConsts.Films.COLUMN_NAME_CATEGORY_ID} = ${DataBaseConsts.Categories.ID} " +
                "WHERE ${DataBaseConsts.Orders.COLUMN_NAME_CLOSE_DATE} IS NULL" , null)
        while (cursor?.moveToNext()!!){
            val start_of_rent = reparseDate(cursor.getString(cursor.getColumnIndex(DataBaseConsts.Orders.COLUMN_NAME_START_OF_RENT)))
            val end_of_rent = reparseDate(cursor.getString(cursor.getColumnIndex(DataBaseConsts.Orders.COLUMN_NAME_END_OF_RENT)))
            val current_date = getDate()
            if(end_of_rent.year < current_date.year ||
                (end_of_rent.year == current_date.year && end_of_rent.month < current_date.month) ||
                (end_of_rent.year == current_date.year && end_of_rent.month == current_date.month && end_of_rent.day < current_date.day)) {

                    val tariff = cursor.getDouble(cursor.getColumnIndex(DataBaseConsts.Categories.COLUMN_NAME_TARIFF))
                    val dayes_overdue = calculateDays(start_of_rent, current_date) - calculateDays(start_of_rent, end_of_rent)
                    val total_debt =  calculateDays(start_of_rent, current_date) * tariff
                    val data = DebtorType(
                        cursor.getString(cursor.getColumnIndex(DataBaseConsts.Customers.COLUMN_NAME_CUSTOMER_FIRST_NAME)),
                        cursor.getString(cursor.getColumnIndex(DataBaseConsts.Customers.COLUMN_NAME_CUSTOMER_SECOND_NAME)),
                        cursor.getString(cursor.getColumnIndex(DataBaseConsts.Customers.COLUMN_NAME_CUSTOMER_EMAIL)),
                        cursor.getString(cursor.getColumnIndex(DataBaseConsts.Customers.COLUMN_NAME_CUSTOMER_PHONE_NUMBER)),
                        cursor.getString(cursor.getColumnIndex(DataBaseConsts.Orders.COLUMN_NAME_START_OF_RENT)),
                        cursor.getString(cursor.getColumnIndex(DataBaseConsts.Orders.COLUMN_NAME_END_OF_RENT)),
                        cursor.getString(cursor.getColumnIndex(DataBaseConsts.Films.COLUMN_NAME_TITLE)),
                        total_debt, dayes_overdue)
                    data_list.add(data)
            }
        }
        cursor.close()
        return data_list
    }

    fun createFinancesReport(year: Int): ArrayList<FinancesType>{
        val data_list = initFinancesList()
        val current_date: DateType = getDate()
        val cursor = db?.rawQuery("SELECT * FROM ${DataBaseConsts.Orders.TABLE_NAME} INNER JOIN ${DataBaseConsts.Films.TABLE_NAME} ON " +
                "${DataBaseConsts.Orders.COLUMN_NAME_FILM_ID} = ${DataBaseConsts.Films.ID} INNER JOIN ${DataBaseConsts.Categories.TABLE_NAME} ON " +
                "${DataBaseConsts.Films.COLUMN_NAME_CATEGORY_ID} = ${DataBaseConsts.Categories.ID}", null)
        while(cursor?.moveToNext()!!){
            if(!cursor.isNull(cursor.getColumnIndex(DataBaseConsts.Orders.COLUMN_NAME_FLAG_NOT_RETURNED))){ //If not returned
               // val date: DateType = reparseDate(cursor?.getString(cursor.getColumnIndex(DataBaseConsts.Orders.COLUMN_NAME_START_OF_RENT)))
                val start_of_rent = reparseDate(cursor?.getString(cursor.getColumnIndex(DataBaseConsts.Orders.COLUMN_NAME_START_OF_RENT)))
                if(year - start_of_rent.year == 1) {
                    val cassette_price = cursor?.getDouble(cursor.getColumnIndex(DataBaseConsts.Films.COLUMN_NAME_CASSETTE_PRICE))
                    data_list[current_date.month - 1].income += cassette_price
                }
            }
            else if (!cursor.isNull(cursor.getColumnIndex(DataBaseConsts.Orders.COLUMN_NAME_END_OF_RENT))){
                val start_of_rent = reparseDate(cursor.getString(cursor.getColumnIndex(DataBaseConsts.Orders.COLUMN_NAME_START_OF_RENT)))
                val end_of_rent = reparseDate(cursor.getString(cursor.getColumnIndex(DataBaseConsts.Orders.COLUMN_NAME_END_OF_RENT)))
                if(end_of_rent.year == year) {
                    val tariff = cursor.getDouble(cursor.getColumnIndex(DataBaseConsts.Categories.COLUMN_NAME_TARIFF))
                    data_list[end_of_rent.month - 1].income += calculateDays(start_of_rent, end_of_rent) * tariff
                }
            }
        }
        data_list[12].income = data_list.sumOf { it.income }
        return data_list
    }

    fun closeDb(){
        DbHelper.close()
    }
    private fun calculateDays(start: DateType, end: DateType) : Int{
        val start_of_rent: Calendar = Calendar.getInstance()
        start_of_rent.set(Calendar.DAY_OF_MONTH, start.day)
        start_of_rent.set(Calendar.MONTH, start.month-1)
        start_of_rent.set(Calendar.YEAR, start.year)
        val end_of_rent: Calendar = Calendar.getInstance()
        end_of_rent.set(Calendar.DAY_OF_MONTH, end.day)
        end_of_rent.set(Calendar.MONTH, end.month-1)
        end_of_rent.set(Calendar.YEAR, end.year)
        val diff: Long = end_of_rent.timeInMillis - start_of_rent.timeInMillis
        val days = diff / (24 * 60 * 60 * 1000)
        return days.toInt()
    }
     fun reparseDate(date:String): DateType{
        val lines: List<String> = date.split("/")
        val date_list = DateType(lines[0].toInt(),lines[1].toInt(),lines[2].toInt())
        return date_list
    }
    private fun initFinancesList(): ArrayList<FinancesType>{
        val data_list = ArrayList<FinancesType>()
        data_list.add(FinancesType("Январь", 0.0,0.0))
        data_list.add(FinancesType("Февраль", 0.0,0.0))
        data_list.add(FinancesType("Март", 0.0,0.0))
        data_list.add(FinancesType("Апрель", 0.0,0.0))
        data_list.add(FinancesType("Март", 0.0,0.0))
        data_list.add(FinancesType("Июнь", 0.0,0.0))
        data_list.add(FinancesType("Июль", 0.0,0.0))
        data_list.add(FinancesType("Август", 0.0,0.0))
        data_list.add(FinancesType("Сентябрь", 0.0,0.0))
        data_list.add(FinancesType("Октябрь", 0.0,0.0))
        data_list.add(FinancesType("Ноябрь", 0.0,0.0))
        data_list.add(FinancesType("Декабрь", 0.0,0.0))
        data_list.add(FinancesType("Всего", 0.0,0.0))
        return data_list
    }
}