package com.demo.yourshoppingcart.ui.coupon.di

import com.demo.yourshoppingcart.Core
import com.demo.yourshoppingcart.cart.domain.usecase.AddCartUseCase
import com.demo.yourshoppingcart.cart.domain.usecase.ClearCartUseCase
import com.demo.yourshoppingcart.cart.domain.usecase.GetCartUseCase
import com.demo.yourshoppingcart.cart.domain.usecase.UpdateCartUseCase
import com.demo.yourshoppingcart.coupon.domain.usecase.ApplyCouponUseCase
import com.demo.yourshoppingcart.coupon.domain.usecase.GetCouponsUseCase
import com.demo.yourshoppingcart.coupon.domain.usecase.RemoveCouponUseCase
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
class CouponsViewModelModule {
    @Provides
    fun provideGetCouponsUseCase(): GetCouponsUseCase {
        return Core.Coupons.getCouponsUseCase
    }

    @Provides
    fun provideApplyCouponUseCase(): ApplyCouponUseCase {
        return Core.Coupons.applyCouponUseCase
    }

    @Provides
    fun provideRemoveCouponUseCase(): RemoveCouponUseCase {
        return Core.Coupons.removeCouponUseCase
    }

}