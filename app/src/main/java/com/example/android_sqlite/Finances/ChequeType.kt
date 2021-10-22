package com.example.android_sqlite.Finances

data class ChequeType(val film : String, val start_of_rent: String, val end_of_rent: String,
                      val close_date: String,val tariff: Double, val cassette_price: Double,
                      val is_getting: Boolean, val customer_payment: Double,val shop_payment: Double)