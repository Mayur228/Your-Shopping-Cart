package com.demo.yourshoppingcart.ui.login

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import com.demo.yourshoppingcart.ui.login.PhoneLoginViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PhoneLoginScreen(
    onLoginSuccess: (String) -> Unit
) {
    val viewModel: PhoneLoginViewModel = hiltViewModel()
    val state by viewModel.state.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(title = { Text("Login with Phone") })
        }
    ) { paddingValues ->

        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp),
            contentAlignment = Alignment.Center
        ) {
            when (state) {
                is PhoneLoginState.Idle,
                is PhoneLoginState.EnterPhone -> {
                    val phone = (state as? PhoneLoginState.EnterPhone)?.phoneNumber ?: ""
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {
                        TextField(
                            value = phone,
                            onValueChange = { viewModel.onEvent(PhoneLoginEvent.PhoneChanged(it)) },
                            label = { Text("Phone Number") },
                            singleLine = true,
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                            modifier = Modifier.fillMaxWidth()
                        )
                        Spacer(Modifier.height(16.dp))
                        Button(
                            onClick = { viewModel.onEvent(PhoneLoginEvent.SendOtpClicked) },
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text("Send OTP")
                        }
                    }
                }

                is PhoneLoginState.Loading -> {
                    CircularProgressIndicator()
                }

                is PhoneLoginState.EnterOtp -> {
                    val enterOtpState = state as PhoneLoginState.EnterOtp
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {
                        Text("OTP sent to ${enterOtpState.phoneNumber}")
                        Spacer(Modifier.height(8.dp))
                        TextField(
                            value = enterOtpState.otp,
                            onValueChange = { viewModel.onEvent(PhoneLoginEvent.OtpChanged(it)) },
                            label = { Text("Enter OTP") },
                            singleLine = true,
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                            modifier = Modifier.fillMaxWidth()
                        )
                        Spacer(Modifier.height(16.dp))
                        Button(
                            onClick = { viewModel.onEvent(PhoneLoginEvent.VerifyOtpClicked) },
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text("Verify OTP")
                        }
                    }
                }

                is PhoneLoginState.Success -> {
                    onLoginSuccess((state as PhoneLoginState.Success).loginId)
                }

                is PhoneLoginState.Error -> {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {
                        Text(
                            text = (state as PhoneLoginState.Error).message,
                            color = Color.Red,
                            modifier = Modifier.padding(16.dp)
                        )
                        Spacer(Modifier.height(16.dp))
                        Button(onClick = {
                            viewModel.onEvent(PhoneLoginEvent.PhoneChanged(""))
                        }) {
                            Text("Try Again")
                        }
                    }
                }
            }
        }
    }
}