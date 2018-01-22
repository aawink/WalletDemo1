package com.shiplytics.mobile.network.retrofit;

import java.lang.annotation.Retention;

import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * Used to indicate that the method or parameter this annotates should be serialized using the
 * JSON specification.
 * @see com.shiplytics.mobile.network.retrofit.Xml
 */
@Retention(RUNTIME)
@interface Json {
}
