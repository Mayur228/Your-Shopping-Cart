package com.demo.yourshoppingcart

import com.demo.yourshoppingcart.common.di.CoreModule
import com.demo.yourshoppingcart.common.network.DocumentApi
import com.demo.yourshoppingcart.common.network.config.ApiDomains
import com.demo.yourshoppingcart.home.domain.usecase.GetAllItemUseCase
import com.demo.yourshoppingcart.home.domain.usecase.GetCategoryUseCase
import com.demo.yourshoppingcart.home.domain.usecase.GetSelectedCategoryItemUseCase
import com.demo.yourshoppingcart.product_details.domain.usecase.GetProductDetailsUseCase
import com.demo.yourshoppingcart.user.domain.usecase.AddCartUseCase
import com.demo.yourshoppingcart.user.domain.usecase.GetCartUseCase
import com.demo.yourshoppingcart.user.domain.usecase.GetUserUseCase
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
        val getCartUseCase: GetCartUseCase
            get() = get().get()

        val addCartUseCase: AddCartUseCase
            get() = get().get()

        val getUserUseCase: GetUserUseCase
            get() = get().get()
    }
}