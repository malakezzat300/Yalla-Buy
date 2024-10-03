package com.malakezzat.yallabuy.ui.auth.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.malakezzat.yallabuy.data.ProductsRepository
import java.lang.IllegalArgumentException

class SignUpViewModelFactory(private val repository: ProductsRepository):
        ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            return if (modelClass.isAssignableFrom(SignUpViewModel::class.java)) {
                SignUpViewModel(repository) as T
            } else {
                throw IllegalArgumentException("ViewModel Class not found")
            }
        }
    }
