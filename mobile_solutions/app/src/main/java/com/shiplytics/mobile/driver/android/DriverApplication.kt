package com.shiplytics.mobile.driver.android

import android.app.Activity
import android.app.Application
import com.shiplytics.mobile.driver.android.dagger.AppModule
import com.shiplytics.mobile.driver.android.dagger.ApplicationComponent
import com.shiplytics.mobile.driver.android.dagger.DaggerApplicationComponent
import dagger.android.AndroidInjector
import dagger.android.DispatchingAndroidInjector
import dagger.android.HasActivityInjector
import javax.inject.Inject

class DriverApplication : Application(), HasActivityInjector {
    @Inject
    lateinit var androidInjector: DispatchingAndroidInjector<Activity>

    val appComponent: ApplicationComponent by lazy {
        DaggerApplicationComponent.builder().appModule(AppModule(this)).build()
    }

    override fun onCreate() {
        super.onCreate()

        appComponent.inject(this)

    }

    override fun activityInjector(): AndroidInjector<Activity> {
        return androidInjector
    }

}