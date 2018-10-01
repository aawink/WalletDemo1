package com.shiplytics.mobile.driver.android.login

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.content.pm.PackageManager
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.text.TextUtils
import android.view.View
import android.view.inputmethod.EditorInfo
import android.widget.AutoCompleteTextView
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import com.shiplytics.mobile.driver.android.R
import dagger.android.AndroidInjection
import kotlinx.android.synthetic.main.activity_login.*
import javax.inject.Inject

/**
 * A login screen that offers login via email/password.
 */
class LoginActivity : AppCompatActivity() {

    companion object {
        /**
         * Id to identity READ_CONTACTS permission request.
         */
        private val REQUEST_READ_CONTACTS = 0
    }

    @Inject
    lateinit var loginPresenter: LoginPresenter

    private lateinit var loginViewModel: LoginViewModel
    private val loginLiveDataObserver = createLoginLiveDateObserver()

    private fun createLoginLiveDateObserver(): Observer<LoginStateModel> {

        return Observer { loginStateModel ->
            if (loginStateModel != null) {
                showProgress(loginStateModel.isLoggingIn)
                if (loginStateModel.isLoggedIn) {
                    Toast.makeText(this, "Login Successful", Toast.LENGTH_SHORT).show()
                } else if (loginStateModel.loginError.isNotEmpty()) {
                    Toast.makeText(this, loginStateModel.loginError, Toast.LENGTH_SHORT).show()
                } else if (loginStateModel.userNameError.isNotEmpty()) {
                    email.error = loginStateModel.userNameError
                    email.requestFocus()
                } else if (loginStateModel.passwordError.isNotEmpty()) {
                    etPassword.error = loginStateModel.passwordError
                    etPassword.requestFocus()
                } else if (loginStateModel.passwordMismatch.isNotEmpty()) {
                    etPasswordConfirm.error = "Passwords Must Match"
                    etPasswordConfirm.requestFocus()
                }
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Dagger injection
        AndroidInjection.inject(this)
        setContentView(R.layout.activity_login)
        // Set up the login form.

        etPassword!!.setOnEditorActionListener(TextView.OnEditorActionListener { textView, id, keyEvent ->
            if (id == EditorInfo.IME_ACTION_DONE || id == EditorInfo.IME_NULL) {
                attemptLogin()
                return@OnEditorActionListener true
            }
            false
        })

        chkRegister.setOnClickListener { view ->
            if (chkRegister.isChecked) {
                email_sign_in_button.setText(R.string.action_register)
                etPasswordConfirm.visibility = View.VISIBLE
            } else {
                email_sign_in_button.setText(R.string.action_sign_in_short)
                etPasswordConfirm.visibility = View.GONE
            }
        }

        val mEmailSignInButton = findViewById<Button>(R.id.email_sign_in_button)
        mEmailSignInButton.setOnClickListener { attemptLogin() }

        loginViewModel = ViewModelProviders.of(this).get(LoginViewModel::class.java)
    }

    /**
     * Callback received when a permissions request has been completed.
     */
    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>,
                                            grantResults: IntArray) {
        if (requestCode == REQUEST_READ_CONTACTS) {
            if (grantResults.size == 1 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            }
        }
    }


    /**
     * Attempts to sign in or register the account specified by the login form.
     * If there are form errors (invalid email, missing fields, etc.), the
     * errors are presented and no actual login attempt is made.
     */
    private fun attemptLogin() {
        loginPresenter.loginOrRegister(email.text.toString(), etPassword.text.toString(), etPasswordConfirm.text.toString(), chkRegister.isChecked)
    }

    /**
     * Shows the progress UI and hides the login form.
     */
    private fun showProgress(show: Boolean) {
        val shortAnimTime = resources.getInteger(android.R.integer.config_shortAnimTime)

        login_form.visibility = if (show) View.GONE else View.VISIBLE
        login_form.animate().setDuration(shortAnimTime.toLong()).alpha(
                (if (show) 0 else 1).toFloat()).setListener(object : AnimatorListenerAdapter() {
            override fun onAnimationEnd(animation: Animator) {
                login_form.visibility = if (show) View.GONE else View.VISIBLE
            }
        })

        login_progress.visibility = if (show) View.VISIBLE else View.GONE
        login_progress.animate().setDuration(shortAnimTime.toLong()).alpha(
                (if (show) 1 else 0).toFloat()).setListener(object : AnimatorListenerAdapter() {
            override fun onAnimationEnd(animation: Animator) {
                login_progress.visibility = if (show) View.VISIBLE else View.GONE
            }
        })
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle?) {
        super.onRestoreInstanceState(savedInstanceState)
        if (chkRegister.isChecked) {
            email_sign_in_button.setText(R.string.action_register)
            etPasswordConfirm.visibility = View.VISIBLE
        }
    }

    override fun onStart() {
        super.onStart()

        loginViewModel.loginLiveData.observe(this, loginLiveDataObserver)
        loginPresenter.bindViewModel(loginViewModel)
    }


}