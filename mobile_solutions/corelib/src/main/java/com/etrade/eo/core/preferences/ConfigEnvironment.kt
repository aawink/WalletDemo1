package com.etrade.eo.core.preferences

data class ConfigEnvironment(val name: String, val serviceEnvironment: AppEnvironment) {

    val url: String
        get() = serviceEnvironment.url

}
