package com.demo.yourshoppingcart.ui.home.repository

import android.util.Log
import com.demo.yourshoppingcart.Resource
import com.demo.yourshoppingcart.common.network.AppHttpClient
import com.demo.yourshoppingcart.common.network.config.ApiDomains
import com.demo.yourshoppingcart.home.data.model.HomeModel
import com.demo.yourshoppingcart.home.data.source.HomeSource
import com.google.firebase.firestore.FirebaseFirestore

class HomeSourceImpl(
    private val appHttpClient: AppHttpClient,
    private val apiDomains: ApiDomains,
    private val firestore: FirebaseFirestore
): HomeSource {
    override suspend fun getCategory(): HomeModel.CategoryResponse {
        firestore.collection("category").get().addOnCompleteListener {
            if (it.isSuccessful){
                Log.d("CHECK",it.result.toString())
                /*it.result.documents.map {

                }
                Resource.Data(HomeModel.CategoryResponse.toEntity(data))*/
            }
        }
        TODO("Not yet implemented")
    }

    override suspend fun getAllCategoryItem(): HomeModel.CategoryItemResponse {
        TODO("Not yet implemented")
    }

    override suspend fun getSelectedCategoryItem(catId: String): HomeModel.CategoryItemResponse {
        TODO("Not yet implemented")
    }

}