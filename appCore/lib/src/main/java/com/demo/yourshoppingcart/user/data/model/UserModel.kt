package com.demo.yourshoppingcart.user.data.model

import com.demo.yourshoppingcart.cart.data.model.CartModel
import com.demo.yourshoppingcart.payment.data.model.PaymentModel

abstract class UserModel {
    data class UserResponse(
        val userId: String = "",
        val userNum: String = "",
        val userType: USERTYPE = USERTYPE.GUEST,
        val isLogin: Boolean = false,
        val cart: CartModel.Cart? = null,
        val paymentMethods: List<PaymentModel> = emptyList(),
        val selectedPaymentMethod: String = ""
    )
   /* data class UserCart(
        val cartId: String,
        val catItem: List<CartItem>
    )

    data class CartItem(
        val itemId: String,
        val itemName: String,
        val price: String,
        val itemQun: Int,
        val itemSec: String,
        val itemImg: String
    )*/
}

enum class USERTYPE {
    GUEST,
    LOGGED,
}