package com.example.android_sqlite.data_base

import android.provider.BaseColumns

object DataBaseConsts {
    const val  DATABASE_NAME = "VideoRent.db"
    const val  DATABASE_VERSION = 4

    object Films : BaseColumns{
        const val TABLE_NAME = "Films"
        const val COLUMN_NAME_TITLE = "title"
        const val COLUMN_NAME_COPIES_REMAIN = "copies_remain"
        const val COLUMN_NAME_CATEGORY_ID = "category_ID"
        const val COLUMN_NAME_CASSETTE_PRICE = "cassette_price"
    }
    object Categories : BaseColumns{
        const val TABLE_NAME = "Categories"
        const val COLUMN_NAME_TITLE = "Category_name"
        const val COLUMN_NAME_TARIFF = "tariff"
    }

    const val SQL_CREATE_FILMS_TABLE =
        "CREATE TABLE IF NOT EXISTS ${Films.TABLE_NAME} (" +
                "${BaseColumns._ID} INTEGER PRIMARY KEY," +
                "${Films.COLUMN_NAME_TITLE} TEXT," +
                "${Films.COLUMN_NAME_COPIES_REMAIN} INTEGER," +
                "${Films.COLUMN_NAME_CATEGORY_ID} INTEGER," +
                "${Films.COLUMN_NAME_CASSETTE_PRICE} REAL)"

    const val SQL_CREATE_CATEGORIES_TABLE =
        "CREATE TABLE IF NOT EXISTS ${Categories.TABLE_NAME} (" +
                "${BaseColumns._ID} INTEGER PRIMARY KEY," +
                "${Categories.COLUMN_NAME_TITLE} TEXT," +
                "${Categories.COLUMN_NAME_TARIFF} REAL)"

    const val SQL_DELETE_FILMS_TABLE = "DROP TABLE IF EXISTS ${Films.TABLE_NAME}"
    const val SQL_DELETE_CATEGORIES_TABLE = "DROP TABLE IF EXISTS ${Categories.TABLE_NAME}"

}