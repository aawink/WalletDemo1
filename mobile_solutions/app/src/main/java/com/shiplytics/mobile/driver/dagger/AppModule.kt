package com.shiplytics.mobile.driver.dagger

import com.shiplytics.mobile.driver.DriverApplication
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class AppModule(val driverApplication: DriverApplication) {
    @Provides
    @Singleton
    fun provideApp() = driverApplication
}
