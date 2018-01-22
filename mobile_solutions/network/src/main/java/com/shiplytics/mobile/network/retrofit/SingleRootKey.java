package com.shiplytics.mobile.network.retrofit;

import java.lang.annotation.Retention;

import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * This class is designed to assist with Retrofit methods that require request bodies containing
 * one top level key.
 *
 * <p>The purpose was to avoid creating needless classes that are only single-property wrappers for
 * the real substance of a request.</p>
 *
 * <p><b>Usage:</b></p>
 * <pre>
 * {@code
 * interface SomeService {
 *     ResponseObject doTheThingYo(@Body @Json @SingleRootKey("topLevelKeyName") String theValue);
 * }
 * }
 * The code above will produce a request that resembles the following:
 * {@code
 * {
 *     "topLevelKeyName": <theValue>
 * }
 * }
 * </pre>
 */
@Retention(RUNTIME)
public @interface SingleRootKey {
    /**
     * The string used as the JSON property key.
     * @return The top level JSON value key.
     */
    String value();
}
