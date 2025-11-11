package com.demo.yourshoppingcart.ui.login

sealed class PhoneLoginState {
    object Idle : PhoneLoginState()
    object Loading : PhoneLoginState()
    data class EnterPhone(val phoneNumber: String) : PhoneLoginState()
    data class EnterOtp(val phoneNumber: String, val otp: String, val otpSent: Boolean) : PhoneLoginState()
    data class Success(val loginId: String) : PhoneLoginState()
    data class Error(val message: String) : PhoneLoginState()
}

sealed class PhoneLoginEvent {
    data class PhoneChanged(val phone: String) : PhoneLoginEvent()
    object SendOtpClicked : PhoneLoginEvent()
    data class OtpChanged(val otp: String) : PhoneLoginEvent()
    object VerifyOtpClicked : PhoneLoginEvent()
}

