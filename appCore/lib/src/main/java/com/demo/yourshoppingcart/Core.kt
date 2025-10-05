package com.demo.yourshoppingcart

import com.demo.yourshoppingcart.common.di.CoreModule
import com.demo.yourshoppingcart.common.network.config.ApiDomains

import org.koin.core.KoinApplication
import org.koin.core.context.startKoin
import org.koin.core.module.Module
import org.koin.dsl.module
import org.koin.ksp.generated.module

object Core {

    data class Config(
        val apiDomains: ApiDomains,
    )

    fun init(config: Config) {
        startKoin {
            modules(
                CoreModule().module,
                module {
                   single {
                       config.apiDomains
                   }
                }
            )
        }
    }
}