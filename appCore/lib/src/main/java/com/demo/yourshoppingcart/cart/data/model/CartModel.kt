package com.demo.yourshoppingcart.cart.data.model

class CartModel {

    data class Cart(
        val cartId: String = "",
        val cartItem: List<CartItem> = emptyList()
    ) {
        companion object {
            fun fromMap(map: Map<String, Any>): Cart {
                val cartId = map["cartId"] as? String ?: ""

                val cartItemsList = map["cartItem"] as? List<Map<String, Any>> ?: emptyList()
                val cartItems = cartItemsList.map { itemMap ->
                    CartItem(
                        productId = itemMap["productId"] as? String ?: "",
                        productName = itemMap["productName"] as? String ?: "",
                        productPrice = itemMap["productPrice"] as? String ?: "",
                        productQun = (itemMap["productQun"] as? Long)?.toInt() ?: 0,
                        productDes = itemMap["productDes"] as? String ?: "",
                        productImg = itemMap["productImg"] as? String ?: ""
                    )
                }
                return Cart(cartId = cartId, cartItem = cartItems)
            }
        }
    }

    data class CartItem(
        val productId: String = "",
        val productName: String = "",
        val productPrice: String = "",
        val productQun: Int = 0,
        val productDes: String = "",
        val productImg: String = ""
    )
}
