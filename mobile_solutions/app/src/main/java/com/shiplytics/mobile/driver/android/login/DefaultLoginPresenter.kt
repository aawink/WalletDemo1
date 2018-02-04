package com.shiplytics.mobile.driver.android.login

import android.content.res.Resources
import android.util.Log
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.shiplytics.mobile.driver.android.R
import javax.inject.Inject

class DefaultLoginPresenter @Inject constructor(private val resources: Resources) : LoginPresenter {

    companion object {
        const val TAG = "DefaultLoginPresenter"
    }

    var loginView: LoginView? = null
    var loginViewModel: LoginViewModel? = null
    var user: FirebaseUser? = null
    private val firebaseAuth: FirebaseAuth = FirebaseAuth.getInstance()

    override fun bindViewModel(loginViewModel: LoginViewModel) {
        this.loginViewModel = loginViewModel
        if (firebaseAuth.currentUser != null) {
            loginViewModel.loginLiveData.value = LoginStateModel(isLoggedIn = true)
        }
    }

    override fun loginOrRegister(userName: String, password: String, confirmPassword: String, isRegister: Boolean) {

        loginViewModel?.loginLiveData?.value
        if (validParams(userName, password)) {
            val newLoginStateModel = LoginStateModel(userName
                    , password
                    , confirmPassword
                    , isRegister
                    , isLoggingIn = true)
            loginViewModel?.loginLiveData?.value = newLoginStateModel

            if (newLoginStateModel.isRegistration) {
                registerUser(newLoginStateModel.userName, newLoginStateModel.password, newLoginStateModel.confirmPassword)
            } else {
                login(newLoginStateModel.userName, newLoginStateModel.password)
            }
        }
    }

    private fun validParams(userName: String, password: String): Boolean = isEmailValid(userName) && isPasswordValid(password)

    private fun registerUser(userName: String, password: String, confirmPassword: String) {

        val loginStateModel = loginViewModel?.loginLiveData?.value
        val loginLiveData = loginViewModel?.loginLiveData
        if (loginLiveData != null && loginStateModel != null && validParams(userName, password)) {
            val newLoginStateModel = LoginStateModel(userName, password, confirmPassword, isLoggingIn = true)
            loginLiveData.value = newLoginStateModel

            if (password == confirmPassword) {
                val task: Task<AuthResult> = firebaseAuth.createUserWithEmailAndPassword(userName, password)
                        .addOnCompleteListener(object : OnCompleteListener<AuthResult> {
                            override fun onComplete(registerTask: Task<AuthResult>) {
                                if (registerTask.isSuccessful) {
                                    Log.d(TAG, " user account created successfuly")
                                    user = registerTask.result.user

                                } else {
                                    Log.w(TAG, " user account creation failed: " + registerTask.exception)
                                }
                                loginLiveData.value = LoginStateModel(isLoggingIn = false)
                            }
                        })
                task.addOnFailureListener({exception -> Log.w(TAG, " exception: " + exception.message)})
            } else {
                // TODO mark model as registration failed.
            }

        }

    }


    private fun login(userName: String, password: String) {
        val loginStateModel = loginViewModel?.loginLiveData?.value
        val loginLiveData = loginViewModel?.loginLiveData
        if (loginLiveData != null && loginStateModel != null && validParams(userName, password)) {
            val newLoginStateModel = LoginStateModel(loginStateModel.userName, loginStateModel.password, isLoggingIn = true)
            loginLiveData.value = newLoginStateModel

            firebaseAuth.signInWithEmailAndPassword(userName, password)
                    .addOnCompleteListener(object : OnCompleteListener<AuthResult> {
                        override fun onComplete(loginTask: Task<AuthResult>) {
                            if (loginTask.isSuccessful) {
                                Log.d(TAG, " user $userName logged in successfuly")
                                user = loginTask.result.user
                                loginLiveData.value = LoginStateModel(userName, isLoggingIn = false, isLoggedIn = true)
                            } else {
                                Log.w(TAG, " user $userName authentication failed: " + loginTask.exception)
                                loginLiveData.value = LoginStateModel(userName, password, isLoggingIn = false)
                            }
                        }

                    })
        }

    }

    override fun bind(view: LoginView) {
        loginView = view
    }

    override fun unbind(view: LoginView) {
        loginView = null
    }

    private fun isEmailValid(email: String): Boolean {
        //TODO: Replace this with your own logic
        return email.contains("@")
    }

    private fun isPasswordValid(password: String): Boolean {
        return password.length >= resources.getInteger(R.integer.minPasswordLength)
    }


}
