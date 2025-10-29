package com.demo.yourshoppingcart.ui.login

import com.demo.yourshoppingcart.Core
import com.demo.yourshoppingcart.home.data.repository.HomeRepositoryImpl
import com.demo.yourshoppingcart.home.domain.repository.HomeRepository
import com.demo.yourshoppingcart.home.domain.usecase.GetAllItemUseCase
import com.demo.yourshoppingcart.home.domain.usecase.GetCategoryUseCase
import com.demo.yourshoppingcart.home.domain.usecase.GetSelectedCategoryItemUseCase
import com.demo.yourshoppingcart.user.domain.usecase.GetUserUseCase
import com.demo.yourshoppingcart.user.domain.usecase.GuestLoginUseCase
import com.demo.yourshoppingcart.user.domain.usecase.PhoneLoginUseCase
import com.demo.yourshoppingcart.user.domain.usecase.SendOtpUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent


@Module
@InstallIn(ViewModelComponent::class)
class PhoneLoginViewModelModule {
    @Provides
    fun providePhoneLoginUseCase(): PhoneLoginUseCase {
        return Core.Login.phoneLoginUseCase
    }

    @Provides
    fun provideSendOtpUseCase(): SendOtpUseCase {
        return Core.Login.sendOtpUseCase
    }
}