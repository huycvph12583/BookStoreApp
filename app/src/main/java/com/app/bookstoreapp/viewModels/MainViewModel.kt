package com.app.bookstoreapp.viewModels

import android.app.Application
import android.view.View
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.app.bookstoreapp.models.UserLogin
import kotlinx.coroutines.launch

class MainViewModel(application: Application) : AndroidViewModel(application) {
    var emailAddress = MutableLiveData<String>()
    var password = MutableLiveData<String>()
    var userMutableLiveData: MutableLiveData<UserLogin>? = null

    fun getUser(): MutableLiveData<UserLogin>? {
        if (userMutableLiveData == null) {
            userMutableLiveData = MutableLiveData<UserLogin>()
        }
        return userMutableLiveData
    }

    fun onClick(view: View) = viewModelScope.launch {
        val loginUser = UserLogin(emailAddress.value, password.value)
        userMutableLiveData!!.value = loginUser
    }
}