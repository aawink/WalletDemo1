package com.shiplytics.mobile.driver.dagger

import com.shiplytics.mobile.driver.DriverApplication
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(modules= arrayOf(AppModule::class))
interface ApplicationComponent {
    fun inject(app:DriverApplication)
}