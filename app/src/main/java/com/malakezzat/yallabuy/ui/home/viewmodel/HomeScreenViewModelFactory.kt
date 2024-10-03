package com.malakezzat.yallabuy.ui.home.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.malakezzat.yallabuy.data.ProductsRepository
import java.lang.IllegalArgumentException

class HomeScreenViewModelFactory(private val repository: ProductsRepository):
    ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return if (modelClass.isAssignableFrom(HomeScreenViewModel::class.java)) {
            HomeScreenViewModel(repository) as T
        } else {
            throw IllegalArgumentException("ViewModel Class not found")
        }
    }
}