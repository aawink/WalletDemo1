package com.shiplytics.mobile.network.authentication;

import android.support.annotation.AnyThread;

@AnyThread
public interface Authenticator {
    String getAuthenticationToken();
}
