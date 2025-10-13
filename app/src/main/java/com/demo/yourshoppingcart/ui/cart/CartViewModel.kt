package com.demo.yourshoppingcart.ui.cart

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.demo.yourshoppingcart.Resource
import com.demo.yourshoppingcart.user.domain.entity.cartEntity
import com.demo.yourshoppingcart.user.domain.usecase.AddCartUseCase
import com.demo.yourshoppingcart.user.domain.usecase.GetCartUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CartViewModel @Inject constructor(
    private val getCartUseCase: GetCartUseCase,
    private val addCartUseCase: AddCartUseCase,
) : ViewModel() {

    private val _viewState = MutableStateFlow(CartState())
    val viewState: StateFlow<CartState> = _viewState

    fun loadCart(productIds: List<String>?,cartId: String) {
        viewModelScope.launch {
            if (productIds.isNullOrEmpty()) {

            }else {
                val result = getCartUseCase.invoke()
            }
            val result = getCartUseCase.invoke(userId = userId)
            when(result){
                is Resource.Data<*> -> {
                    val data = result as cartEntity
                }
                is Resource.Error -> {}
            }
        }
    }

   /* fun addItem(item: UserModel.UserCart) {
        val existing = _cartItems.find { it.itemId == item.itemId }
        if (existing != null) {
            val updated = existing.copy(itemQun = existing.itemQun + 1)
            updateItem(updated)
        } else {
            _cartItems.add(item.copy(itemQun = 1))
        }
        saveCartState()
    }

    fun removeItem(itemId: String) {
        val existing = _cartItems.find { it.itemId == itemId } ?: return
        if (existing.itemQun > 1) {
            val updated = existing.copy(itemQun = existing.itemQun - 1)
            updateItem(updated)
        } else {
            _cartItems.remove(existing)
        }
        saveCartState()
    }

    fun setItemQuantity(itemId: String, quantity: Int) {
        val existing = _cartItems.find { it.itemId == itemId } ?: return
        if (quantity > 0) {
            val updated = existing.copy(itemQun = quantity)
            updateItem(updated)
        } else {
            _cartItems.remove(existing)
        }
        saveCartState()
    }

    fun getQuantity(itemId: String): Int {
        return _cartItems.find { it.itemId == itemId }?.itemQun ?: 0
    }

    fun getTotalPrice(): Int {
        return _cartItems.sumOf {
            val price = it.price.toIntOrNull() ?: 0
            price * it.itemQun
        }
    }

    private fun updateItem(updated: UserModel.UserCart) {
        val index = _cartItems.indexOfFirst { it.itemId == updated.itemId }
        if (index != -1) {
            _cartItems[index] = updated
        }
    }

    private fun saveCartState() {
        viewModelScope.launch {
            addCartUseCase.invoke(_cartItems)
        }
    }*/
}
