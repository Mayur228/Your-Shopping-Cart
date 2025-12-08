package com.demo.yourshoppingcart.order.data.source

import com.demo.yourshoppingcart.Resource
import com.demo.yourshoppingcart.common.network.DocumentApi
import com.demo.yourshoppingcart.order.data.model.OrderModel
import org.koin.core.annotation.Factory

interface OrderSource {
    suspend fun addOrderHistory(order: OrderModel): Resource<Unit>
    suspend fun getOrderHistory(): Resource<List<OrderModel>>
    suspend fun getOrderDetails(id: String): Resource<OrderModel>
}

@Factory
class OrderSourceImpl(private val documentApi: DocumentApi): OrderSource{
    override suspend fun addOrderHistory(order: OrderModel): Resource<Unit> {
        return try {
            val data = documentApi.addOrdersHistory(order = order)
            Resource.Data(data)
        }catch (e: Exception) {
            Resource.Error(e)
        }
    }

    override suspend fun getOrderHistory(): Resource<List<OrderModel>> {
        return try {
            val data = documentApi.fetchOrderHistory()
            Resource.Data(data)
        }catch (e: Exception) {
            Resource.Error(e)
        }
    }

    override suspend fun getOrderDetails(id: String): Resource<OrderModel> {
        return try {
            val data = documentApi.fetchOrderDetails(id = id)
            Resource.Data(data)
        }catch (e: Exception) {
            Resource.Error(e)
        }
    }
}