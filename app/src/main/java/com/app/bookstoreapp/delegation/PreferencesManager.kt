package com.app.bookstoreapp.delegation

class PreferencesManager {
    infix fun sumNumberTest(number: Int): Int {
        return number * number
    }

    infix fun dataType(x: Any): Any {
        var i = when (x) {
            is String -> "String"
            is Int -> "Integer"
            is Double -> "Double"
            else -> "invalid"
        }
        return i
    }
}
