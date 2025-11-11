package com.demo.yourshoppingcart.framework.firebase

import com.demo.yourshoppingcart.FTAClass
import com.demo.yourshoppingcart.cart.data.model.CartModel
import com.demo.yourshoppingcart.common.network.DocumentApi
import com.demo.yourshoppingcart.home.data.model.HomeModel
import com.demo.yourshoppingcart.product_details.data.model.ProductDetailsModel
import com.demo.yourshoppingcart.user.data.model.USERTYPE
import com.demo.yourshoppingcart.user.data.model.UserModel
import com.google.firebase.FirebaseException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.PhoneAuthCredential
import com.google.firebase.auth.PhoneAuthOptions
import com.google.firebase.auth.PhoneAuthProvider
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlinx.coroutines.tasks.await
import java.util.concurrent.TimeUnit
import javax.inject.Inject
import kotlin.coroutines.resume

class DocumentApiFirebaseImpl @Inject constructor(
    private val firestore: FirebaseFirestore,
    private val auth: FirebaseAuth,
    private val activityProvider: FTAClass
) : DocumentApi {

    override suspend fun fetchCategory(): HomeModel.CategoryResponse {
        val collection = firestore.collection("category").get().await()
        val catData = collection.documents.map { doc ->
            HomeModel.Category(
                catId = doc.getString("catId") ?: "",
                catName = doc.getString("catName") ?: "",
                catImg = doc.getString("catImg") ?: ""
            )
        }
        return HomeModel.CategoryResponse(catData)
    }

    override suspend fun fetchAllItems(): HomeModel.CategoryItemResponse {
        val collection = firestore.collection("categoryItem").get().await()
        val itemData = collection.documents.map { doc ->
            HomeModel.Item(
                itemId = doc.getString("itemId") ?: "",
                itemName = doc.getString("itemName") ?: "",
                itemDes = doc.getString("itemDes") ?: "",
                itemImg = doc.getString("itemImg") ?: "",
                itemPrice = doc.getString("itemPrice") ?: "",
                cat = doc.getString("cat") ?: ""
            )
        }
        return HomeModel.CategoryItemResponse(itemData)
    }

    override suspend fun fetchSelectedCatItem(cat: String): HomeModel.CategoryItemResponse {
        val collection = firestore.collection("categoryItem")
            .whereEqualTo("cat", cat)
            .get()
            .await()
        val itemData = collection.documents.map { doc ->
            HomeModel.Item(
                itemId = doc.getString("itemId") ?: "",
                itemName = doc.getString("itemName") ?: "",
                itemDes = doc.getString("itemDes") ?: "",
                itemImg = doc.getString("itemImg") ?: "",
                itemPrice = doc.getString("itemPrice") ?: "",
                cat = doc.getString("cat") ?: ""
            )
        }
        return HomeModel.CategoryItemResponse(itemData)
    }

    override suspend fun fetchProductDetails(itemId: String): ProductDetailsModel.DetailModel {
        val data = firestore.collection("categoryItem")
            .whereEqualTo("itemId", itemId)
            .get()
            .await()
            .documents
            .firstOrNull()

        return ProductDetailsModel.DetailModel(
            itemId = data?.getString("itemId") ?: "",
            itemName = data?.getString("itemName") ?: "",
            itemImages = data?.get("itemImages") as? List<String> ?: emptyList(),
            itemDescription = data?.getString("itemDes") ?: "",
            itemPrice = data?.getString("itemPrice") ?: "",
            itemImage = data?.getString("itemImg") ?: ""
        )
    }

    override suspend fun addProductToCart(cart: CartModel.Cart) {
        val document = auth.currentUser?.uid ?: return

        val cartItem = cart.cartItem.map {
            hashMapOf(
                "productId" to it.productId,
                "productName" to it.productName,
                "productImg" to it.productImg,
                "productPrice" to it.productPrice,
                "productQun" to it.productQun,
                "productDes" to it.productDes
            )
        }
        val cartData = hashMapOf("cartId" to cart.cartId, "cartItem" to cartItem)
        firestore.collection("user").document(document).update("cart", cartData).await()
    }

    override suspend fun fetchCart(): CartModel.Cart {
        val userId = auth.currentUser?.uid ?: ""
        // Get the user document
        val userDoc = firestore.collection("user")
            .document(userId)
            .get()
            .await()

        if (!userDoc.exists()) throw Exception("User not found")

        // Extract the cart map
        val cartMap = userDoc.get("cart") as? Map<*, *>
            ?: throw Exception("Cart data missing")

        // Convert the map to CartModel.Cart
        return CartModel.Cart(
            cartId = cartMap["cartId"] as? String ?: "",
            cartItem = (cartMap["cartItem"] as? List<Map<String, Any>>)?.map {
                CartModel.CartItem(
                    productId = it["productId"] as? String ?: "",
                    productName = it["productName"] as? String ?: "",
                    productPrice = it["productPrice"] as? String ?: "",
                    productQun = (it["productQun"] as? Long)?.toInt() ?: 0,
                    productDes = it["productDes"] as? String ?: "",
                    productImg = it["productImg"] as? String ?: ""
                )
            } ?: emptyList()
        )
    }

    override suspend fun updateCartItem(
        cartId: String,
        cart: List<CartModel.CartItem>
    ) {
        val uid = auth.currentUser?.uid ?: throw Exception("User not logged in")

        val cartItemList = cart.map { item ->
            hashMapOf(
                "productId" to item.productId,
                "productName" to item.productName,
                "productImg" to item.productImg,
                "productPrice" to item.productPrice,
                "productQun" to item.productQun,
                "productDes" to item.productDes
            )
        }

        val cartData = hashMapOf(
            "cartId" to cartId,
            "cartItem" to cartItemList
        )

        firestore.collection("user")
            .document(uid)
            .update("cart", cartData)
            .await()
    }

    override suspend fun clearCart() {
        val uid = auth.currentUser?.uid ?: return
        firestore.collection("user").document(uid).update("cart", emptyMap<String, Any>()).await()
    }

    override suspend fun fetchUser(): UserModel.UserResponse {
        val userId = auth.currentUser?.uid ?: throw Exception("User not logged in")
        val document = firestore.collection("user").document(userId).get().await()
        return document.toObject(UserModel.UserResponse::class.java) ?: throw Exception("User not found")
    }

    override suspend fun guestLogin(): String {
        val authResult = auth.signInAnonymously().await()
        val uid = authResult.user?.uid ?: throw Exception("Anonymous sign-in failed")
        val userData = hashMapOf(
            "userId" to uid,
            "userType" to USERTYPE.GUEST.name,
            "userNum" to "",
            "isLogin" to true,
            "cart" to null
        )
        firestore.collection("user").document(uid).set(userData).await()
        return uid
    }

    override suspend fun phoneLogin(
        oldGuestId: String?,
        user: UserModel.UserResponse,
        verificationId: String?,
        otp: String?
    ): String {
        if (verificationId.isNullOrEmpty() || otp.isNullOrEmpty()) throw IllegalArgumentException("Verification ID or OTP missing")

        val credential = PhoneAuthProvider.getCredential(verificationId, otp)
        val authResult = auth.signInWithCredential(credential).await()
        val uid = authResult.user?.uid ?: throw Exception("Phone sign-in failed")

        val cartData = if (!oldGuestId.isNullOrEmpty()) {
            val guestDoc = firestore.collection("user").document(oldGuestId).get().await()
            guestDoc.get("cart") as? CartModel.Cart
        } else null

        val userData = hashMapOf(
            "userId" to uid,
            "userNum" to user.userNum,
            "userType" to USERTYPE.LOGGED.name,
            "isLogin" to true,
            "cart" to (cartData ?: user.cart)
        )

        firestore.collection("user").document(uid).set(userData).await()
        if (!oldGuestId.isNullOrEmpty() && oldGuestId != uid) firestore.collection("user").document(oldGuestId).delete().await()

        return uid
    }

    override suspend fun sendOtpFlow(phoneNumber: String): String {
        return suspendCancellableCoroutine { cont ->
            val activity = activityProvider.currentActivity?: throw NullPointerException()
            val options = PhoneAuthOptions.newBuilder(auth)
                .setPhoneNumber(phoneNumber)
                .setTimeout(60L, TimeUnit.SECONDS)
                .setActivity(activity)
                .setCallbacks(object : PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
                    override fun onVerificationCompleted(p0: PhoneAuthCredential) {}
                    override fun onVerificationFailed(e: FirebaseException) {
                        cont.resumeWith(Result.failure(e))
                    }
                    override fun onCodeSent(verId: String, token: PhoneAuthProvider.ForceResendingToken) {
                        cont.resume(verId)
                    }
                })
                .build()

            PhoneAuthProvider.verifyPhoneNumber(options)
        }
    }
}