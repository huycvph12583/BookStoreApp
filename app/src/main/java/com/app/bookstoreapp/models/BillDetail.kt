package com.app.bookstoreapp.models
import com.google.firebase.database.IgnoreExtraProperties
@IgnoreExtraProperties
data class BillDetail(
    val idBill: String = "",
    val idFood: String = "",
    val name: String = "",
    val quantity: Int = 0,
    val price: Int = 0,
    val imageUrl: String = "",
)