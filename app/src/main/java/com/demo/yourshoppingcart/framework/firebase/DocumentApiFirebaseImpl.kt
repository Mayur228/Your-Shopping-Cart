package com.demo.yourshoppingcart.framework.firebase

import android.util.Log
import com.demo.yourshoppingcart.common.network.DocumentApi
import com.demo.yourshoppingcart.home.data.model.HomeModel
import com.demo.yourshoppingcart.product_details.data.model.ProductDetailsModel
import com.demo.yourshoppingcart.user.data.model.USERTYPE
import com.demo.yourshoppingcart.user.data.model.UserModel
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.getField
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class DocumentApiFirebaseImpl @Inject constructor(
    private val firestore: FirebaseFirestore,
    private val auth: FirebaseAuth
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

        val data = HomeModel.CategoryResponse(catData)
        return data
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

        val data = HomeModel.CategoryItemResponse(itemData)
        return data
    }

    override suspend fun fetchSelectedCatItem(cat: String): HomeModel.CategoryItemResponse {
        val collection = firestore.collection("categoryItem").whereEqualTo("cat", cat).get().await()

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

        val data = HomeModel.CategoryItemResponse(itemData)
        return data
    }


    override suspend fun fetchProductDetails(itemId: String): ProductDetailsModel.DetailModel {
        val collection =
            firestore.collection("categoryItem").whereEqualTo("itemId", itemId).get().await()

        val data = collection.documents.firstOrNull()

        val itemData = ProductDetailsModel.DetailModel(
            itemId = data?.getString("itemId") ?: "",
            itemName = data?.getString("itemName") ?: "",
            itemImages = data?.get("itemImages") as? List<String> ?: emptyList(),
            itemDescription = data?.getString("itemDes") ?: "",
            itemPrice = data?.getString("itemPrice") ?: ""
        )
        return itemData
    }

    override suspend fun addProductToCart(cart: UserModel.UserCart) {
        val document = auth.currentUser?.uid ?: return

        val cartData = hashMapOf(
            "productId" to cart.itemId,
            "productName" to cart.itemName,
            "productImg" to cart.itemImg,
            "productPrice" to cart.price,
            "productQun" to cart.itemQun,
            "productDes" to cart.itemSec
        )
        firestore.collection("user")
            .document(document)
            .update("cart", cartData)
            .addOnCompleteListener {
                if (it.isSuccessful) {
                    Log.e("CHECK", "Cart added")
                } else {
                    Log.e("CHECK", it.exception?.message.toString())
                }
            }
    }

    override suspend fun fetchCart(cartId: String): List<UserModel.UserCart> {
        val collection = firestore.collection("user").get().await()
        val itemData = collection.documents.map { doc ->
            UserModel.UserCart(
                itemId = doc.getString("productId") ?: "",
                itemName = doc.getString("productName") ?: "",
                itemImg = doc.getString("productImg") ?: "",
                price = doc.getField("productPrice") ?: "",
                itemQun = doc.getField("productQun") ?: 0,
                itemSec = doc.getString("productDes") ?: ""
            )
        }

        return itemData
    }

    override suspend fun getAnonymouseUser(userId: String): UserModel.UserResponse {
        val document = firestore.collection("user").document(userId).get().await()
        val userData = UserModel.UserResponse(
            userId = document.getString("userId") ?: "",
            userNum = document.getString("userNum") ?: "",
            userType = if (document.getString("userType") == USERTYPE.GUEST.name)
                USERTYPE.GUEST else USERTYPE.LOGGED,
            isLogin = document.getBoolean("isLogin") ?: false,
            cart = document.get("cart") as UserModel.UserCart?
        )
        return userData
    }

    override suspend fun addUser(isAnonymouse: Boolean, user: UserModel.UserResponse): String {
        if (isAnonymouse) {
            val logInAnonymouse = auth.signInAnonymously().await()
            if (logInAnonymouse.user != null) {
                val userData = hashMapOf(
                    "userId" to logInAnonymouse.user?.uid!!,
                    "userType" to USERTYPE.GUEST,
                    "userNum" to "",
                    "isLogin" to false,
                    "cart" to null
                )

                firestore.collection("user")
                    .document(logInAnonymouse.user?.uid!!)
                    .set(userData)
                    .addOnCompleteListener {
                        if (it.isSuccessful) {
                            logInAnonymouse.user?.uid!!
                        } else {
                            "User Not Added due to ${it.exception?.message}"
                        }
                    }
            }
        } else {
            // Write code for phone login
        }
        return ""
    }

}