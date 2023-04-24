package com.app.bookstoreapp.viewModels

import android.app.Application
import android.view.View
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.app.bookstoreapp.models.UserLogin
class MainViewModel(application: Application) : AndroidViewModel(application) {
    var emailAddress = MutableLiveData<String>()
    var password = MutableLiveData<String>()

    private var userMutableLiveData: MutableLiveData<UserLogin>? = null

    fun getUser(): MutableLiveData<UserLogin>? {
        if (userMutableLiveData == null) {
            userMutableLiveData = MutableLiveData<UserLogin>()
        }
        return userMutableLiveData
    }

    fun onClick(view: View) {
        val loginUser = UserLogin(emailAddress.value, password.value)
        userMutableLiveData!!.value = loginUser
    }
}