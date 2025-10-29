/*
package com.demo.yourshoppingcart.di

import android.app.Activity
import android.content.Context
import com.demo.yourshoppingcart.common.network.DocumentApi
import com.demo.yourshoppingcart.framework.firebase.DocumentApiFirebaseImpl
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityComponent
import dagger.hilt.android.qualifiers.ActivityContext

@Module
@InstallIn(ActivityComponent::class)
object ActivityModule {

    @Provides
    fun provideActivityProvider(@ActivityContext activity: Context): () -> Activity = {
        activity as Activity
    }

    @Provides
    fun provideDocumentApi(
        firestore: FirebaseFirestore,
        auth: FirebaseAuth,
        activity: Activity
    ): DocumentApi = DocumentApiFirebaseImpl(firestore, auth, { activity })
}
*/
