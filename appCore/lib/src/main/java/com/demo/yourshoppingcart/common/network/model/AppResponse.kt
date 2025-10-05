package com.demo.yourshoppingcart.common.network.model

import kotlinx.serialization.InternalSerializationApi
import kotlinx.serialization.Serializable

@InternalSerializationApi @Serializable
data class AppRawResponse<T>(
    val success: Boolean,
    val message: String? = null,
    val data: T? = null
) {
    fun asResponse(httpStatusCode: Int): AppResponse<T> {
        return if (success) {
            if (data != null) {
                AppResponse.Data(httpStatusCode, message, data)
            } else {
                AppResponse.Empty(httpStatusCode, message)
            }
        } else {
            AppResponse.Error(httpStatusCode, message)
        }
    }
}

/**
 * parse app response
 */
sealed class AppResponse<out T>(
    val httpStatusCode: Int,
    val message: String?
) {

    data class Data<T>(
        private val _code: Int,
        private val _message: String?,
        val data: T
    ) : AppResponse<T>(_code, _message)

    data class Error(
        private val _code: Int,
        private val _message: String?
    ) : AppResponse<Nothing>(_code, _message)

    data class Empty(
        private val _code: Int,
        private val _message: String?,
    ) : AppResponse<Nothing>(_code, _message)
}
