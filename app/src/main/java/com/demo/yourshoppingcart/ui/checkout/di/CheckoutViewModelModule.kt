package com.demo.yourshoppingcart.ui.checkout.di

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
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent


@Module
@InstallIn(ViewModelComponent::class)
class CheckoutViewModelModule {
    @Provides
    fun provideAddPaymentMethodUseCase(): AddPaymentMethodUseCase {
        return Core.Payment.addPaymentMethodUseCase
    }

    @Provides
    fun provideGetPaymentMethodUseCase(): GetPaymentMethodUseCase {
        return Core.Payment.getPaymentMethodUseCase
    }

    @Provides
    fun provideUpdatePaymentMethodUseCase(): UpdatePaymentMethodUseCase {
        return Core.Payment.updatePaymentMethodUseCase
    }

    @Provides
    fun provideDeletePaymentMethodUseCase(): DeletePaymentMethodUseCase {
        return Core.Payment.deletePaymentMethodUseCase
    }

    @Provides
    fun provideSelectedPaymentMethodUseCase(): SelectedPaymentMethodUseCase {
        return Core.Payment.selectedPaymentMethodUseCase
    }

    @Provides
    fun provideGetSelectedPaymentMethodUseCase(): GetSelectedPaymentMethodUseCase {
        return Core.Payment.getSelectedPaymentMethodUseCase
    }
}