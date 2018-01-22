package com.etrade.eo.core.domain

import com.google.gson.annotations.SerializedName

open class BaseAdapterResponse(genericErrors: List<GenericError>? = null) {

    @SerializedName("errors") val errors: List<GenericError>? = genericErrors

    /**
     * Indicates whether or not the response has errors.
     * @return true if the "errors" JSON field is present and or empty. False otherwise.
     */
    fun hasErrors(): Boolean = errors != null

    /**
     * Returns a single error string from the errors structure.
     *
     * The rules for selecting the string to use go as follows:
     *
     * 1. The message will be selected from the first element in the "errors" array.
     * 2. If a "faultInfo" object exists, it's "errorMessage" will be used.
     * 3. If the above does not exist, then the error's "message" will be used.
     * 4. If the above does not exist, then "Unknown error" will be used.
     *
     * @return Single error string extracted from the "errors" array.
     */
    fun extractErrors(): String = extractErrors(this)

    companion object {
        private const val UNKNOWN_ERROR = "Unknown error"

        private fun extractErrors(baseResponse: BaseAdapterResponse): String {
            var error: String = UNKNOWN_ERROR

            if (baseResponse.errors != null && !baseResponse.errors.isEmpty()) {
                val (message, faultInfo) = baseResponse.errors[0]
                message?.let { error = it }
                faultInfo?.errorMessage?.let { error = it }
            }

            return error
        }
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as BaseAdapterResponse

        if (errors != other.errors) return false

        return true
    }

    override fun hashCode(): Int {
        return errors?.hashCode() ?: 0
    }
}

data class GenericError(@SerializedName("message") val message: String?,
                        @SerializedName("faultInfo") val faultInfo: FaultInfo?,
                        @SerializedName("errorType") val errorType: String?,
                        @SerializedName("errorCode") val errorCode: String?) {

    data class FaultInfo(@SerializedName("errorCode") val errorCode: String?,
                         @SerializedName("errorMessage") val errorMessage: String?)
}


open class BaseResponseException: Exception {
    constructor(adapterResponse: BaseAdapterResponse) : super(adapterResponse.extractErrors())
    constructor(adapterResponse: BaseAdapterResponse, cause: Throwable) : super(adapterResponse.extractErrors(), cause)
}
