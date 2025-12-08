package com.demo.yourshoppingcart

import com.demo.yourshoppingcart.common.di.CoreModule
import com.demo.yourshoppingcart.common.network.DocumentApi
import com.demo.yourshoppingcart.common.network.config.ApiDomains
import com.demo.yourshoppingcart.home.domain.usecase.GetAllItemUseCase
import com.demo.yourshoppingcart.home.domain.usecase.GetCategoryUseCase
import com.demo.yourshoppingcart.home.domain.usecase.GetSelectedCategoryItemUseCase
import com.demo.yourshoppingcart.product_details.domain.usecase.GetProductDetailsUseCase
import com.demo.yourshoppingcart.cart.domain.usecase.AddCartUseCase
import com.demo.yourshoppingcart.cart.domain.usecase.ClearCartUseCase
import com.demo.yourshoppingcart.cart.domain.usecase.GetCartUseCase
import com.demo.yourshoppingcart.user.domain.usecase.GetUserUseCase
import com.demo.yourshoppingcart.user.domain.usecase.GuestLoginUseCase
import com.demo.yourshoppingcart.user.domain.usecase.PhoneLoginUseCase
import com.demo.yourshoppingcart.user.domain.usecase.SendOtpUseCase
import com.demo.yourshoppingcart.cart.domain.usecase.UpdateCartUseCase
import com.demo.yourshoppingcart.coupon.domain.usecase.ApplyCouponUseCase
import com.demo.yourshoppingcart.coupon.domain.usecase.GetCouponsUseCase
import com.demo.yourshoppingcart.coupon.domain.usecase.RemoveCouponUseCase
import com.demo.yourshoppingcart.order.domain.usecase.AddOrderHistoryUseCase
import com.demo.yourshoppingcart.order.domain.usecase.GetOrderDetailsUseCase
import com.demo.yourshoppingcart.order.domain.usecase.GetOrderHistoryUseCase
import com.demo.yourshoppingcart.payment.domain.usecase.AddPaymentMethodUseCase
import com.demo.yourshoppingcart.payment.domain.usecase.DeletePaymentMethodUseCase
import com.demo.yourshoppingcart.payment.domain.usecase.GetPaymentMethodUseCase
import com.demo.yourshoppingcart.payment.domain.usecase.GetSelectedPaymentMethodUseCase
import com.demo.yourshoppingcart.payment.domain.usecase.SelectedPaymentMethodUseCase
import com.demo.yourshoppingcart.payment.domain.usecase.UpdatePaymentMethodUseCase
import com.demo.yourshoppingcart.user.domain.usecase.AddAddressUseCase
import com.demo.yourshoppingcart.user.domain.usecase.CheckUserUseCase
import com.demo.yourshoppingcart.user.domain.usecase.DeleteAddressUseCase
import com.demo.yourshoppingcart.user.domain.usecase.UpdateAddressUseCase
import com.demo.yourshoppingcart.user.domain.usecase.UpdateUserUseCase
import org.koin.core.context.GlobalContext.get
import org.koin.core.context.startKoin
import org.koin.dsl.module
import org.koin.ksp.generated.module

object Core {

    data class Config(
        val apiDomains: ApiDomains,
        val documentApi: DocumentApi,
    )

    fun init(config: Config) {
        startKoin {
            modules(
                CoreModule().module,
                module {
                   single {
                       config.apiDomains
                   }
                    single {
                        config.documentApi
                    }
                }
            )
        }
    }

    object Home {
        val getCategoryUseCase: GetCategoryUseCase
            get() = get().get()

        val getAllItemUseCase: GetAllItemUseCase
            get() = get().get()

        val getSelectedCategoryItemUseCase: GetSelectedCategoryItemUseCase
            get() = get().get()

    }

    object ProductDetails {
        val getProductDetailsUseCase: GetProductDetailsUseCase
            get() = get().get()
    }

    object User {
        val checkUserUseCase: CheckUserUseCase
            get() = get().get()
        val getUserUseCase: GetUserUseCase
            get() = get().get()

        val updateUserUseCase: UpdateUserUseCase
            get() = get().get()

        val addAddressUseCase: AddAddressUseCase
            get() = get().get()

        val updateAddressUseCase: UpdateAddressUseCase
            get() = get().get()

        val deleteAddressUseCase: DeleteAddressUseCase
            get() = get().get()
    }

    object Login {
        val guestLoginUseCase: GuestLoginUseCase
            get() = get().get()

        val phoneLoginUseCase: PhoneLoginUseCase
            get() = get().get()

        val sendOtpUseCase: SendOtpUseCase
            get() = get().get()
    }

    object Cart {
        val getCartUseCase: GetCartUseCase
            get() = get().get()

        val addCartUseCase: AddCartUseCase
            get() = get().get()

        val updateCartUseCase: UpdateCartUseCase
            get() = get().get()

        val clearCartUseCase: ClearCartUseCase
            get() = get().get()
    }

    object Payment {
        val addPaymentMethodUseCase: AddPaymentMethodUseCase
            get() = get().get()

        val getPaymentMethodUseCase: GetPaymentMethodUseCase
            get() = get().get()

        val updatePaymentMethodUseCase: UpdatePaymentMethodUseCase
            get() = get().get()

        val deletePaymentMethodUseCase: DeletePaymentMethodUseCase
            get() = get().get()

        val selectedPaymentMethodUseCase: SelectedPaymentMethodUseCase
            get() = get().get()

        val getSelectedPaymentMethodUseCase: GetSelectedPaymentMethodUseCase
            get() = get().get()
    }

    object Coupons{
        val getCouponsUseCase: GetCouponsUseCase
            get() = get().get()

        val applyCouponUseCase: ApplyCouponUseCase
            get() = get().get()

        val removeCouponUseCase: RemoveCouponUseCase
            get() = get().get()
    }

    object Order{
        val addOrderHistoryUseCase: AddOrderHistoryUseCase
            get() = get().get()

        val getOrderHistoryUseCase: GetOrderHistoryUseCase
            get() = get().get()

        val getOrderDetailsUseCase: GetOrderDetailsUseCase
            get() = get().get()
    }
}