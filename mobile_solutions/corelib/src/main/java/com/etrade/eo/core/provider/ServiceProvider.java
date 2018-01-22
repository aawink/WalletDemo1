package com.etrade.eo.core.provider;

/**
 * Created by cmai on 5/1/17.
 */

public interface ServiceProvider {

    /**
     * Start processing requests.
     */
    void start();

    /**
     * Stop processing requests.
     */
    void stop();

    /**
     * Stop processing request and cancel dequeue all existing requests.
     */
    void shutdown();
}
