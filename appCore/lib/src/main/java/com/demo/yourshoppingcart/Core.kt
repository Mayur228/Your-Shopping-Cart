package com.demo.yourshoppingcart

import com.demo.yourshoppingcart.common.di.CoreModule
import com.demo.yourshoppingcart.common.network.DocumentApi
import com.demo.yourshoppingcart.common.network.config.ApiDomains
import com.demo.yourshoppingcart.home.domain.usecase.GetCategoryUseCase
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

    /*object Home {
        val getCategoryUseCase: GetCategoryUseCase
            get() = get().get()
    }*/
}