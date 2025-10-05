package com.demo.yourshoppingcart.common.network

sealed class AppApiRequest {
    abstract val url: String
    open val queryParams: Map<String, String> = mapOf()

    data class Get(
        override val url: String,
        override val queryParams: Map<String, String> = mapOf(),
    ) : AppApiRequest()

    data class Post<B>(
        override val url: String,
        override val queryParams: Map<String, String> = mapOf(),
        val body: B? = null
    ) : AppApiRequest()
}
