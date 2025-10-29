package com.demo.yourshoppingcart.di

import android.app.Activity
import android.content.Context
import com.demo.yourshoppingcart.framework.firebase.DocumentApiFirebaseImpl
import com.demo.yourshoppingcart.common.network.DocumentApi
import com.demo.yourshoppingcart.common.network.config.ApiDomains
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.firestore
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ActivityContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class AppModule {

    companion object {
        @Provides
        fun provideApiDomains(): ApiDomains {
            return ApiDomains(
                base = ""
            )
        }

        @Provides
        fun provideFirebaseFireStore(): FirebaseFirestore {
            return Firebase.firestore
        }

        @Provides
        fun provideFirebaseAuth(): FirebaseAuth {
            return Firebase.auth
        }


       /* @Provides
        @Singleton
        fun provideDocumentApi(
            firestore: FirebaseFirestore,
            auth: FirebaseAuth,
            activityProvider: () -> Activity
        ): DocumentApi = DocumentApiFirebaseImpl(firestore, auth, activityProvider)*/
    }

    @Binds
    abstract fun bindDocumentApiProvider(impl: DocumentApiFirebaseImpl): DocumentApi
}
