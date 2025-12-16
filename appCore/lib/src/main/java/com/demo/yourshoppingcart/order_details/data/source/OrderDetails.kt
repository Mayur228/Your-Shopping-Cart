package com.demo.yourshoppingcart.order_details.data.source

import com.demo.yourshoppingcart.Resource
import com.demo.yourshoppingcart.common.network.DocumentApi
import com.demo.yourshoppingcart.order.data.model.OrderModel
import org.koin.core.annotation.Factory

interface OrderDetails {
    suspend fun getOrderDetails(id: String): Resource<OrderModel>
}

@Factory
class OrderDetailsImpl(private val api: DocumentApi): OrderDetails {
    override suspend fun getOrderDetails(id: String): Resource<OrderModel> {
        return try {
            val data = api.fetchOrderDetails(id = id)
            Resource.Data(data)
        }catch (e: Exception) {
            Resource.Error(e)
        }
    }
}