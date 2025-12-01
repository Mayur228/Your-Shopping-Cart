package com.demo.yourshoppingcart

import com.demo.yourshoppingcart.user.domain.usecase.CheckUserUseCase
import com.demo.yourshoppingcart.user.domain.usecase.GetUserUseCase
import com.demo.yourshoppingcart.user.domain.usecase.GuestLoginUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent


@Module
@InstallIn(ViewModelComponent::class)
class MainViewModelModule {
    /*@Provides
    fun provideGetUserUseCase(): GetUserUseCase {
        return Core.User.getUserUseCase
    }*/

    @Provides
    fun provideCheckUserUseCase(): CheckUserUseCase {
        return Core.User.checkUserUseCase
    }

    @Provides
    fun provideGuestLoginUseCase(): GuestLoginUseCase {
        return Core.Login.guestLoginUseCase
    }
}
