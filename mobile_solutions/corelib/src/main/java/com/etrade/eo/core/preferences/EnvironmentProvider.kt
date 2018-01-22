package com.etrade.eo.core.preferences

/**
 * An interface for generic read/wirte access to locally stored application settings and
 * a few particular settings like secure mode and environment configuration
 */
interface EnvironmentProvider {

    val currentEnv: ConfigEnvironment

    val defaultEnv: ConfigEnvironment

    val isDefaultEnv: Boolean

    fun setEnvironment(environment: ConfigEnvironment)


}
