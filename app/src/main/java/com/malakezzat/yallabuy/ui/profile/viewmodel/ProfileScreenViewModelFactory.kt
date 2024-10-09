package com.malakezzat.yallabuy.ui.profile.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.malakezzat.yallabuy.data.ProductsRepository
import com.malakezzat.yallabuy.ui.home.viewmodel.HomeScreenViewModel
import java.lang.IllegalArgumentException

class ProfileScreenViewModelFactory (private val repository: ProductsRepository):
    ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return if (modelClass.isAssignableFrom(ProfileScreenViewModel::class.java)) {
            ProfileScreenViewModel(repository) as T
        } else {
            throw IllegalArgumentException("ViewModel Class not found")
        }
    }
}