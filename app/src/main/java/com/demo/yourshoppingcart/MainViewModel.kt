package com.demo.yourshoppingcart

import androidx.lifecycle.ViewModel
import com.demo.yourshoppingcart.common.StorageKeys
import com.demo.yourshoppingcart.framework.storage.StorageProvider
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    //private val storageProvider: StorageProvider
): ViewModel() {
    private val _viewState = MutableStateFlow(MainState())
    val viewState: StateFlow<MainState> = _viewState

    fun getView(isDark: Boolean) {
        _viewState.value = _viewState.value.copy(isDark = isDark)
    }

    fun updateViewTheme(isDark: Boolean) {
        //storageProvider.putBoolean(StorageKeys.APP_THEME,isDark)
        _viewState.value = _viewState.value.copy(isDark)
    }
}