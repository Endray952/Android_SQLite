package com.example.android_sqlite.DataBase

object DataBaseConsts {
    const val  DATABASE_NAME = "VideoRent.db"
    const val  DATABASE_VERSION = 32

    object Films {
        const val ID = "film_id"
        const val TABLE_NAME = "Films"
        const val COLUMN_NAME_TITLE = "title"
        const val COLUMN_NAME_COPIES_REMAIN = "copies_remain"
        const val COLUMN_NAME_CATEGORY_ID = "category_ID"
        const val COLUMN_NAME_CASSETTE_PRICE = "cassette_price"
    }
    object Categories {
        const val ID = "_id"
        const val TABLE_NAME = "Categories"
        const val COLUMN_NAME_TITLE = "Category_name"
        const val COLUMN_NAME_TARIFF = "tariff"
    }
    object Orders {
        const val ID = "_order_id"
        const val TABLE_NAME = "Orders"
        const val COLUMN_NAME_FILM_ID = "column_film_id"
        const val COLUMN_NAME_CUSTOMER_ID = "customer_id"
        const val COLUMN_NAME_START_OF_RENT = "start_of_rent"
        const val COLUMN_NAME_END_OF_RENT = "end_of_rent"
        const val COLUMN_NAME_FLAG_NOT_RETURNED = "flag_not_returned"
    }
    object Customers {
        const val ID = "_customer_id"
        const val TABLE_NAME = "Customers"
        const val COLUMN_NAME_CUSTOMER_FIRST_NAME = "first_name"
        const val COLUMN_NAME_CUSTOMER_SECOND_NAME = "second_name"
        const val COLUMN_NAME_CUSTOMER_EMAIL = "email"
        const val COLUMN_NAME_CUSTOMER_PHONE_NUMBER = "phone_number"
    }
    object Date{
        const val TABLE_NAME = "DATE"
        const val DATE = "cur_date"
        const val ID = "_id"
    }

    const val SQL_CREATE_FILMS_TABLE =
        "CREATE TABLE IF NOT EXISTS ${Films.TABLE_NAME} (" +
                "${Films.ID} INTEGER PRIMARY KEY AUTOINCREMENT," +
                "${Films.COLUMN_NAME_TITLE} TEXT NOT NULL," +
                "${Films.COLUMN_NAME_COPIES_REMAIN} INTEGER NOT NULL," +
                "${Films.COLUMN_NAME_CATEGORY_ID} INTEGER NOT NULL," +
                "${Films.COLUMN_NAME_CASSETTE_PRICE} REAL NOT NULL)"

    const val SQL_CREATE_CATEGORIES_TABLE =
        "CREATE TABLE IF NOT EXISTS ${Categories.TABLE_NAME} (" +
                "${Categories.ID} INTEGER PRIMARY KEY AUTOINCREMENT," +
                "${Categories.COLUMN_NAME_TITLE} TEXT NOT NULL," +
                "${Categories.COLUMN_NAME_TARIFF} REAL NOT NULL)"

    const val SQL_CREATE_ORDERS_TABLE =
        "CREATE TABLE IF NOT EXISTS ${Orders.TABLE_NAME} (" +
                "${Orders.ID} INTEGER PRIMARY KEY AUTOINCREMENT," +
                "${Orders.COLUMN_NAME_FILM_ID} INTEGER NOT NULL," +
                "${Orders.COLUMN_NAME_CUSTOMER_ID} INTEGER NOT NULL," +
                "${Orders.COLUMN_NAME_START_OF_RENT} TEXT NOT NULL," +
                "${Orders.COLUMN_NAME_END_OF_RENT} TEXT NOT NULL," +
                "${Orders.COLUMN_NAME_FLAG_NOT_RETURNED} INTEGER)"

    const val SQL_CREATE_CUSTOMERS_TABLE =
        "CREATE TABLE IF NOT EXISTS ${Customers.TABLE_NAME} (" +
                "${Customers.ID} INTEGER PRIMARY KEY AUTOINCREMENT," +
                "${Customers.COLUMN_NAME_CUSTOMER_FIRST_NAME} TEXT NOT NULL," +
                "${Customers.COLUMN_NAME_CUSTOMER_SECOND_NAME} TEXT NOT NULL," +
                "${Customers.COLUMN_NAME_CUSTOMER_EMAIL} TEXT NOT NULL," +
                "${Customers.COLUMN_NAME_CUSTOMER_PHONE_NUMBER} TEXT NOT NULL)"

    const val SQL_CREATE_DATE_TABLE = "CREATE TABLE IF NOT EXISTS ${Date.TABLE_NAME} " +
            "(${Date.ID} INTEGER PRIMARY KEY, " +
            "${Date.DATE} TEXT)"

    const val SQL_DELETE_FILMS_TABLE = "DROP TABLE IF EXISTS ${Films.TABLE_NAME}"
    const val SQL_DELETE_CATEGORIES_TABLE = "DROP TABLE IF EXISTS ${Categories.TABLE_NAME}"
    const val SQL_DELETE_CUSTOMERS_TABLE = "DROP TABLE IF EXISTS ${Customers.TABLE_NAME}"
    const val SQL_DELETE_ORDERS_TABLE = "DROP TABLE IF EXISTS ${Orders.TABLE_NAME}"
    const val SQL_DELETE_DATE_TABLE = "DROP TABLE IF EXISTS ${Date.TABLE_NAME}"

}