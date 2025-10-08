package com.demo.yourshoppingcart.framework.firebase

import com.demo.yourshoppingcart.common.network.DocumentApi
import com.demo.yourshoppingcart.home.data.model.HomeModel
import com.demo.yourshoppingcart.product_details.data.model.ProductDetailsModel
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.getField
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class DocumentApiFirebaseImpl @Inject constructor (
    private val firestore: FirebaseFirestore
) : DocumentApi {
    override suspend fun fetchCategory():  HomeModel.CategoryResponse {
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
        val collection = firestore.collection("categoryItem").whereEqualTo("cat",cat).get().await()

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


    override suspend fun fetchSingleData(): Map<String, String> {
        TODO("Not yet implemented")
    }

    override suspend fun fetchProductDetails(itemId: String): ProductDetailsModel.DetailModel {
        val collection = firestore.collection("categoryItem").whereEqualTo("itemId",itemId).get().await()

        val data = collection.documents.firstOrNull()

        val itemData = ProductDetailsModel.DetailModel(
            itemId = data?.getString("itemId") ?: "",
            itemName = data?.getString("itemName") ?: "",
            itemImages =  data?.get("itemImages") as? List<String> ?: emptyList(),
            itemDescription = data?.getString("itemDes") ?: "",
            itemPrice = data?.getString("itemPrice") ?: ""
        )
        return itemData
    }
}