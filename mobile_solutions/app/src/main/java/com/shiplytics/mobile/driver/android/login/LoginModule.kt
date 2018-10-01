package com.shiplytics.mobile.driver.android.login

import dagger.Binds
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class LoginModule {
    @ContributesAndroidInjector
    abstract fun contributeMainActivityInjector():LoginActivity

    @Binds
    abstract fun bindLoginPresenter(loginPresenter: DefaultLoginPresenter): LoginPresenter
}