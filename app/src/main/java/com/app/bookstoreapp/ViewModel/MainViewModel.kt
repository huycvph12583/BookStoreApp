package com.app.bookstoreapp.ViewModel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.app.bookstoreapp.DataStore.DataStoreManager
import kotlinx.coroutines.launch

class MainViewModel(application: Application) : AndroidViewModel(application) {

    private val reposition = DataStoreManager(application)

    fun readData(): String {
        var value1 = ""
       viewModelScope.launch {
            reposition.mData.collect() { value ->
                if (!value.isNullOrEmpty()) {
                    value1 = value
                }
            }
        }
        return value1
    }

    fun saveData(link: String) {
        viewModelScope.launch {
            reposition.insertData(link)
        }

    }

}