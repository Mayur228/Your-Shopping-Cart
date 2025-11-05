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
import com.demo.yourshoppingcart.cart.domain.usecase.GetProductsUseCase
import com.demo.yourshoppingcart.user.domain.usecase.GetUserUseCase
import com.demo.yourshoppingcart.user.domain.usecase.GuestLoginUseCase
import com.demo.yourshoppingcart.user.domain.usecase.PhoneLoginUseCase
import com.demo.yourshoppingcart.user.domain.usecase.SendOtpUseCase
import com.demo.yourshoppingcart.cart.domain.usecase.UpdateCartUseCase
import com.demo.yourshoppingcart.home.domain.usecase.GetQuantityUseCase
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

        val getUserUseCase: GetQuantityUseCase
            get() = get().get()
    }

    object ProductDetails {
        val getProductDetailsUseCase: GetProductDetailsUseCase
            get() = get().get()
    }

    object User {
        val getUserUseCase: GetUserUseCase
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

        val getProductsUseCase: GetProductsUseCase
            get() = get().get()

        val updateCartUseCase: UpdateCartUseCase
            get() = get().get()

        val clearCartUseCase: ClearCartUseCase
            get() = get().get()

    }
}