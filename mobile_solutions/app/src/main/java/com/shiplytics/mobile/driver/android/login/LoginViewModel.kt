package com.shiplytics.mobile.driver.android.login

import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel

class LoginViewModel : ViewModel() {
    val loginLiveData: LoginLiveData = LoginLiveData()
}

class LoginLiveData : MutableLiveData<LoginStateModel>() {

}

data class LoginStateModel(var userName: String = ""
                           , var password: String = ""
                           , val confirmPassword: String = ""
                           , val isRegistration: Boolean = false
                           , val isLoggingIn: Boolean = false
                           , val isLoggedIn: Boolean = false
                           , val userNameError: String = ""
                           , val passwordError: String = ""
                           , val passwordMismatch: String = ""
                           , val loginError: String = "")
