package com.shiplytics.mobile.driver

import android.app.Application
import com.shiplytics.mobile.driver.dagger.AppModule
import com.shiplytics.mobile.driver.dagger.ApplicationComponent
import com.shiplytics.mobile.driver.dagger.DaggerApplicationComponent

class DriverApplication : Application() {

    val appComponent: ApplicationComponent by lazy {
        DaggerApplicationComponent.builder().appModule(AppModule(this)).build();
    }

    override fun onCreate() {
        super.onCreate()

        appComponent.inject(this)

    }
}