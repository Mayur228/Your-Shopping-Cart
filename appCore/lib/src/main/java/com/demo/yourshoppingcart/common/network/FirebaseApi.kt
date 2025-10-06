package com.demo.yourshoppingcart.common.network

interface DocumentApi {
    suspend  fun fetchAllData(): List<Map<String, String>>
    suspend fun fetchSingleData(): Map<String, String>
}