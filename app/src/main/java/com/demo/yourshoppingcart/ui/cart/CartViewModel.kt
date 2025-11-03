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

    // Load existing cart
    fun loadCart(
        productIds: List<String>?,
        cartId: String,
        productQuantity: Map<String, Int>
    ) {
        viewModelScope.launch {
            _viewState.value = _viewState.value.copy(isLoading = true)

            try {
                if (cartId.isNotEmpty()) {
                    // Existing cart
                    when (val cartResult = getCartUseCase.invoke(cartId)) {
                        is Resource.Data<*> -> {
                            val cart = cartResult.value as CartModel.Cart
                            _viewState.value = _viewState.value.copy(isLoading = false, cartData = cart)
                            // Merge new products if any
                            productIds?.let { updateCartItems(cart.cartId, it, productQuantity) }
                        }
                        is Resource.Error -> {
                            _viewState.value = _viewState.value.copy(
                                isLoading = false,
                                errorMessage = cartResult.throwable.message
                            )
                        }
                    }
                } else if (!productIds.isNullOrEmpty()) {
                    // No cart yet → create new cart with these products
                    addProductToCart(productIds, productQuantity)
                } else {
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

    // Add products to a new cart
    private fun addProductToCart(
        productIds: List<String>,
        productQuantities: Map<String, Int>
    ) {
        viewModelScope.launch {
            _viewState.value = _viewState.value.copy(isLoading = true)

            try {
                val fetchedProducts = getProductDetailsUseCase.invoke(productIds)

                if (fetchedProducts is Resource.Data<*>) {
                    val products = fetchedProducts.value as List<HomeModel.Item>
                    val cartItems = products.map { product ->
                        CartModel.CartItem(
                            productId = product.itemId,
                            productName = product.itemName,
                            productPrice = product.itemPrice,
                            productQun = productQuantities[product.itemId] ?: 0,
                            productDes = product.itemDes,
                            productImg = product.itemImg
                        )
                    }

                    val newCart = cartEntity(
                        cartId = UUID.randomUUID().toString(),
                        cartItem = cartItems
                    )

                    when (val result = addCartUseCase.invoke(newCart)) {
                        is Resource.Data<*> -> {
                            _viewState.value = _viewState.value.copy(
                                isLoading = false,
                                cartData = newCart,
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
                } else if (fetchedProducts is Resource.Error) {
                    _viewState.value = _viewState.value.copy(
                        isLoading = false,
                        errorMessage = fetchedProducts.throwable.message
                    )
                }
            } catch (e: Exception) {
                _viewState.value = _viewState.value.copy(
                    isLoading = false,
                    errorMessage = e.message
                )
            }
        }
    }

    // Update the whole cart in Firebase
    private fun updateCart(cartId: String, cart: List<CartModel.CartItem>) {
        viewModelScope.launch {
            _viewState.value = _viewState.value.copy(isLoading = true)
            val result = updateCartItemUseCase.invoke(cartId = cartId, cart = cart)
            when (result) {
                is Resource.Data<*> -> {
                    _viewState.value = _viewState.value.copy(
                        isLoading = false,
                        cartData = CartModel.Cart(cartId = cartId, cartItem = cart)
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

    // Update single product quantity in cart (handles add/remove)
    fun updateCartQuantity(cartId: String?, productId: String, newQuantity: Int) {
        viewModelScope.launch {
            val currentCart = _viewState.value.cartData
            val updatedItems = currentCart?.cartItem?.toMutableList() ?: mutableListOf()

            val index = updatedItems.indexOfFirst { it.productId == productId }

            if (index != -1) {
                if (newQuantity > 0) {
                    updatedItems[index] = updatedItems[index].copy(productQun = newQuantity)
                } else {
                    updatedItems.removeAt(index)
                }
            } else if (newQuantity > 0) {
                // If cart exists, add new item
                val product = getProductDetailsUseCase.invoke(listOf(productId))
                if (product is Resource.Data<*>) {
                    val item = (product.value as List<HomeModel.Item>)[0]
                    updatedItems.add(
                        CartModel.CartItem(
                            productId = item.itemId,
                            productName = item.itemName,
                            productPrice = item.itemPrice,
                            productQun = newQuantity,
                            productDes = item.itemDes,
                            productImg = item.itemImg
                        )
                    )
                }
            }

            if (!cartId.isNullOrEmpty()) {
                // Update existing cart
                _viewState.value = _viewState.value.copy(
                    cartData = currentCart?.copy(cartItem = updatedItems)
                        ?: CartModel.Cart(cartId = cartId, cartItem = updatedItems)
                )
                updateCart(cartId, updatedItems)
            } else if (updatedItems.isNotEmpty()) {
                // No cart yet → create new
                val newCartId = UUID.randomUUID().toString()
                val newCart = CartModel.Cart(cartId = newCartId, cartItem = updatedItems)
                _viewState.value = _viewState.value.copy(cartData = newCart)
                addCartUseCase.invoke(newCart)
            }
        }
    }

    // Checkout (clear cart)
    fun checkout(cartId: String) {
        viewModelScope.launch {
            _viewState.value = _viewState.value.copy(isLoading = true)

            val emptyCart = cartEntity(
                cartId = "",
                cartItem = emptyList()
            )

            when (val result = addCartUseCase.invoke(emptyCart)) {
                is Resource.Data<*> -> {
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

    // Merge new products into existing cart
    private suspend fun updateCartItems(
        cartId: String,
        productIds: List<String>,
        productQuantity: Map<String, Int>
    ) {
        val currentCart = _viewState.value.cartData
        val updatedItems = currentCart?.cartItem?.toMutableList() ?: mutableListOf()

        val fetchedProducts = getProductDetailsUseCase.invoke(productIds)

        if (fetchedProducts is Resource.Data<*>) {
            val products = fetchedProducts.value as List<HomeModel.Item>
            products.forEach { product ->
                val quantity = productQuantity[product.itemId] ?: 0
                val index = updatedItems.indexOfFirst { it.productId == product.itemId }
                if (index >= 0) {
                    updatedItems[index] = updatedItems[index].copy(productQun = quantity)
                } else {
                    updatedItems.add(
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
            updateCart(cartId, updatedItems)
        }
    }
}