package com.shiplytics.mobile.network.authentication;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;

import javax.inject.Inject;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

public class AuthenticationInterceptor implements Interceptor {
    private static final String HEADER_NAME = "Token";

    private final Authenticator authenticator;

    @Inject
    public AuthenticationInterceptor(Authenticator authenticator) {
        this.authenticator = authenticator;
    }

    @Override
    public Response intercept(@NotNull Chain chain) throws IOException {
        final Request originalRequest = chain.request();
        if (originalRequest.header(HEADER_NAME) != null) {
            return chain.proceed(originalRequest);
        } else {
            return chain.proceed(withHeader(originalRequest,
                    HEADER_NAME,
                    authenticator.getAuthenticationToken()));
        }
    }

    private static Request withHeader(final Request request, final String header, final String value) {
        return request.newBuilder()
                .addHeader(header, value)
                .build();
    }
}
