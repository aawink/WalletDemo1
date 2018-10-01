package com.shiplytics.mobile.network.retrofit;

import okhttp3.OkHttpClient;
import retrofit2.CallAdapter;
import retrofit2.Converter;
import retrofit2.Retrofit;

/**
 * Constructs {@link Retrofit.Builder} objects.
 *
 * <p>If OkHttpClient Builder is provided it will be used to construct http client otherwise the default
 * okHttpClient will be used.  HttpClient or HttpClient builder must be provided.
 */
public class ServiceClient {


    public static final class Builder {
        private final Retrofit.Builder retrofitBuilder = new Retrofit.Builder();
        private OkHttpClient okHttpClient;
        private OkHttpClient.Builder okHttpClientBuilder;

        /**
         * Constructs a Retrofit client builder.
         *
         * @return Retrofit builder
         */
        public Retrofit.Builder build() {
            if (okHttpClientBuilder != null) {
                okHttpClient = okHttpClientBuilder.build();
            }

            return retrofitBuilder.client(okHttpClient);
        }


        public Builder addCallAdapter(CallAdapter.Factory factory) {
            retrofitBuilder.addCallAdapterFactory(factory);
            return this;
        }

        public Builder addTypeAdapter(Converter.Factory converter) {
            retrofitBuilder.addConverterFactory(converter);
            return this;
        }

        public Builder okHttpClient(OkHttpClient okHttpClient) {
            this.okHttpClient = okHttpClient;
            return this;
        }

        public Builder okHttpClientBuilder(OkHttpClient.Builder builder) {
            this.okHttpClientBuilder = builder;
            return this;
        }
    }
}
