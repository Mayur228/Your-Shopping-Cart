package com.demo.yourshoppingcart.ui.cart

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.demo.yourshoppingcart.Resource
import com.demo.yourshoppingcart.cart.data.model.CartModel
import com.demo.yourshoppingcart.cart.domain.entity.cartEntity
import com.demo.yourshoppingcart.cart.domain.entity.cartItemEntity
import com.demo.yourshoppingcart.cart.domain.usecase.AddCartUseCase
import com.demo.yourshoppingcart.cart.domain.usecase.ClearCartUseCase
import com.demo.yourshoppingcart.cart.domain.usecase.GetCartUseCase
import com.demo.yourshoppingcart.cart.domain.usecase.UpdateCartUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import java.util.UUID
import javax.inject.Inject

@HiltViewModel
class CartViewModel @Inject constructor(
    private val getCartUseCase: GetCartUseCase,
    private val addCartUseCase: AddCartUseCase,
    private val updateCartItemUseCase: UpdateCartUseCase,
    private val clearCart: ClearCartUseCase
) : ViewModel() {

    private val _viewState = MutableStateFlow<CartState>(CartState.Loading)
    val viewState: StateFlow<CartState> = _viewState

    init {
        loadCart()
    }

    /** Load cart */
    fun loadCart() {
        viewModelScope.launch {
            _viewState.value = CartState.Loading
            when (val result = getCartUseCase.invoke()) {
                is Resource.Data<CartModel.Cart> -> {
                    _viewState.value = CartState.Success(result.value)
                }
                is Resource.Error -> {
                    _viewState.value = CartState.Error(result.throwable.message ?: "Failed to load cart")
                }
            }
        }
    }

    /** Add / update / remove a single product */
    fun createOrUpdateCart(productId: String, quantity: Int, cartItem: cartItemEntity? = null) {
        val currentCart = (_viewState.value as? CartState.Success)?.cartEntity
            ?: cartEntity(cartId = UUID.randomUUID().toString(), cartItem = listOf(cartItem!!))

        val updatedItems = currentCart.cartItem.toMutableList()
        val index = updatedItems.indexOfFirst { it.productId == productId }

        when {
            index == -1 && cartItem != null -> updatedItems.add(cartItem)
            index >= 0 && quantity > 0 -> updatedItems[index] = updatedItems[index].copy(productQun = quantity)
            index >= 0 && quantity <= 0 -> updatedItems.removeAt(index)
        }

        val updatedCart = currentCart.copy(cartItem = updatedItems)
        _viewState.value = CartState.Success(updatedCart)

        viewModelScope.launch {
            val result = if (updatedCart.cartId.isNotEmpty()) {
                updateCartItemUseCase.invoke(updatedCart.cartId, updatedCart.cartItem)
            } else {
                addCartUseCase.invoke(updatedCart)
            }

            if (result is Resource.Error) {
                _viewState.value = CartState.Error(result.throwable.message ?: "Failed to update cart")
                loadCart()
            }
        }
    }

    /** Clear cart (checkout) */
    fun checkout() {
        viewModelScope.launch {
            when (val result = clearCart.invoke()) {
                is Resource.Data<*> -> _viewState.value = CartState.Success(cartEntity(cartId = "", cartItem = mutableListOf()))
                is Resource.Error -> _viewState.value = CartState.Error(result.throwable.message ?: "Failed to clear cart")
            }
        }
    }
}