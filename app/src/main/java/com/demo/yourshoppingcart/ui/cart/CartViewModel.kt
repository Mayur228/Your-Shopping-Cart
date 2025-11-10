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
    private val _viewState = MutableStateFlow(CartState())
    val viewState: StateFlow<CartState> = _viewState

    init {
        loadCart()
    }

    fun loadCart() {
        viewModelScope.launch {
            when (val result = getCartUseCase.invoke()) {
                is Resource.Data<CartModel.Cart> -> {
                    _viewState.value =
                        _viewState.value.copy(isLoading = false, cartData = result.value)
                }

                is Resource.Error -> {
                    _viewState.value = _viewState.value.copy(
                        isLoading = false,
                        errorMessage = result.throwable.message
                    )
                }
            }
        }
    }

    /** Add, update, or remove a single product */
    fun createOrUpdateCart(productId: String, quantity: Int, cartItem: cartItemEntity? = null) {
        viewModelScope.launch {
            val currentCart = _viewState.value.cartData
            val newCartItem = currentCart?.cartItem?.toMutableList() ?: mutableListOf()
            val index = newCartItem.indexOfFirst { it.productId == productId }

            if (index == -1 && cartItem != null) {
                newCartItem.add(cartItem)
            } else if (index >= 0 && quantity > 0) {
                newCartItem[index] = newCartItem[index].copy(productQun = quantity)
            } else {
                newCartItem.removeAt(index)
            }

            val finalData = currentCart?.copy(cartItem = newCartItem) ?: cartEntity(cartId = UUID.randomUUID().toString(),cartItem = newCartItem)

            saveCart(finalData)
        }
    }

    /** Checkout (clear cart) */
    fun checkout() {
        viewModelScope.launch {
            val result = clearCart.invoke()
            when (result) {
                is Resource.Data<*> -> {
                    _viewState.value = _viewState.value.copy(isLoading = false, cartData = null)
                }

                is Resource.Error -> {
                    _viewState.value = _viewState.value.copy(
                        isLoading = false,
                        errorMessage = result.throwable.message
                    )
                }
            }
        }
    }

    /** Save cart to Firebase (handles new and existing carts) */
    private suspend fun saveCart(cart: CartModel.Cart) {

        val result = if (cart.cartId.isNotEmpty()) {
            updateCartItemUseCase.invoke(cart.cartId, cart.cartItem)
        } else {
            addCartUseCase.invoke(cart)
        }

        _viewState.value = when (result) {
            is Resource.Data<*> -> _viewState.value.copy(
                isLoading = false,
                cartData = cart,
                errorMessage = null
            )

            is Resource.Error -> _viewState.value.copy(
                isLoading = false,
                errorMessage = result.throwable.message
            )
        }

    }
}