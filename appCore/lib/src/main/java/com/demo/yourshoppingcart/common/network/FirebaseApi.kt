package com.demo.yourshoppingcart.common.network

import com.demo.yourshoppingcart.cart.data.model.CartModel
import com.demo.yourshoppingcart.coupon.data.model.Coupon
import com.demo.yourshoppingcart.home.data.model.HomeModel
import com.demo.yourshoppingcart.payment.data.model.PaymentModel
import com.demo.yourshoppingcart.product_details.data.model.ProductDetailsModel
import com.demo.yourshoppingcart.user.data.model.AddressModel
import com.demo.yourshoppingcart.user.data.model.UserModel
import kotlinx.coroutines.flow.Flow

interface DocumentApi {
    suspend fun fetchCategory(): HomeModel.CategoryResponse
    suspend fun fetchAllItems(): HomeModel.CategoryItemResponse
    suspend fun fetchSelectedCatItem(cat: String): HomeModel.CategoryItemResponse
    suspend fun fetchProductDetails(itemId: String): ProductDetailsModel.DetailModel
    suspend fun addProductToCart(cart: CartModel.Cart)
    suspend fun fetchCart(): CartModel.Cart
    suspend fun updateCartItem(cartId: String,cart: List<CartModel.CartItem>)
    suspend fun clearCart()
    suspend fun isNewUser(): Boolean
    suspend fun fetchUser(): UserModel.UserResponse
    suspend fun updateUser(userId: String,user: UserModel.UserResponse)
    suspend fun guestLogin(): String
    suspend fun phoneLogin(oldGuestId: String? = null, user: UserModel.UserResponse, verificationId: String? = null, otp: String? = null): String
    suspend fun sendOtpFlow(phoneNumber: String): String
    suspend fun addPaymentMethod(method: PaymentModel): String
    suspend fun getPaymentMethod(): List<PaymentModel>
    suspend fun updatePaymentMethod(paymentMethodId: String, updatedMethod: PaymentModel): String
    suspend fun deletePaymentMethod(paymentMethodId: String): String
    suspend fun selectedPaymentMethod(id: String): Unit
    suspend fun getSelectedPaymentMethod(): String
    suspend fun uploadDataToFirestore() //Use only for data upload when need to avoid manual data addition
    suspend fun getCoupons(): List<Coupon>
    suspend fun applyCoupon(id: String)
    suspend fun removeCoupon(id: String)
    suspend fun addAddress(address: AddressModel)
    suspend fun updateAddress(id: String,address: AddressModel)
    suspend fun deleteAddress(id: String)
}