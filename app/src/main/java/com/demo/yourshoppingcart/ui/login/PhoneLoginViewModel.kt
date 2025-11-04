package com.demo.yourshoppingcart.ui.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.demo.yourshoppingcart.user.data.model.USERTYPE
import com.demo.yourshoppingcart.user.data.model.UserModel
import com.demo.yourshoppingcart.user.domain.usecase.PhoneLoginUseCase
import com.demo.yourshoppingcart.user.domain.usecase.SendOtpUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PhoneLoginViewModel @Inject constructor(
    private val phoneLoginUseCase: PhoneLoginUseCase,
    private val sendOtpUseCase: SendOtpUseCase
) : ViewModel() {

    private val _state = MutableStateFlow(PhoneLoginState())
    val state: StateFlow<PhoneLoginState> = _state

    var verificationId: String? = null

    fun sendOtp(phoneNumber: String) {
        _state.value = _state.value.copy(isLoading = true, errorMessage = null)

        viewModelScope.launch {
            try {
                val sentVerificationId = sendOtpUseCase.invoke("+${91}${phoneNumber}")

                verificationId = sentVerificationId
                _state.value = _state.value.copy(
                    isLoading = false,
                    otpSent = true
                )

            } catch (e: Exception) {
                _state.value = _state.value.copy(
                    isLoading = false,
                    errorMessage = e.message
                )
            }
        }
    }

    fun verifyOtp(otp: String, guestId: String?, userNumber: String) {
        _state.value = _state.value.copy(isLoading = true, errorMessage = null)

        viewModelScope.launch {
            try {
                val user = UserModel.UserResponse(
                    userId = "", // Will be replaced by Firebase UID
                    userNum = userNumber,
                    userType = USERTYPE.LOGGED,
                    isLogin = true,
                    cart = null
                )

                val uid = phoneLoginUseCase.invoke(
                    oldGuestId = guestId,
                    user = user,
                    verificationId = verificationId,
                    otp = otp
                )

                _state.value = _state.value.copy(
                    isLoading = false,
                    loginSuccessId = uid as String
                )

            } catch (e: Exception) {
                _state.value = _state.value.copy(
                    isLoading = false,
                    errorMessage = e.message
                )
            }
        }
    }
}

data class PhoneLoginState(
    val isLoading: Boolean = false,
    val otpSent: Boolean = false,
    val loginSuccessId: String? = null,
    val errorMessage: String? = null
)
