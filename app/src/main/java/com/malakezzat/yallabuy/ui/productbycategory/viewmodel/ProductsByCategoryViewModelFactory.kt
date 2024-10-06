package com.malakezzat.yallabuy.ui.productbycategory.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.malakezzat.yallabuy.data.ProductsRepository
import java.lang.IllegalArgumentException

class ProductsByCategoryViewModelFactory (private val repository: ProductsRepository):
    ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return if (modelClass.isAssignableFrom(ProductsByCategoryViewModel::class.java)) {
            ProductsByCategoryViewModel(repository) as T
        } else {
            throw IllegalArgumentException("ViewModel Class not found")
        }
    }
}