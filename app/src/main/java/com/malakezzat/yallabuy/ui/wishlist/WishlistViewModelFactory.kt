package com.malakezzat.yallabuy.ui.wishlist

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.malakezzat.yallabuy.data.ProductsRepository
import com.malakezzat.yallabuy.ui.search.SearchViewModel
import java.lang.IllegalArgumentException

class WishlistViewModelFactory(private val repository: ProductsRepository):
    ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return if (modelClass.isAssignableFrom(WishlistViewModel::class.java)) {
            WishlistViewModel(repository) as T
        } else {
            throw IllegalArgumentException("ViewModel Class not found")
        }
    }

    }