package com.demo.yourshoppingcart.ui.home.di

import com.demo.yourshoppingcart.Core
import com.demo.yourshoppingcart.home.data.repository.HomeRepositoryImpl
import com.demo.yourshoppingcart.home.domain.repository.HomeRepository
import com.demo.yourshoppingcart.home.domain.usecase.GetAllItemUseCase
import com.demo.yourshoppingcart.home.domain.usecase.GetCategoryUseCase
import com.demo.yourshoppingcart.home.domain.usecase.GetSelectedCategoryItemUseCase
import com.demo.yourshoppingcart.user.domain.usecase.GetUserUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent


@Module
@InstallIn(ViewModelComponent::class)
class HomeViewModelModule {
    @Provides
    fun getCategoryUseCase(): GetCategoryUseCase {
        return Core.Home.getCategoryUseCase
    }

    @Provides
    fun getAllItemsUseCase(): GetAllItemUseCase {
        return Core.Home.getAllItemUseCase
    }

    @Provides
    fun getSelectedCatItemUseCase(): GetSelectedCategoryItemUseCase {
        return Core.Home.getSelectedCategoryItemUseCase
    }
}