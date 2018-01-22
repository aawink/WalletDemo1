package com.shiplytics.mobile.network.retrofit;

import java.lang.annotation.Retention;

import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * Used to indicate that the method or parameter this annotates should be serialized using the
 * XML specification.
 * @see com.shiplytics.mobile.network.retrofit.Json
 */
@Retention(RUNTIME)
@interface Xml {
}
