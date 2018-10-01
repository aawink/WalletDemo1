package com.etrade.eo.core.json

import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.google.gson.stream.JsonReader

import java.io.BufferedReader
import java.io.IOException
import java.io.InputStream
import java.io.InputStreamReader
import java.lang.reflect.Type

import javax.inject.Inject

/**
 * Json serialization utility leveraging Gson.
 */
class GsonJsonUtil @Inject
constructor(private val gson: Gson) : JsonUtil {

    override fun serialize(obj: Any?): String {
        return gson.toJson(obj)
    }

    override fun <T> deserialize(jsonString: String?, typeToken: TypeToken<T>): T? {
        return deserialize(jsonString, typeToken.type)
    }

    override fun <T> deserialize(jsonString: String?, type: Type): T? {
        return gson.fromJson(jsonString, type)
    }

    @Throws(IOException::class)
    override fun <T> deserialize(inputStream: InputStream, charset: String, type: Type): T? {
        val inputStreamReader = InputStreamReader(inputStream, charset)
        val reader = BufferedReader(inputStreamReader)
        val jsonReader = JsonReader(reader)

        return gson.fromJson(jsonReader, type)
    }
}
