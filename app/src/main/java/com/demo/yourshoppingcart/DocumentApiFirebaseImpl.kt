package com.demo.yourshoppingcart

import com.demo.yourshoppingcart.common.network.DocumentApi
import com.google.firebase.firestore.FirebaseFirestore
import org.w3c.dom.Document
import javax.inject.Inject

class DocumentApiFirebaseImpl @Inject constructor (
    private val firestore: FirebaseFirestore
) : DocumentApi {
    override suspend fun fetchAllData(): List<Map<String, String>> {
        TODO("Not yet implemented")
    }

    override suspend fun fetchSingleData(): Map<String, String> {
        TODO("Not yet implemented")
    }
}