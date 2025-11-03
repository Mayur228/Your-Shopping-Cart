package com.demo.yourshoppingcart.ui.cart

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.demo.yourshoppingcart.Resource
import com.demo.yourshoppingcart.cart.data.model.CartModel
import com.demo.yourshoppingcart.cart.domain.usecase.AddCartUseCase
import com.demo.yourshoppingcart.cart.domain.usecase.GetCartUseCase
import com.demo.yourshoppingcart.cart.domain.usecase.GetProductsUseCase
import com.demo.yourshoppingcart.cart.domain.usecase.UpdateCartUseCase
import com.demo.yourshoppingcart.cart.domain.entity.cartEntity
import com.demo.yourshoppingcart.home.data.model.HomeModel
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
    private val getProductDetailsUseCase: GetProductsUseCase,
    private val updateCartItemUseCase: UpdateCartUseCase
) : ViewModel() {

    private val _viewState = MutableStateFlow(CartState())
    val viewState: StateFlow<CartState> = _viewState

    fun loadCart(
        productIds: List<String>?,
        cartId: String,
        productQuantity: Map<String, Int>
    ) {
        viewModelScope.launch {
            _viewState.value = _viewState.value.copy(isLoading = true)

            try {
                // CASE 1: Existing cart + new product IDs => merge
                if (cartId.isNotEmpty() && !productIds.isNullOrEmpty()) {
                    when (val existingResult = getCartUseCase.invoke(cartId)) {
                        is Resource.Data<*> -> {
                            val existingCart = existingResult.value as CartModel.Cart
                            val existingItems = existingCart.cartItem.toMutableList()

                            // Fetch product details for new items
                            when (val fetchedProducts = getProductDetailsUseCase.invoke(productIds)) {
                                is Resource.Data<*> -> {
                                    val products = fetchedProducts.value as List<HomeModel.Item>

                                    products.forEach { product ->
                                        val quantity = productQuantity[product.itemId] ?: 0
                                        val existingIndex =
                                            existingItems.indexOfFirst { it.productId == product.itemId }

                                        if (existingIndex >= 0) {
                                            // Update quantity
                                            val existing = existingItems[existingIndex]
                                            existingItems[existingIndex] = existing.copy(
                                                productQun = existing.productQun + quantity
                                            )
                                        } else {
                                            // Add new item
                                            existingItems.add(
                                                CartModel.CartItem(
                                                    productId = product.itemId,
                                                    productName = product.itemName,
                                                    productPrice = product.itemPrice,
                                                    productQun = quantity,
                                                    productDes = product.itemDes,
                                                    productImg = product.itemImg
                                                )
                                            )
                                        }
                                    }

                                    // Update the cart on Firestore
                                    updateCart(
                                        cartId = cartId,
                                        cart = existingItems
                                    )
                                }

                                is Resource.Error -> {
                                    _viewState.value = _viewState.value.copy(
                                        isLoading = false,
                                        errorMessage = fetchedProducts.throwable.message
                                    )
                                }
                            }
                        }

                        is Resource.Error -> {
                            _viewState.value = _viewState.value.copy(
                                isLoading = false,
                                errorMessage = existingResult.throwable.message
                            )
                        }
                    }
                }

                // CASE 2: Existing cart but no new product IDs => just load existing data
                else if (cartId.isNotEmpty() && productIds.isNullOrEmpty()) {
                    when (val cartResult = getCartUseCase.invoke(cartId)) {
                        is Resource.Data<*> -> {
                            val cart = cartResult.value as CartModel.Cart
                            _viewState.value = _viewState.value.copy(
                                isLoading = false,
                                cartData = cart
                            )
                        }

                        is Resource.Error -> {
                            _viewState.value = _viewState.value.copy(
                                isLoading = false,
                                errorMessage = cartResult.throwable.message
                            )
                        }
                    }
                }

                // CASE 3: No cart yet => create new one
                else if (cartId.isEmpty() && !productIds.isNullOrEmpty()) {
                    addProductToCart(productIds, productQuantity)
                }

                // CASE 4: Nothing to do
                else {
                    _viewState.value = _viewState.value.copy(isLoading = false)
                }
            } catch (e: Exception) {
                _viewState.value = _viewState.value.copy(
                    isLoading = false,
                    errorMessage = e.message
                )
            }
        }
    }

    fun addProductToCart(
        productIds: List<String>,
        productQuantities: Map<String, Int>
    ) {
        viewModelScope.launch {
            _viewState.value = _viewState.value.copy(isLoading = true)

            try {
                val currentCart = _viewState.value.cartData
                val updatedCartItems = currentCart?.cartItem?.toMutableList() ?: mutableListOf()

                val fetchedProducts = getProductDetailsUseCase.invoke(productIds)

                when (fetchedProducts) {
                    is Resource.Data<*> -> {
                        val products = fetchedProducts.value as List<HomeModel.Item>

                        products.forEach { product ->
                            val quantity = productQuantities[product.itemId] ?: 0

                            updatedCartItems.add(
                                CartModel.CartItem(
                                productId = product.itemId,
                                productName = product.itemName,
                                productPrice = product.itemPrice,
                                productQun = quantity,
                                productDes = product.itemDes,
                                productImg = product.itemImg
                            )
                        )
                        }
                    }
                    is Resource.Error -> {
                        _viewState.value = _viewState.value.copy(
                            isLoading = false,
                            errorMessage = fetchedProducts.throwable.message
                        )
                    }
                }

                val updatedCart = cartEntity(
                    cartId = UUID.randomUUID().toString(),
                    cartItem = updatedCartItems
                )

                Log.e("CHECK",updatedCart.toString())

                when (val result = addCartUseCase.invoke(updatedCart)) {
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

            } catch (e: Exception) {
                _viewState.value = _viewState.value.copy(
                    isLoading = false,
                    errorMessage = e.message
                )
            }
        }
    }

    fun updateCart(
        cartId: String,
        cart: List<CartModel.CartItem>,
    ) {
        viewModelScope.launch {
            _viewState.value = _viewState.value.copy(isLoading = true)
            val result = updateCartItemUseCase.invoke(cartId = cartId, cart = cart)
            when (result) {
                is Resource.Data<*> -> {
                    //val cart = result.value as String
                    _viewState.value = _viewState.value.copy(
                        isLoading = false,
                        cartData = CartModel.Cart(
                            cartId = cartId,
                            cartItem = cart
                        )
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
                cartItem = emptyList()
            )

            when (val result = addCartUseCase(emptyCart)) {
                is Resource.Data<*> -> {
                    //quantityViewModel.reset()
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