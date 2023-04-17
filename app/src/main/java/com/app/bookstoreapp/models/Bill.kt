package com.app.bookstoreapp.models

import com.google.firebase.database.IgnoreExtraProperties

@IgnoreExtraProperties
data class Bill(
    val idUser: String? = "",
    val idBill: String = "",
    val sumQuantity: Int = 0,
    val sumPrice: Int = 0,
    val date: String = "",
    val address:String =""
)