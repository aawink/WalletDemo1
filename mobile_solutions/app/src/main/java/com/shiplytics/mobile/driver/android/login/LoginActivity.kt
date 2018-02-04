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

    // UI references.
    private var mEmailView: AutoCompleteTextView? = null
    private var mProgressView: View? = null
    private var mLoginFormView: View? = null
    private lateinit var loginViewModel: LoginViewModel
    private val loginLiveDataObserver = createLoginLiveDateObserver()

    private fun createLoginLiveDateObserver(): Observer<LoginStateModel> {

        return Observer { loginStateModel ->
            if (loginStateModel != null) {
                showProgress(loginStateModel.isLoggingIn)
                if (loginStateModel.isLoggedIn) {
                    Toast.makeText(this, "Login Successful", Toast.LENGTH_SHORT).show()
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
        mEmailView = findViewById(R.id.email)

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

        mLoginFormView = findViewById(R.id.login_form)
        mProgressView = findViewById(R.id.login_progress)

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

    private fun oldAttemptLogin() {

        // Reset errors.
        mEmailView!!.error = null
        etPassword!!.error = null

        // Store values at the time of the login attempt.
        val email = mEmailView!!.text.toString()
        val password = etPassword!!.text.toString()

        var cancel = false
        var focusView: View? = null

        // Check for a valid password, if the user entered one.
        if (!TextUtils.isEmpty(password) && !isPasswordValid(password)) {
            etPassword!!.error = getString(R.string.error_invalid_password)
            focusView = etPassword
            cancel = true
        }

        // Check for a valid email address.
        if (TextUtils.isEmpty(email)) {
            mEmailView!!.error = getString(R.string.error_field_required)
            focusView = mEmailView
            cancel = true
        } else if (!isEmailValid(email)) {
            mEmailView!!.error = getString(R.string.error_invalid_email)
            focusView = mEmailView
            cancel = true
        }

        if (cancel) {
            // There was an error; don't attempt login and focus the first
            // form field with an error.
            focusView!!.requestFocus()
        } else {
            // Show a progress spinner, and kick off a background task to
            // perform the user login attempt.
            showProgress(true)
        }
    }

    private fun isEmailValid(email: String): Boolean {
        return email.contains("@")
    }

    private fun isPasswordValid(password: String): Boolean {
        return password.length >= resources.getInteger(R.integer.minPasswordLength)
    }

    /**
     * Shows the progress UI and hides the login form.
     */
    private fun showProgress(show: Boolean) {
        val shortAnimTime = resources.getInteger(android.R.integer.config_shortAnimTime)

        mLoginFormView!!.visibility = if (show) View.GONE else View.VISIBLE
        mLoginFormView!!.animate().setDuration(shortAnimTime.toLong()).alpha(
                (if (show) 0 else 1).toFloat()).setListener(object : AnimatorListenerAdapter() {
            override fun onAnimationEnd(animation: Animator) {
                mLoginFormView!!.visibility = if (show) View.GONE else View.VISIBLE
            }
        })

        mProgressView!!.visibility = if (show) View.VISIBLE else View.GONE
        mProgressView!!.animate().setDuration(shortAnimTime.toLong()).alpha(
                (if (show) 1 else 0).toFloat()).setListener(object : AnimatorListenerAdapter() {
            override fun onAnimationEnd(animation: Animator) {
                mProgressView!!.visibility = if (show) View.VISIBLE else View.GONE
            }
        })
    }

    override fun onStart() {
        super.onStart()

        loginViewModel.loginLiveData.observe(this, loginLiveDataObserver)
        loginPresenter.bindViewModel(loginViewModel)
    }

    override fun onPause() {
        super.onPause()
    }

    override fun onStop() {
        super.onStop()
    }


}