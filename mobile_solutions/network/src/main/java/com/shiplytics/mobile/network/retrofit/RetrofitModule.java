package com.shiplytics.mobile.network.retrofit;

import com.google.gson.Gson;

import dagger.Module;
import dagger.Provides;
import okhttp3.CookieJar;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Converter;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.simplexml.SimpleXmlConverterFactory;

@Module
public class RetrofitModule {

    @Provides
    Converter.Factory provideConverterFactory(Gson gson) {
        final AnnotatedTypeConverter annotatedTypeConverter = new AnnotatedTypeConverter(GsonConverterFactory.create(gson),
                SimpleXmlConverterFactory.create());
        return new SingleKeyRequestConverter(annotatedTypeConverter, gson);
    }

    @Provides
    OkHttpClient.Builder provideOkHttpClientBuilder(CookieJar cookieJar) {
        final HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
        logging.setLevel(HttpLoggingInterceptor.Level.BODY);

        return new OkHttpClient.Builder()
                .cookieJar(cookieJar)
                .addInterceptor(logging);

    }

}
