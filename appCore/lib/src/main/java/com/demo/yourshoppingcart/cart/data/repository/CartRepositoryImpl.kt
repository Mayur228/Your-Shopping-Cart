package com.demo.yourshoppingcart.cart.data.repository

import com.demo.yourshoppingcart.Resource
import com.demo.yourshoppingcart.cart.data.model.CartModel
import com.demo.yourshoppingcart.cart.data.source.CartSource
import com.demo.yourshoppingcart.cart.domain.repository.CartRepository
import com.demo.yourshoppingcart.cart.domain.entity.cartEntity
import com.demo.yourshoppingcart.home.data.model.HomeModel
import org.koin.core.annotation.Factory

@Factory
class CartRepositoryImpl(private val source: CartSource) : CartRepository {
    override suspend fun addCart(cart: CartModel.Cart): Resource<Unit> {
        return try {
            val data = source.addCart(cart = cart)
            print(cart)
            Resource.Data(Unit)
        }catch (e: Exception) {
            Resource.Error(e)
        }
    }

    override suspend fun getCart(cartId: String): Resource<cartEntity> {
        return try {
            val data = source.getCart(cartId = cartId)
            Resource.Data(data)
        }catch (e: Exception) {
            Resource.Error(e)
        }
    }

  /*  override suspend fun getUser(): Resource<UserModel.UserResponse> {
        return try {
            val data = source.getUser()
            Resource.Data(data)
        } catch (e: Exception) {
            Resource.Error(e)
        }
    }*/

    override suspend fun fetchProducts(productids: List<String>): Resource<List<HomeModel.Item>> {
        return try {
            val data = source.fetchProducts(
                productids
            )
            Resource.Data(data)
        } catch (e: Exception) {
            Resource.Error(e)
        }
    }

    override suspend fun updateCart(
        cartId: String,
        cart: List<CartModel.CartItem>
    ): Resource<String> {
        return try {
            val data = source.updateCart(
                cartId = cartId,
                cart = cart
            )
            Resource.Data("Updated")
        } catch (e: Exception) {
            Resource.Error(e)
        }
    }
}