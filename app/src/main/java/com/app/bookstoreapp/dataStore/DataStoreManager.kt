package com.app.bookstoreapp.dataStore

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")

class DataStoreManager(context: Context) {
    private var appContext = context.applicationContext

    val mData: Flow<String?> = appContext.dataStore.data.map { preferences ->
        preferences[IMAGE]?:"none"
    }

    suspend fun insertData(value: String) {
        appContext.dataStore.edit { preferences ->
            preferences[IMAGE] = value
        }
    }

    companion object {
        private val IMAGE = stringPreferencesKey("Image")
    }
}
//
//val pref = context.getSharedPreferences("PREF2", MODE_PRIVATE)!!
//
//fun saveInf(uid: String) {
//    var link: String =
//        "https://t3.ftcdn.net/jpg/03/46/83/96/360_F_346839683_6nAPzbhpSkIpb8pmAwufkC7c5eD7wYws.jpg"
//    val database: DatabaseReference = FirebaseDatabase.getInstance().getReference("Users")
//        .child(uid)
//    database.addValueEventListener(object : ValueEventListener {
//        override fun onDataChange(snapshot: DataSnapshot) {
//            val user = snapshot.getValue(User::class.java)!!
//            if (user != null) {
//                link = user.linkImage
//            }
//            pref.edit().putString("IMAGE", link).commit()
//            pref.edit().putString("NAME", link).commit()
//        }
//
//        override fun onCancelled(error: DatabaseError) {
//        }
//    }
//    )
//}
//
//fun getImage(): String {
//    var link: String =
//        "https://t3.ftcdn.net/jpg/03/46/83/96/360_F_346839683_6nAPzbhpSkIpb8pmAwufkC7c5eD7wYws.jpg"
//    return pref.getString("IMAGE", link).toString()
//}


//}