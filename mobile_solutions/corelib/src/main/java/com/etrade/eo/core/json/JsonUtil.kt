
package com.etrade.eo.core.json


import com.google.gson.reflect.TypeToken

import java.io.IOException
import java.io.InputStream
import java.lang.reflect.Type

interface JsonUtil {
    fun serialize(obj: Any?): String

    fun <T> deserialize(jsonString: String?, typeToken: TypeToken<T>): T?

    fun <T> deserialize(jsonString: String?, type: Type): T?

    @Throws(IOException::class)
    fun <T> deserialize(inputStream: InputStream, charset: String, type: Type): T?
}
