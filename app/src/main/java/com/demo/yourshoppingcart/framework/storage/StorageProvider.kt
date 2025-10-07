package com.demo.yourshoppingcart.framework.storage

import com.demo.yourshoppingcart.common.StorageKeys

interface StorageProvider {
    fun putString(key: StorageKeys, value: String)
    fun getString(key: StorageKeys): String?

    fun putBoolean(key: StorageKeys, value: Boolean)

    fun getBoolean(key: StorageKeys): Boolean?
    fun clearAll()
}