package com.shiplytics.mobile.network


import com.jakewharton.retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import com.shiplytics.mobile.network.authentication.AuthenticationInterceptor
import com.shiplytics.mobile.network.authentication.Authenticator
import com.shiplytics.mobile.network.retrofit.RetrofitModule
import com.shiplytics.mobile.network.retrofit.ServiceClient

import javax.inject.Singleton

import dagger.Module
import dagger.Provides
import io.reactivex.schedulers.Schedulers
import okhttp3.OkHttpClient
import retrofit2.Converter

@Module(includes = arrayOf(RetrofitModule::class))
object NetworkModule {

    @Provides
    @Singleton
    internal fun providesOkHttpClient(okHttpClientBuilder: OkHttpClient.Builder, authenticator: Authenticator): OkHttpClient {
        return okHttpClientBuilder.addInterceptor(AuthenticationInterceptor(authenticator)).build()
    }


    @Provides
    internal fun provideRetrofitBuilder(converterFactory: Converter.Factory, okHttpClient: OkHttpClient): ServiceClient.Builder {
        return ServiceClient.Builder()
                .okHttpClient(okHttpClient)
                .addTypeAdapter(converterFactory)
                .addCallAdapter(RxJava2CallAdapterFactory.createWithScheduler(Schedulers.io()))
    }
}