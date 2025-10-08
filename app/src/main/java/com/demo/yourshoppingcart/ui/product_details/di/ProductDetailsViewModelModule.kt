package com.demo.yourshoppingcart.ui.product_details.di

import com.demo.yourshoppingcart.Core
import com.demo.yourshoppingcart.home.data.repository.HomeRepositoryImpl
import com.demo.yourshoppingcart.home.domain.repository.HomeRepository
import com.demo.yourshoppingcart.home.domain.usecase.GetAllItemUseCase
import com.demo.yourshoppingcart.home.domain.usecase.GetCategoryUseCase
import com.demo.yourshoppingcart.home.domain.usecase.GetSelectedCategoryItemUseCase
import com.demo.yourshoppingcart.product_details.domain.usecase.GetProductDetailsUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent


@Module
@InstallIn(ViewModelComponent::class)
class ProductDetailsViewModelModule {
    @Provides
    fun getProductDetailsUseCase(): GetProductDetailsUseCase {
        return Core.ProductDetails.getProductDetailsUseCase
    }
}