package com.demo.yourshoppingcart.framework.firebase

import android.app.Activity
import com.demo.yourshoppingcart.common.network.DocumentApi
import com.demo.yourshoppingcart.home.data.model.HomeModel
import com.demo.yourshoppingcart.product_details.data.model.ProductDetailsModel
import com.demo.yourshoppingcart.user.data.model.USERTYPE
import com.demo.yourshoppingcart.user.data.model.UserModel
import com.google.firebase.FirebaseException
import com.google.firebase.auth.*
import com.google.firebase.firestore.FirebaseFirestore
import dagger.hilt.android.qualifiers.ActivityContext
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlinx.coroutines.tasks.await
import java.util.concurrent.TimeUnit
import javax.inject.Inject
import kotlin.coroutines.resume

class DocumentApiFirebaseImpl @Inject constructor(
    private val firestore: FirebaseFirestore,
    private val auth: FirebaseAuth,
    //private val activityProvider: () -> Activity
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
            itemPrice = data?.getString("itemPrice") ?: ""
        )
    }

    override suspend fun addProductToCart(cart: UserModel.UserCart) {
        val document = auth.currentUser?.uid ?: return
        val cartItem = cart.catItem.map {
            hashMapOf(
                "productId" to it.itemId,
                "productName" to it.itemName,
                "productImg" to it.itemImg,
                "productPrice" to it.price,
                "productQun" to it.itemQun,
                "productDes" to it.itemSec
            )
        }
        val cartData = hashMapOf("cartId" to cart.cartId, "cartItem" to cartItem)
        firestore.collection("user").document(document).update("cart", cartData).await()
    }

    override suspend fun fetchCart(cartId: String): UserModel.UserCart {
        val collection = firestore.collection("user")
            .whereEqualTo("cart.cartId", cartId)
            .get()
            .await()
        val doc = collection.documents.firstOrNull() ?: throw Exception("Cart not found")
        return doc.get("cart") as UserModel.UserCart
    }

    override suspend fun clearCart() {
        val uid = auth.currentUser?.uid ?: return
        firestore.collection("user").document(uid).update("cart", null).await()
    }

    override suspend fun fetchUser(): UserModel.UserResponse {
        val userId = auth.currentUser?.uid ?: throw Exception("User not logged in")
        val document = firestore.collection("user").document(userId).get().await()
        return UserModel.UserResponse(
            userId = document.getString("userId") ?: "",
            userNum = document.getString("userNum") ?: "",
            userType = if (document.getString("userType") == USERTYPE.GUEST.name) USERTYPE.GUEST else USERTYPE.LOGGED,
            isLogin = document.getBoolean("isLogin") ?: false,
            cart = document.get("cart") as? UserModel.UserCart
        )
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
            guestDoc.get("cart") as? UserModel.UserCart
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
       /* return suspendCancellableCoroutine { cont ->
            val options = PhoneAuthOptions.newBuilder(auth)
                .setPhoneNumber(phoneNumber)
                .setTimeout(60L, TimeUnit.SECONDS)
                .setActivity(activityProvider())
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
        }*/
        return ""
    }
}