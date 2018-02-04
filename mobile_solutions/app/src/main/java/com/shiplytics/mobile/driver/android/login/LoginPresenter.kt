package com.shiplytics.mobile.driver.android.login

interface LoginPresenter {

    fun bindViewModel(loginViewModel: LoginViewModel)
    fun bind(view: LoginView)
    fun unbind(view: LoginView)

    fun loginOrRegister(userName: String, password: String, confirmPassword: String = "", isRegister: Boolean = false)

}

interface LoginView {

}
