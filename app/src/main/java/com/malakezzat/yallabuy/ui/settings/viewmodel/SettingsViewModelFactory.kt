package com.malakezzat.yallabuy.ui.settings.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.malakezzat.yallabuy.data.ProductsRepository
import com.malakezzat.yallabuy.ui.auth.viewmodel.SignUpViewModel
import java.lang.IllegalArgumentException

class SettingsViewModelFactory(private val repository: ProductsRepository):
        ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            return if (modelClass.isAssignableFrom(SettingsViewModel::class.java)) {
                SettingsViewModel(repository) as T
            } else {
                throw IllegalArgumentException("ViewModel Class not found")
            }
        }
}

