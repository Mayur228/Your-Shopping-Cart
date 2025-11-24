package com.demo.yourshoppingcart.payment.data.model

import java.util.UUID

sealed class PaymentModel {

    abstract val id: String
    abstract val type: String

    data class Upi(
        override val id: String = UUID.randomUUID().toString(),
        val upiId: String
    ) : PaymentModel() {
        override val type: String = "UPI"
    }

    data class Card(
        override val id: String = UUID.randomUUID().toString(),
        val cardHolderName: String,
        val cardNumber: Long,
        val cvv: Int,
        val expiryDate: String,
        val nickName: String?
    ) : PaymentModel() {
        override val type: String = "Card"
    }

    object COD : PaymentModel() {
        override val id: String = "COD"
        override val type: String = "COD"
    }

    companion object {
        fun fromMap(map: Map<String, Any>): PaymentModel? {
            return when (map["type"] as? String) {
                "COD" -> COD
                "UPI" -> (map["upiId"] as? String)?.let {
                    Upi(id = map["id"] as? String ?: UUID.randomUUID().toString(), upiId = it)
                }
                "Card" -> {
                    val cardHolderName = map["cardHolderName"] as? String ?: return null
                    val cardNumber = (map["cardNumber"] as? Long) ?: return null
                    val cvv = (map["cvv"] as? Long)?.toInt() ?: return null
                    val expiryDate = map["expiryDate"] as? String ?: return null
                    val nickName = map["nickName"] as? String
                    Card(
                        id = map["id"] as? String ?: UUID.randomUUID().toString(),
                        cardHolderName = cardHolderName,
                        cardNumber = cardNumber,
                        cvv = cvv,
                        expiryDate = expiryDate,
                        nickName = nickName
                    )
                }
                else -> null
            }
        }
    }
}