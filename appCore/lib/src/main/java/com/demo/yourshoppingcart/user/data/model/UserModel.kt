package com.demo.yourshoppingcart.user.data.model

import com.demo.yourshoppingcart.cart.data.model.CartModel
import com.demo.yourshoppingcart.payment.data.model.PaymentModel
import java.util.UUID
import kotlin.random.Random

abstract class UserModel {
    data class UserResponse(
        val userId: String = "",
        val userPP: String = "",
        val userNum: String = "",
        val userName: String = "",
        val userType: USERTYPE = USERTYPE.GUEST,
        val isLogin: Boolean = false,
        val cart: CartModel.Cart? = null,
        val paymentMethods: List<PaymentModel> = emptyList(),
        val selectedPaymentMethod: String = "",
        val address: List<AddressModel> = emptyList()
    )
}

data class AddressModel(
    val id: String = UUID.randomUUID().toString(),
    val name: String = "",
    val phone: String = "",
    val pinCode: String = "",
    val fullAddress: String = "",
    val city: String = "",
    val state: String = "",
    val type: String = AddressType.HOME.name
)

enum class USERTYPE {
    GUEST,
    LOGGED,
}

enum class AddressType {
    HOME,
    OFFICE,
    OTHER
}