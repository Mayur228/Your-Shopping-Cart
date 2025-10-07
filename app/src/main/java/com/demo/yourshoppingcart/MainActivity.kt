package com.demo.yourshoppingcart

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.demo.yourshoppingcart.ui.home.HomeScreen
import com.demo.yourshoppingcart.ui.theme.YourShoppingCartTheme
import com.google.firebase.Firebase
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.firestore
import dagger.hilt.android.AndroidEntryPoint
import androidx.compose.runtime.collectAsState
import com.demo.yourshoppingcart.common.StorageKeys
import com.demo.yourshoppingcart.framework.storage.StorageProvider
import com.demo.yourshoppingcart.framework.storage.StorageProviderImpl
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val viewModel: MainViewModel by viewModels()
        val storageProvider = StorageProviderImpl(context = application.applicationContext)
        viewModel.getView(storageProvider.getBoolean(StorageKeys.APP_THEME) == true)

        enableEdgeToEdge()
        setContent {
            val isDark = viewModel.viewState.collectAsState().value.isDark
            YourShoppingCartTheme(darkTheme = isDark) {
                HomeScreen(
                    isDarkTheme = isDark,
                    onThemeToggle = {
                        storageProvider.putBoolean(StorageKeys.APP_THEME,it)
                        viewModel.updateViewTheme(it)
                    },
                    onCartClick = {
                        //Navigate to Cart Screen
                    }
                )
            }
        }
    }
}