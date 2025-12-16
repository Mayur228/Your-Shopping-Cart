package com.demo.yourshoppingcart.ui.wish_list.di

import com.demo.yourshoppingcart.Core
import com.demo.yourshoppingcart.cart.domain.usecase.AddCartUseCase
import com.demo.yourshoppingcart.cart.domain.usecase.ClearCartUseCase
import com.demo.yourshoppingcart.cart.domain.usecase.GetCartUseCase
import com.demo.yourshoppingcart.cart.domain.usecase.UpdateCartUseCase
import com.demo.yourshoppingcart.payment.domain.usecase.AddPaymentMethodUseCase
import com.demo.yourshoppingcart.payment.domain.usecase.DeletePaymentMethodUseCase
import com.demo.yourshoppingcart.payment.domain.usecase.GetPaymentMethodUseCase
import com.demo.yourshoppingcart.payment.domain.usecase.GetSelectedPaymentMethodUseCase
import com.demo.yourshoppingcart.payment.domain.usecase.SelectedPaymentMethodUseCase
import com.demo.yourshoppingcart.payment.domain.usecase.UpdatePaymentMethodUseCase
import com.demo.yourshoppingcart.user.domain.usecase.AddAddressUseCase
import com.demo.yourshoppingcart.user.domain.usecase.DeleteAddressUseCase
import com.demo.yourshoppingcart.user.domain.usecase.GetUserUseCase
import com.demo.yourshoppingcart.user.domain.usecase.UpdateAddressUseCase
import com.demo.yourshoppingcart.user.domain.usecase.UpdateUserUseCase
import com.demo.yourshoppingcart.wish_list.domain.usecase.AddToWishListUseCase
import com.demo.yourshoppingcart.wish_list.domain.usecase.GetWishListUseCase
import com.demo.yourshoppingcart.wish_list.domain.usecase.RemoveWishListUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent


@Module
@InstallIn(ViewModelComponent::class)
class WishListViewModelModule {
    @Provides
    fun provideAddToWishListUseCase(): AddToWishListUseCase {
        return Core.WishList.addToWishListUseCase
    }

    @Provides
    fun provideGetWishListUseCase(): GetWishListUseCase {
        return Core.WishList.getWishListUseCase
    }

    @Provides
    fun provideRemoveWishListUseCase(): RemoveWishListUseCase {
        return Core.WishList.removeWishListUseCase
    }

}