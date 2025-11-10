package com.demo.yourshoppingcart.ui.cart.di

import com.demo.yourshoppingcart.Core
import com.demo.yourshoppingcart.cart.domain.usecase.AddCartUseCase
import com.demo.yourshoppingcart.cart.domain.usecase.ClearCartUseCase
import com.demo.yourshoppingcart.cart.domain.usecase.GetCartUseCase
import com.demo.yourshoppingcart.cart.domain.usecase.UpdateCartUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent


@Module
@InstallIn(ViewModelComponent::class)
class CartViewModelModule {
    @Provides
    fun getCartUseCase(): GetCartUseCase {
        return Core.Cart.getCartUseCase
    }
    @Provides
    fun addCartUseCase(): AddCartUseCase {
        return Core.Cart.addCartUseCase
    }

    @Provides
    fun updateCartUseCase(): UpdateCartUseCase {
        return Core.Cart.updateCartUseCase
    }

    @Provides
    fun clearCartUseCase(): ClearCartUseCase {
        return Core.Cart.clearCartUseCase
    }
}