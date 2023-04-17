package com.app.bookstoreapp.models

data class User(
    var uid: String? = "",
    var username: String? = "",
    var linkImage: String = "",
    var addressUser: String = "",
    var email: String? = "",
    var password: String? = "",
    var dateofbirth: String? = ""
)
