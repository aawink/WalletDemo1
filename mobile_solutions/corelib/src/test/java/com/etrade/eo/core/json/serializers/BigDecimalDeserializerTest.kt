package com.etrade.eo.core.json.serializers

import com.google.gson.Gson
import com.google.gson.GsonBuilder

import org.junit.Test

import java.math.BigDecimal

import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull

class BigDecimalDeserializerTest {

    private val gson = createGson()

    @Test
    @Throws(Exception::class)
    fun deserializeWithNumber() {
        val bigDecimal = BigDecimal(4325426.42624)
        val testObj = gson.fromJson(createTestObjectJson(bigDecimal.toPlainString()), TestObject::class.java)
        assertEquals(bigDecimal, testObj.bigDecimal)
    }

    @Test
    @Throws(Exception::class)
    fun deserializeWithString() {
        val bigDecimal = BigDecimal(4325426.42624)
        val testObj = gson.fromJson(createTestObjectJson("\"${bigDecimal.toPlainString()}\""), TestObject::class.java)
        assertEquals(bigDecimal, testObj.bigDecimal)
    }

    @Test
    @Throws(Exception::class)
    fun deserializeWithNull() {
        val testObj = gson.fromJson(createTestObjectJson("null"), TestObject::class.java)
        assertNull(testObj.bigDecimal)
    }

    private fun createGson(): Gson {
        return GsonBuilder()
                .registerTypeAdapter(BigDecimal::class.java, BigDecimalDeserializer())
                .create()
    }

    private fun createTestObjectJson(bigDecimalValue: String): String {
        return "{ \"bigDecimal\": $bigDecimalValue }"
    }

    private inner class TestObject private constructor(val bigDecimal: BigDecimal)
}