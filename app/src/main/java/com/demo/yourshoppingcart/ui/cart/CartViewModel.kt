package com.demo.yourshoppingcart.ui.cart

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.demo.yourshoppingcart.Resource
import com.demo.yourshoppingcart.common.QuantityViewModel
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
    private val quantityViewModel: QuantityViewModel
) : ViewModel() {

    private val _viewState = MutableStateFlow(CartState())
    val viewState: StateFlow<CartState> = _viewState

    fun loadCart(productIds: List<String>?, cartId: String) {
        viewModelScope.launch {
            _viewState.value = _viewState.value.copy(isLoading = true)

            if (productIds.isNullOrEmpty()) {
                when (val result = getCartUseCase(cartId)) {
                    is Resource.Data<*> -> {
                        val cart = result.value as cartEntity
                        _viewState.value = _viewState.value.copy(
                            isLoading = false,
                            cartData = cart
                        )

                        // Update quantities in local QuantityViewModel
                        cart.catItem.forEach {
                            quantityViewModel.setQuantity(it.itemId, it.itemQun)
                        }
                    }

                    is Resource.Error -> {
                        _viewState.value = _viewState.value.copy(
                            isLoading = false,
                            errorMessage = result.throwable.message
                        )
                    }
                }
            } else {
                addProductToCart(productIds, cartId)
            }
        }
    }

    fun addProductToCart(productIds: List<String>, cartId: String) {
        viewModelScope.launch {
            _viewState.value = _viewState.value.copy(isLoading = true)

            val currentCart = _viewState.value.cartData
            val updatedCartItems = currentCart?.catItem?.toMutableList() ?: mutableListOf()

            productIds.forEach { id ->
                val quantity = quantityViewModel.getQuantity(id)
                if (quantity > 0) {
                    val existingItem = updatedCartItems.find { it.itemId == id }
                    if (existingItem != null) {
                        val updatedItem = existingItem.copy(itemQun = quantity)
                        updatedCartItems[updatedCartItems.indexOf(existingItem)] = updatedItem
                    } else {
                        updatedCartItems.add(
                            com.demo.yourshoppingcart.user.data.model.UserModel.CartItem(
                                itemId = id,
                                itemName = "",
                                price = "",
                                itemQun = quantity,
                                itemSec = "",
                                itemImg = ""
                            )
                        )
                    }
                }
            }

            val updatedCart = cartEntity(
                cartId = cartId,
                catItem = updatedCartItems
            )

            when (val result = addCartUseCase(updatedCart)) {
                is Resource.Data<*> -> {
                    _viewState.value = _viewState.value.copy(
                        isLoading = false,
                        cartData = updatedCart,
                        errorMessage = null
                    )
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
    fun checkout(cartId: String) {
        viewModelScope.launch {
            _viewState.value = _viewState.value.copy(isLoading = true)

            val emptyCart = cartEntity(
                cartId = cartId,
                catItem = emptyList()
            )

            when (val result = addCartUseCase(emptyCart)) {
                is Resource.Data<*> -> {
                    quantityViewModel.reset()
                    _viewState.value = _viewState.value.copy(
                        isLoading = false,
                        cartData = emptyCart,
                        errorMessage = null
                    )
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
}