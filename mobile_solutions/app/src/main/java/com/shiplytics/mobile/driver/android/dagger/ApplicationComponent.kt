package com.shiplytics.mobile.driver.android.dagger

import com.shiplytics.mobile.driver.android.DriverApplication
import com.shiplytics.mobile.driver.android.login.LoginModule
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(modules= arrayOf(AppModule::class, LoginModule::class))
interface ApplicationComponent {
    fun inject(app:DriverApplication)
}