package com.shiplytics.mobile.driver.android.dagger

import android.content.res.Resources
import com.shiplytics.mobile.driver.android.DriverApplication
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class AppModule(val driverApplication: DriverApplication) {
    @Provides
    @Singleton
    fun provideApp() = driverApplication

    @Provides
    @Singleton
    fun provideResources(): Resources = driverApplication.resources


}
