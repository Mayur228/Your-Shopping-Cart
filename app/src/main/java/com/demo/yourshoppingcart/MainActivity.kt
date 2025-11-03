package com.demo.yourshoppingcart

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import com.demo.yourshoppingcart.ui.home.HomeScreen
import com.demo.yourshoppingcart.ui.theme.YourShoppingCartTheme
import dagger.hilt.android.AndroidEntryPoint
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.navigation.compose.rememberNavController
import com.demo.yourshoppingcart.common.StorageKeys
import com.demo.yourshoppingcart.framework.storage.StorageProviderImpl
import com.demo.yourshoppingcart.navigation.AppNavHost
import com.demo.yourshoppingcart.navigation.NavRoutes.CART

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val viewModel: MainViewModel by viewModels()
        val storageProvider = StorageProviderImpl(context = application.applicationContext)
        viewModel.updateTheme(storageProvider.getBoolean(StorageKeys.APP_THEME) == true)

        enableEdgeToEdge()
        setContent {
            val view by viewModel.viewState.collectAsState()
            val navController = rememberNavController()
            val storageProvider = StorageProviderImpl(context = application.applicationContext)

            val isDark = view.isDark

            YourShoppingCartTheme(darkTheme = isDark) {
                if (view.user == null && !view.isLoading) {
                    LaunchedEffect(Unit) {
                        viewModel.guestLogin()
                    }
                }

                if (view.user == null) {
                    // wait for get user
                } else {
                    AppNavHost(
                        navController = navController,
                        isDarkTheme = isDark,
                        onThemeToggle = {
                            storageProvider.putBoolean(StorageKeys.APP_THEME, it)
                            viewModel.updateTheme(it)
                        },
                        user = view.user!!
                    )
                }
            }
        }
    }
}