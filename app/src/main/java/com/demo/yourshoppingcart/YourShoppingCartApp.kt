package com.demo.yourshoppingcart

import android.app.Application
import com.demo.yourshoppingcart.common.network.config.ApiDomains
import com.demo.yourshoppingcart.framework.firebase.DocumentApiFirebaseImpl
import com.google.firebase.Firebase
import com.google.firebase.FirebaseApp
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.firestore
import dagger.hilt.android.HiltAndroidApp
import javax.inject.Inject

@HiltAndroidApp
class YourShoppingCartApp: Application() {

    @Inject
    lateinit var apiDomains: ApiDomains

//    @Inject
//    lateinit var db: FirebaseFirestore

    @Inject
    lateinit var documentApi: DocumentApiFirebaseImpl

    override fun onCreate() {
        super.onCreate()
        FirebaseApp.initializeApp(this)
//        db = Firebase.firestore
        initCore()
    }

    private fun initCore() {
        Core.init(
            config = Core.Config(
                apiDomains = apiDomains,
                documentApi = documentApi,
            )
        )
    }
}