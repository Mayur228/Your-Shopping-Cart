package com.demo.yourshoppingcart

import android.content.Context
import com.demo.yourshoppingcart.Core
import com.demo.yourshoppingcart.framework.storage.StorageProvider
import com.demo.yourshoppingcart.framework.storage.StorageProviderImpl
import com.demo.yourshoppingcart.home.data.repository.HomeRepositoryImpl
import com.demo.yourshoppingcart.home.domain.repository.HomeRepository
import com.demo.yourshoppingcart.home.domain.usecase.GetAllItemUseCase
import com.demo.yourshoppingcart.home.domain.usecase.GetCategoryUseCase
import com.demo.yourshoppingcart.home.domain.usecase.GetSelectedCategoryItemUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.qualifiers.ApplicationContext


/*
@Module
@InstallIn(ViewModelComponent::class)
class MainViewModelModule {
    @Provides
    fun storageProvider(context: Context): StorageProvider {
        return StorageProviderImpl(
            context = @ApplicationContext context
        )
    }
}*/
