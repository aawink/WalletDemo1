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
        user = firebaseAuth.currentUser
        if (firebaseAuth.currentUser != null) {
            loginViewModel.loginLiveData.value = LoginStateModel(isLoggedIn = true)
        }
    }

    override fun loginOrRegister(userName: String, password: String, confirmPassword: String, isRegister: Boolean) {
        firebaseAuth.signOut();
        loginViewModel?.loginLiveData?.value =

                if (!isEmailValid(userName)) {
                    LoginStateModel(userName
                            , password
                            , confirmPassword
                            , isRegister
                            , userNameError = "Invalid e-mail.  User name must be a valid e-mail address");

                } else if (!isPasswordValid(password)) {
                    LoginStateModel(userName
                            , password
                            , confirmPassword
                            , isRegister
                            , passwordError = "Password must be at least " + resources.getInteger(R.integer.minPasswordLength) + " characters long")

                } else {
                    if (isRegister) {
                        registerUser(userName, password, confirmPassword)
                    } else {
                        login(userName, password)
                    }
                }
    }

    private fun registerUser(userName: String, password: String, confirmPassword: String): LoginStateModel {
        return if (password == confirmPassword) {
            val task: Task<AuthResult> = firebaseAuth.createUserWithEmailAndPassword(userName, password)
                    .addOnCompleteListener(object : OnCompleteListener<AuthResult> {
                        override fun onComplete(registerTask: Task<AuthResult>) {
                            loginViewModel?.loginLiveData?.value = if (registerTask.isSuccessful) {
                                Log.d(TAG, " user account created successfuly")
                                user = registerTask.result.user
                                LoginStateModel(isLoggingIn = false)
                            } else {
                                Log.w(TAG, " user account creation failed: " + registerTask.exception)
                                LoginStateModel(isLoggingIn = false, loginError = registerTask.exception?.message
                                        ?: "")
                            }

                        }
                    })
            task.addOnFailureListener({ exception -> Log.w(TAG, " exception: " + exception.message) })
            LoginStateModel(userName, password, confirmPassword, isRegistration = true, isLoggingIn = true)
        } else {
            LoginStateModel(userName, password, confirmPassword, isRegistration = true, isLoggingIn = false, passwordMismatch = "Passwords must match")
        }
    }


    private fun login(userName: String, password: String): LoginStateModel {

        firebaseAuth.signInWithEmailAndPassword(userName, password)
                .addOnCompleteListener(object : OnCompleteListener<AuthResult> {
                    override fun onComplete(loginTask: Task<AuthResult>) {
                        loginViewModel?.loginLiveData?.value =
                                if (loginTask.isSuccessful) {
                                    Log.d(TAG, " user $userName logged in successfuly")
                                    user = loginTask.result.user
                                    LoginStateModel(userName, isLoggingIn = false, isLoggedIn = true)
                                } else {
                                    Log.w(TAG, " user $userName authentication failed: " + loginTask.exception)
                                    LoginStateModel(userName, password, isLoggingIn = false, loginError = loginTask.exception?.message
                                            ?: "")

                                }
                    }

                })
        return LoginStateModel(userName, password, isLoggingIn = true)

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
