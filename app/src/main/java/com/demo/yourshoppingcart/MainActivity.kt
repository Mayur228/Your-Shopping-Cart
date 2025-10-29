package com.demo.yourshoppingcart

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.runtime.Composable
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
            val isDark = viewModel.viewState.collectAsState().value.isDark
            val navController = rememberNavController()
            val view by viewModel.viewState.collectAsState()

            if (view.user == null) {
                viewModel.guestLogin()
            }else{
                YourShoppingCartTheme(darkTheme = isDark) {
                    AppNavHost(
                        navController = navController,
                        isDarkTheme = isDark,
                        onThemeToggle = {
                            storageProvider.putBoolean(StorageKeys.APP_THEME, it)
                            viewModel.updateTheme(it)
                        },
                        user = view.user!!,
                    )
                }
            }
        }
    }
}