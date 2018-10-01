package com.etrade.eo.core.preferences

/**
 * Class to keep back-end environment address
 */
internal class AppEnvironmentException(override val message: String, override val cause: Throwable? = null) : Exception(message, cause)

private const val HASH_CODE_PRIME_SEED = 31

class AppEnvironment(val serviceConnectionProtocol: String?
                     , val serviceConnectionAddress: String
                     , val serviceConnectionBaseDomain: String?
                     , private val serviceConnectionPort: String?) {

    val url: String
        get() {
            val url = appendConnectionProtocol(StringBuilder())
            url.append(serviceConnectionAddress)
            return appendConnectionPort(url).toString()
        }

    fun constructUrlWithSubDomain(subDomain: String): String {
        val url = appendConnectionProtocol(StringBuilder())
                .append(subDomain)
                .append('.')
                .append(serviceConnectionBaseDomain)
        return appendConnectionPort(url).toString()
    }

    private fun appendConnectionProtocol(urlStringBuilder: StringBuilder): StringBuilder {
        return if (!serviceConnectionProtocol!!.isEmpty()) {
            urlStringBuilder
                    .append(serviceConnectionProtocol)
                    .append(PROTOCOL_POSTFIX)
        } else {
            urlStringBuilder
        }
    }

    private fun appendConnectionPort(urlStringBuilder: StringBuilder): StringBuilder {
        return if (!serviceConnectionPort!!.isEmpty()) {
            urlStringBuilder
                    .append(':')
                    .append(serviceConnectionPort)
        } else {
            urlStringBuilder
        }
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other == null || javaClass != other.javaClass) return false

        val that = other as AppEnvironment?

        if (if (serviceConnectionProtocol != null) serviceConnectionProtocol != that!!.serviceConnectionProtocol else that!!.serviceConnectionProtocol != null)
            return false
        return if (serviceConnectionAddress != that.serviceConnectionAddress) false
        else !if (serviceConnectionPort != null) serviceConnectionPort != that.serviceConnectionPort
        else that.serviceConnectionPort != null

    }

    override fun hashCode(): Int {
        var result = serviceConnectionProtocol?.hashCode() ?: 0
        result = HASH_CODE_PRIME_SEED * result + serviceConnectionAddress.hashCode()
        result = HASH_CODE_PRIME_SEED * result + (serviceConnectionPort?.hashCode() ?: 0)
        result = HASH_CODE_PRIME_SEED * result + (serviceConnectionBaseDomain?.hashCode() ?: 0)
        return result
    }

    companion object {

        private val HTTP_PROTOCOL = "http"
        private val PROTOCOL_POSTFIX = "://"
        private val DOT = "\\."
        private val INVALID_ADDRESS_STRING = "Invalid Address String"
        private const val NO_PROTOCOL_NO_PORT = 1
        private const val NO_PROTOCOL_OR_NO_PORT = 2
        private const val HAS_PROTOCOL_AND_PORT = 3

        @JvmStatic
        fun parseConnection(connectionSpec: String): AppEnvironment {
            val split = nullToEmpty(connectionSpec).split(":".toRegex()).dropLastWhile({ it.isEmpty() }).toTypedArray()
            val connectionAddress: String
            when (split.size) {
                NO_PROTOCOL_NO_PORT -> {
                    connectionAddress = removeSlashes(split[0])
                    return AppEnvironment("", connectionAddress, parseServiceConnectionBaseDomain(connectionAddress), "")
                }
                NO_PROTOCOL_OR_NO_PORT -> if (connectionSpec.contains(HTTP_PROTOCOL)) {
                    connectionAddress = removeSlashes(split[1])
                    return AppEnvironment(split[0], connectionAddress, parseServiceConnectionBaseDomain(connectionAddress), "")
                } else {
                    connectionAddress = removeSlashes(split[0])
                    return AppEnvironment("", connectionAddress, parseServiceConnectionBaseDomain(connectionAddress), split[1])
                }
                HAS_PROTOCOL_AND_PORT -> {
                    connectionAddress = removeSlashes(split[1])
                    return AppEnvironment(split[0], connectionAddress, parseServiceConnectionBaseDomain(connectionAddress), split[2])
                }
                else -> throw AppEnvironmentException(INVALID_ADDRESS_STRING + ": " + connectionSpec)
            }
        }

        private fun parseServiceConnectionBaseDomain(connectionAddress: String?): String {
            val domain = StringBuilder()
            val split = nullToEmpty(connectionAddress).split(DOT.toRegex()).dropLastWhile({ it.isEmpty() }).toTypedArray()
            if (split.size > 2) {
                //If there are at least 3 splits, there is a sub-domain present
                for (i in 1 until split.size - 1) {
                    domain.append(split[i])
                    domain.append('.')
                }
                domain.append(split[split.size - 1])
                return domain.toString()
            }
            return nullToEmpty(connectionAddress)
        }

        private fun nullToEmpty(possiblyNullString: String?): String = possiblyNullString ?: ""


        private fun removeSlashes(text: String): String {
            return text.replace("/".toRegex(), "")
        }
    }
}

