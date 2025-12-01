package com.demo.yourshoppingcart.framework.firebase

import com.demo.yourshoppingcart.FTAClass
import com.demo.yourshoppingcart.cart.data.model.CartModel
import com.demo.yourshoppingcart.common.network.DocumentApi
import com.demo.yourshoppingcart.coupon.data.model.Coupon
import com.demo.yourshoppingcart.home.data.model.HomeModel
import com.demo.yourshoppingcart.payment.data.model.PaymentModel
import com.demo.yourshoppingcart.product_details.data.model.ProductDetailsModel
import com.demo.yourshoppingcart.user.data.model.AddressModel
import com.demo.yourshoppingcart.user.data.model.AddressType
import com.demo.yourshoppingcart.user.data.model.USERTYPE
import com.demo.yourshoppingcart.user.data.model.UserModel
import com.google.firebase.FirebaseException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.PhoneAuthCredential
import com.google.firebase.auth.PhoneAuthOptions
import com.google.firebase.auth.PhoneAuthProvider
import com.google.firebase.firestore.FieldValue.arrayRemove
import com.google.firebase.firestore.FieldValue.arrayUnion
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.getField
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlinx.coroutines.tasks.await
import java.util.UUID
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

    override suspend fun isNewUser(): Boolean {
        val userId = auth.currentUser?.uid ?: throw Exception("User not logged in")
        val document = firestore.collection("user").document(userId).get().await()
        if (!document.exists()) throw Exception("User not found")
        val userType = document.getString("userType")?.let { USERTYPE.valueOf(it) } ?: USERTYPE.GUEST


        return !(userType == USERTYPE.GUEST || userType == USERTYPE.LOGGED)
    }

    override suspend fun fetchUser(): UserModel.UserResponse {
        val userId = auth.currentUser?.uid ?: throw Exception("User not logged in")
        val document = firestore.collection("user").document(userId).get().await()

        if (!document.exists()) throw Exception("User not found")

        val userName = document.getString("userName") ?: ""
        val userPP = document.getString("userPP") ?: ""
        val userNum = document.getString("userNum") ?: ""
        val userType = document.getString("userType")?.let { USERTYPE.valueOf(it) } ?: USERTYPE.GUEST
        val isLogin = document.getBoolean("isLogin") ?: false
        val selectedPaymentMethod = document.getString("selectedPaymentMethod") ?: PaymentModel.COD.id

        val cart = document.get("cart")?.let { CartModel.Cart.fromMap(it as Map<String, Any>) }

        val paymentList = document.get("paymentMethods") as? List<Map<String, Any>> ?: emptyList()
        val paymentMethods = paymentList.mapNotNull { PaymentModel.fromMap(it) }

        val addressList = document.get("address") as? List<Map<String, Any>> ?: emptyList()

        val addresses = addressList.map { map ->
            AddressModel(
                id = map["id"] as? String ?: "",
                name = map["name"] as? String ?: "",
                phone = map["phone"] as? String ?: "",
                pinCode = map["pinCode"] as? String ?: "",
                fullAddress = map["fullAddress"] as? String ?: "",
                city = map["city"] as? String ?: "",
                state = map["state"] as? String ?: "",
                type = (map["type"] as? String) ?: AddressType.HOME.name
            )
        }

        return UserModel.UserResponse(
            userId = userId,
            userName = userName,
            userPP = userPP,
            userNum = userNum,
            userType = userType,
            isLogin = isLogin,
            cart = cart,
            paymentMethods = paymentMethods,
            selectedPaymentMethod = selectedPaymentMethod,
            address = addresses
        )
    }

    override suspend fun updateUser(userId: String, user: UserModel.UserResponse) {

        val paymentMethodsList = user.paymentMethods.map { payment ->

            when (payment) {

                is PaymentModel.Card -> mapOf(
                    "id" to payment.id,
                    "type" to "card",
                    "cardNumber" to payment.cardNumber,
                    "expiry" to payment.expiryDate,
                    "holderName" to payment.cardHolderName
                )

                is PaymentModel.Upi -> mapOf(
                    "id" to payment.id,
                    "type" to "upi",
                    "upiId" to payment.upiId
                )

                else -> emptyMap()
            }
        }

        val addressList = user.address.map { addr ->
            mapOf(
                "id" to addr.id,
                "name" to addr.name,
                "phone" to addr.phone,
                "pinCode" to addr.pinCode,
                "fullAddress" to addr.fullAddress,
                "city" to addr.city,
                "state" to addr.state,
                "type" to addr.type
            )
        }

        val cartMap = user.cart?.let { cart ->
            mapOf(
                "cartItem" to cart.cartItem.map { item ->
                    mapOf(
                        "productId" to item.productId,
                        "productName" to item.productName,
                        "productDes" to item.productDes,
                        "productPrice" to item.productPrice,
                        "productImg" to item.productImg,
                        "productQun" to item.productQun
                    )
                }
            )
        }

        val userMap = mapOf(
            "userId" to user.userId,
            "userName" to user.userName,
            "userPP" to user.userPP,
            "userNum" to user.userNum,
            "userType" to user.userType.name,
            "isLogin" to user.isLogin,
            "selectedPaymentMethod" to user.selectedPaymentMethod,
            "cart" to cartMap,
            "paymentMethods" to paymentMethodsList,
            "address" to addressList
        )

        firestore.collection("user")
            .document(userId)
            .update(userMap)
            .await()
    }


    override suspend fun guestLogin(): String {
        val authResult = auth.signInAnonymously().await()
        val uid = authResult.user?.uid ?: throw Exception("Anonymous sign-in failed")
        val userData = hashMapOf(
            "userId" to uid,
            "userType" to USERTYPE.GUEST.name,
            "userName" to "Guest User",
            "userPP" to "",
            "userNum" to "",
            "isLogin" to true,
            "cart" to null,
            "paymentMethod" to null,
            "selectedPaymentMethod" to PaymentModel.COD.id,
            "address" to emptyList<AddressModel>()
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
            "userName" to user.userName,
            "userPP" to user.userPP,
            "userNum" to user.userNum,
            "userType" to USERTYPE.LOGGED.name,
            "isLogin" to true,
            "cart" to (cartData ?: user.cart),
            "paymentMethod" to null,
            "selectedPaymentMethod" to PaymentModel.COD.id,
            "address" to user.address
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

    override suspend fun addPaymentMethod(method: PaymentModel): String {
        val userId = auth.currentUser?.uid ?: throw Exception("User not authenticated")
        val userRef = firestore.collection("user").document(userId)

        userRef.update("paymentMethods", arrayUnion(method)).await()
        return "Payment method added successfully"
    }

    override suspend fun getPaymentMethod(): List<PaymentModel> {
        val userId = auth.currentUser?.uid ?: throw Exception("User not authenticated")
        val document = firestore.collection("user").document(userId).get().await()

        val paymentList = document.get("paymentMethods") as? List<Map<String, Any>> ?: emptyList()
        return paymentList.mapNotNull { PaymentModel.fromMap(it) }
    }

    override suspend fun updatePaymentMethod(paymentMethodId: String, updatedMethod: PaymentModel): String {
        val userId = auth.currentUser?.uid ?: throw Exception("User not authenticated")
        val userRef = firestore.collection("user").document(userId)

        val currentMethods = getPaymentMethod()
        val oldMethod = currentMethods.find { it.id == paymentMethodId }
            ?: throw Exception("Payment method not found")

        userRef.update("paymentMethods", arrayRemove(oldMethod)).await()

        val newMethod = when (updatedMethod) {
            is PaymentModel.COD -> PaymentModel.COD
            is PaymentModel.Upi -> updatedMethod.copy(id = paymentMethodId)
            is PaymentModel.Card -> updatedMethod.copy(id = paymentMethodId)
        }

        userRef.update("paymentMethods", arrayUnion(newMethod)).await()
        return "Payment method updated successfully"
    }

    override suspend fun deletePaymentMethod(paymentMethodId: String): String {
        val userId = auth.currentUser?.uid ?: throw Exception("User not authenticated")
        val userRef = firestore.collection("user").document(userId)

        val currentMethods = getPaymentMethod()
        val methodToDelete = currentMethods.find { it.id == paymentMethodId }
            ?: throw Exception("Payment method not found")

        userRef.update("paymentMethods", arrayRemove(methodToDelete)).await()
        return "Payment method deleted successfully"
    }

    override suspend fun selectedPaymentMethod(id: String) {
        val uid = auth.currentUser?.uid ?: throw Exception("User not logged in")
        firestore.collection("user").document(uid)
            .update("selectedPaymentMethod", id)
            .await()
    }

    override suspend fun getSelectedPaymentMethod(): String {
        val uid = auth.currentUser?.uid ?: throw Exception("User not logged in")
        val doc = firestore.collection("user").document(uid).get().await()
        return doc.getString("selectedPaymentMethod") ?: PaymentModel.COD.id
    }

    override suspend fun getCoupons(): List<Coupon> {
        val collection = firestore.collection("coupons").get().await()
        val coupons = collection.documents.map { doc ->
            Coupon(
                id = doc.id,
                code = doc.getString("code") ?: "",
                description = doc.getString("description") ?: "",
                discountAmount = doc.getField("discountAmount") ?: 0,
                discountPercent = doc.getField("discountPercent") ?: 0,
                expiryDate = doc.getString("expiryDate") ?: "",
                isApplied = doc.getField("applied") ?: false
            )
        }
        return coupons
    }

    override suspend fun applyCoupon(id: String) {
        val allCoupons = firestore.collection("coupons").get().await()
        allCoupons.documents.forEach { doc ->
            if (doc.getBoolean("applied") == true && doc.id != id) {
                doc.reference.update("applied", false).await()
            }
        }

        firestore.collection("coupons")
            .document(id)
            .update("applied", true)
            .await()
    }

    override suspend fun removeCoupon(id: String) {
        firestore.collection("coupons")
            .document(id)
            .update("applied", false)
            .await()
    }

    override suspend fun addAddress(address: AddressModel) {
        val uid = auth.currentUser?.uid ?: throw Exception("User not logged in")
        val userRef = firestore.collection("user").document(uid)

        userRef.update("address", arrayUnion(address)).await()
    }

    override suspend fun updateAddress(id: String, address: AddressModel) {
        val uid = auth.currentUser?.uid ?: throw Exception("User not logged in")
        val userRef = firestore.collection("user").document(uid)

        val document = userRef.get().await()
        val addressList = document.get("address") as? List<Map<String, Any>> ?: emptyList()

        val updatedList = addressList.map {
            if (it["id"] == id) {
                hashMapOf(
                    "id" to address.id,
                    "name" to address.name,
                    "phone" to address.phone,
                    "pinCode" to address.pinCode,
                    "fullAddress" to address.fullAddress,
                    "city" to address.city,
                    "state" to address.state,
                    "type" to address.type
                )
            } else it
        }

        userRef.update("address", updatedList).await()
    }

    override suspend fun deleteAddress(id: String) {
        val uid = auth.currentUser?.uid ?: throw Exception("User not logged in")
        val userRef = firestore.collection("user").document(uid)

        val document = userRef.get().await()
        val addressList = document.get("address") as? List<Map<String, Any>> ?: emptyList()

        val updatedList = addressList.filter { it["id"] != id }

        userRef.update("address", updatedList).await()
    }


    override suspend fun uploadDataToFirestore() {
        val coupons = listOf(
            Coupon(code = "FLAT50", description = "Get ₹50 off on your order", discountAmount = 50, discountPercent =  0, expiryDate =  "2025-12-31", isApplied = false),
            Coupon(code = "SAVE10", description = "Save 10% on your order", discountAmount =  0, discountPercent = 10, expiryDate =  "2025-12-31", isApplied = false),
            Coupon(code = "WELCOME100", description = "Get ₹100 off on first order", discountAmount =  100, discountPercent = 0, expiryDate =  "2026-03-31", isApplied = false),
            Coupon(code = "FESTIVE20", description = "Get 20% off during festive season", discountAmount = 0, discountPercent = 20, expiryDate =  "2025-12-31", isApplied = false),
            Coupon(code = "FREESHIP", description =  "Free delivery on this order", discountAmount = 40, discountPercent = 0, expiryDate =  "2025-12-31", isApplied = false)
        )

        for (coupon in coupons) {
            firestore.collection("coupons")
                .document()
                .set(coupon)
                .addOnSuccessListener {
                    println("Coupon ${coupon.code} added")
                }
                .addOnFailureListener { e ->
                    println("Error adding coupon ${coupon.code}: $e")
                }
        }
    }

}