package com.malakezzat.yallabuy.ui.shoppingcart.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.malakezzat.paymenttest2.PaymentRemoteDataSourceImpl
import com.malakezzat.yallabuy.data.ProductsRepository
import java.lang.IllegalArgumentException

class ShoppingCartViewModelFactory(private val repository: ProductsRepository):
        ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            return if (modelClass.isAssignableFrom(ShoppingCartViewModel::class.java)) {
                ShoppingCartViewModel(repository) as T
            } else {
                throw IllegalArgumentException("ViewModel Class not found")
            }
        }
}

