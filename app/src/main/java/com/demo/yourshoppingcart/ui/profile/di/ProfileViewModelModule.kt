package com.demo.yourshoppingcart.ui.profile.di

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
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent


@Module
@InstallIn(ViewModelComponent::class)
class ProfileViewModelModule {
    @Provides
    fun provideGetUserUseCase(): GetUserUseCase {
        return Core.User.getUserUseCase
    }

    @Provides
    fun provideUpdateUserUseCase(): UpdateUserUseCase{
        return Core.User.updateUserUseCase
    }
}