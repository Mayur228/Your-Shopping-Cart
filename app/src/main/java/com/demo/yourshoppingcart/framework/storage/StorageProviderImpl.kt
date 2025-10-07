package com.demo.yourshoppingcart.framework.storage

import android.content.Context
import androidx.core.content.edit
import com.demo.yourshoppingcart.common.StorageKeys
import javax.inject.Inject

class StorageProviderImpl @Inject constructor(
    private val context: Context,
) : StorageProvider {

    private val sharedPreferences by lazy {
        context.getSharedPreferences("app_preference", Context.MODE_PRIVATE)
    }

    override fun putString(
        key: StorageKeys,
        value: String
    ) {
        sharedPreferences.edit {
            putString(key.name, value)
        }
    }

    override fun getString(key: StorageKeys): String? {
        return sharedPreferences.getString(key.name, null)
    }

    override fun putBoolean(
        key: StorageKeys,
        value: Boolean
    ) {
        sharedPreferences.edit {
            putBoolean(key.name, value)
        }
    }

    override fun getBoolean(key: StorageKeys): Boolean? {
        return sharedPreferences.getBoolean(key.name, false)
    }

    override fun clearAll() {
        sharedPreferences.edit { clear() }
    }
}