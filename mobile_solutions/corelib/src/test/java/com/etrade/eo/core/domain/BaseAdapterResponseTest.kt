package com.etrade.eo.core.domain

import com.google.common.testing.EqualsTester
import com.google.gson.Gson
import org.junit.Assert.*
import org.junit.Test

class BaseAdapterResponseTest {

    private val gson = Gson()

    @Test
    fun getErrors() {
        val errors = listOf(createGenericError())
        val response = BaseAdapterResponse(errors)
        assertEquals(errors, response.errors)
    }

    @Test
    fun hasErrors() {
        val response = BaseAdapterResponse(listOf(createGenericError(), createGenericError()))
        assertTrue(response.hasErrors())

        val emptyListResponse = BaseAdapterResponse(listOf())
        assertTrue(emptyListResponse.hasErrors())
    }

    @Test
    fun extractErrors() {
        val responseEmptyErrors = BaseAdapterResponse(emptyList())
        assertEquals(responseEmptyErrors.extractErrors(), "Unknown error")

        val errWithFault = createGenericError()
        val response = BaseAdapterResponse(listOf(errWithFault))
        assertEquals(response.extractErrors(), errWithFault.faultInfo?.errorMessage)

        val errNoFault = createGenericErrorNoFaultInfo()
        val responseNoFaultInfo = BaseAdapterResponse(listOf(errNoFault))
        assertEquals(responseNoFaultInfo.extractErrors(), errNoFault.message)

        val noMsg = createGenericErrorNoMessage()
        val responseNoMsg = BaseAdapterResponse(listOf(noMsg))
        assertEquals(responseNoMsg.extractErrors(), noMsg.faultInfo?.errorMessage)

        val noMsgs = createGenericErrorNoMessages()
        val responseNoMsgs = BaseAdapterResponse(listOf(noMsgs))
        assertEquals(responseNoMsgs.extractErrors(), "Unknown error")

        val noErrorsJson = BaseAdapterResponseTest::class.java.getResource("base_response_no_errors.json").readText()
        val noErrors = gson.fromJson(noErrorsJson, BaseAdapterResponse::class.java)
        assertFalse(noErrors.hasErrors())
        assertEquals(noErrors.extractErrors(), "Unknown error")
    }

    @Test
    fun baseResponseException() {
        val response = BaseAdapterResponse(listOf(createGenericError()))
        try {
            throw BaseResponseException(response)
        } catch (e: BaseResponseException) {
            assertEquals(e.message, response.extractErrors())
        }

        val cause = NullPointerException("test")
        try {
            throw BaseResponseException(response, cause)
        } catch (e: BaseResponseException) {
            assertEquals(e.cause, cause)
        }
    }

    @Test
    fun equalsContract() {
        EqualsTester()
                .addEqualityGroup(BaseAdapterResponse(listOf(createGenericError())))
                .addEqualityGroup(BaseAdapterResponse(listOf(createGenericErrorNoFaultInfo())))
                .addEqualityGroup(BaseAdapterResponse(listOf(createGenericErrorNoMessages())))
                .testEquals()
    }

    private fun createGenericError(): GenericError {
        return GenericError("message",
                GenericError.FaultInfo("fault_error_code", "fault_error_message"),
                "error_type",
                "error_code")
    }

    private fun createGenericErrorNoFaultInfo(): GenericError {
        return GenericError("message",
                null,
                "error_type",
                "error_code")
    }

    private fun createGenericErrorNoMessage(): GenericError {
        return GenericError(null,
                GenericError.FaultInfo("fault_error_code", "fault_error_message"),
                "error_type",
                "error_code"
        )
    }

    private fun createGenericErrorNoMessages(): GenericError {
        return GenericError(null,
                GenericError.FaultInfo("fault_error_code", null),
                "error_type",
                "error_code"
        )
    }
}