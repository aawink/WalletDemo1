package com.etrade.eo.core.preferences

/**
 * A simple interface for storing basic types by string key
 */
interface KeyValueStorage {
    fun getStringValue(key: String, defaultValue: String? = ""): String?

    /**
     * @param key - it is strongly recommended to prefix all keys with a package name to avoid key clashes
     * @param value - nullable string value
     */
    fun putStringValue(key: String, value: String?)

    fun getIntValue(key: String, defaultValue: Int = 0): Int

    /**
     * @param key - it is strongly recommended to prefix all keys with a package name to avoid key clashes
     * @param value - any integer value
     */
    fun putIntValue(key: String, value: Int)

    fun getBooleanValue(key: String, defaultValue: Boolean? = false): Boolean?

    /**
     * @param key - it is strongly recommended to prefix all keys with a package name to avoid key clashes
     * @param value - any boolean value
     */
    fun putBooleanValue(key: String, value: Boolean?)

}
