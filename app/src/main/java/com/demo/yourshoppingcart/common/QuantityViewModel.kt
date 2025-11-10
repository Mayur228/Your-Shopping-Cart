/*
package com.demo.yourshoppingcart.common

import androidx.compose.runtime.mutableStateMapOf
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class QuantityViewModel @Inject constructor() : ViewModel() {
    private val _quantities = mutableStateMapOf<String, Int>()
    val quantities: Map<String, Int> get() = _quantities

    fun getQuantity(productId: String): Int = _quantities[productId] ?: 0

    fun increaseQuantity(productId: String) {
        val current = _quantities[productId] ?: 0
        _quantities[productId] = current + 1
    }

    fun decreaseQuantity(productId: String) {
        val current = _quantities[productId] ?: 0
        if (current > 1) {
            _quantities[productId] = current - 1
        } else {
            _quantities.remove(productId)
        }
    }

    fun setQuantity(productId: String, quantity: Int) {
        if (quantity <= 0) _quantities.remove(productId)
        else _quantities[productId] = quantity
    }

    fun setQuantities(newQuantities: Map<String, Int>) {
        _quantities.clear()
        _quantities.putAll(newQuantities)
    }

    fun reset() {
        _quantities.clear()
    }
}
*/
