package com.demo.yourshoppingcart.ui.cart

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.demo.yourshoppingcart.Resource
import com.demo.yourshoppingcart.cart.data.model.CartModel
import com.demo.yourshoppingcart.cart.domain.usecase.AddCartUseCase
import com.demo.yourshoppingcart.cart.domain.usecase.ClearCartUseCase
import com.demo.yourshoppingcart.cart.domain.usecase.GetCartUseCase
import com.demo.yourshoppingcart.cart.domain.usecase.GetProductsUseCase
import com.demo.yourshoppingcart.cart.domain.usecase.UpdateCartUseCase
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
    private val updateCartItemUseCase: UpdateCartUseCase,
    private val clearCart: ClearCartUseCase
) : ViewModel() {
    private val _viewState = MutableStateFlow(CartState())
    val viewState: StateFlow<CartState> = _viewState

    /** Load or add products to cart */
    init {
        loadCart()
    }
    fun loadCart() {
        viewModelScope.launch {
            _viewState.value = _viewState.value.copy(isLoading = true)
            try {
                 val currentCart = getCart()
                //mergeAndSaveCart(currentCart, productIds, productQuantity)
                _viewState.value = _viewState.value.copy(isLoading = false, cartData = currentCart)
            } catch (e: Exception) {
                _viewState.value = _viewState.value.copy(isLoading = false, errorMessage = e.message)
            }
        }
    }

    /** Add, update, or remove a single product */
    fun updateCartQuantity( productId: String, newQuantity: Int) {
        viewModelScope.launch {
            val currentCart = _viewState.value.cartData
            val quantityMap = if (newQuantity > 0) mapOf(productId to newQuantity) else mapOf(productId to 0)
            mergeAndSaveCart(currentCart, listOf(productId), quantityMap)
        }
    }

    /** Checkout (clear cart) */
    fun checkout() {
        viewModelScope.launch {
            val result = clearCart.invoke()
            when(result){
                is Resource.Data<*> -> {
                    _viewState.value = _viewState.value.copy(isLoading = false, cartData = null)
                }
                is Resource.Error -> {
                    _viewState.value = _viewState.value.copy(isLoading = false, errorMessage = result.throwable.message)
                }
            }
        }
    }

    // --- Helper Functions ---

    private suspend fun getCart(): CartModel.Cart? {
        return when (val result = getCartUseCase.invoke()) {
            is Resource.Data<*> -> result.value as CartModel.Cart
            is Resource.Error -> {
                _viewState.value = _viewState.value.copy(isLoading = false, errorMessage = result.throwable.message)
                null
            }
        }
    }

    /**
     * Merge products into cart and save
     * Handles adding, updating, and removing items in one flow
     */
    private suspend fun mergeAndSaveCart(
        currentCart: CartModel.Cart?,
        productIds: List<String>?,
        productQuantity: Map<String, Int>,
    ) {
        val updatedItems = currentCart?.cartItem?.toMutableList() ?: mutableListOf()

        //single productId

        val result = getProductDetailsUseCase.invoke(productIds!!)

        when(result) {
            is Resource.Data<*> -> {
                val products = result.value as List<HomeModel.Item>

                products.map {product ->
                    val index = updatedItems.indexOfFirst { it.productId == product.itemId }
                    val quantity = productQuantity[product.itemId] ?: 0

                    if (index == -1) {
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
                    }else if (index >= 0 && quantity > 0) {
                        updatedItems[index] = updatedItems[index].copy(productQun = quantity)
                    }else {
                        updatedItems.removeAt(index)
                    }
                }
            }
            is Resource.Error -> {
                // Error
            }
        }

        if (currentCart == null && updatedItems.isEmpty()) return

        val finalCart = currentCart?.copy(cartItem = updatedItems)
            ?: CartModel.Cart(UUID.randomUUID().toString(), updatedItems)

        saveCart(finalCart)
    }

    /** Save cart to Firebase (handles new and existing carts) */
    private suspend fun saveCart(cart: CartModel.Cart) {
        _viewState.value = _viewState.value.copy(isLoading = true)

        val result = if (cart.cartId.isNotEmpty()) {
            updateCartItemUseCase.invoke(cart.cartId, cart.cartItem)
        } else {
            addCartUseCase.invoke(cart)
        }

        _viewState.value = when (result) {
            is Resource.Data<*> -> _viewState.value.copy(isLoading = false, cartData = cart, errorMessage = null)
            is Resource.Error -> _viewState.value.copy(isLoading = false, errorMessage = result.throwable.message)
        }
    }
}