package com.demo.yourshoppingcart.ui.cart.di

import com.demo.yourshoppingcart.Core
import com.demo.yourshoppingcart.home.data.repository.HomeRepositoryImpl
import com.demo.yourshoppingcart.home.domain.repository.HomeRepository
import com.demo.yourshoppingcart.home.domain.usecase.GetAllItemUseCase
import com.demo.yourshoppingcart.home.domain.usecase.GetCategoryUseCase
import com.demo.yourshoppingcart.home.domain.usecase.GetSelectedCategoryItemUseCase
import com.demo.yourshoppingcart.user.domain.entity.cartEntity
import com.demo.yourshoppingcart.user.domain.usecase.AddCartUseCase
import com.demo.yourshoppingcart.user.domain.usecase.GetCartUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent


@Module
@InstallIn(ViewModelComponent::class)
class CartViewModelModule {
    @Provides
    fun getCartUseCase(): GetCartUseCase {
        return Core.User.getCartUseCase
    }

    @Provides
    fun addCartUseCase(): AddCartUseCase {
        return Core.User.addCartUseCase
    }
}