package com.demo.yourshoppingcart

sealed class Resource<out T> {
    data class Data<T>(val value: T) : Resource<T>()
    data class Error(val throwable: Throwable) : Resource<Nothing>()
}