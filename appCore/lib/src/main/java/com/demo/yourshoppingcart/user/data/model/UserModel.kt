package com.demo.yourshoppingcart.user.data.model

abstract class UserModel {

    data class UserResponse(
        val userId: String,
        val userNum: String,
        val userType: USERTYPE,
        val isLogin: Boolean,
        val cart: UserCart?
    )

    data class UserCart(
        val itemId: String,
        val itemName: String,
        val price: String,
        val itemQun: Int,
        val itemSec: String,
        val itemImg: String
    )
}

enum class USERTYPE {
    GUEST,
    LOGGED
}