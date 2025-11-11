package com.demo.yourshoppingcart

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.navigation.compose.rememberNavController
import com.demo.yourshoppingcart.common.StorageKeys
import com.demo.yourshoppingcart.framework.storage.StorageProviderImpl
import com.demo.yourshoppingcart.navigation.AppNavHost
import com.demo.yourshoppingcart.ui.theme.YourShoppingCartTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val viewModel: MainViewModel by viewModels()
        val storageProvider = StorageProviderImpl(context = application.applicationContext)

        viewModel.updateTheme(storageProvider.getBoolean(StorageKeys.APP_THEME) == true)

        enableEdgeToEdge()

        setContent {
            MainScreen(viewModel, storageProvider)
        }
    }
}

@Composable
fun MainScreen(viewModel: MainViewModel, storageProvider: StorageProviderImpl) {
    val state by viewModel.viewState.collectAsState()
    val navController = rememberNavController()

    when (state) {
        is MainState.Loading -> {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator()
            }
        }

        is MainState.Error -> {
            val errorMessage = (state as MainState.Error).error
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Text(text = errorMessage)
            }
        }

        is MainState.Success -> {
            val successState = state as MainState.Success
            YourShoppingCartTheme(darkTheme = successState.isDark) {
                AppNavHost(
                    navController = navController,
                    isDarkTheme = successState.isDark,
                    onThemeToggle = { isDark ->
                        storageProvider.putBoolean(StorageKeys.APP_THEME, isDark)
                        viewModel.updateTheme(isDark)
                    },
                    user = successState.user
                )
            }
        }
    }
}