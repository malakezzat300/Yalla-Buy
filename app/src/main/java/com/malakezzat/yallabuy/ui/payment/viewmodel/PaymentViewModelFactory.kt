package com.malakezzat.yallabuy.ui.payment.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.malakezzat.yallabuy.data.ProductsRepository
import com.malakezzat.yallabuy.ui.auth.viewmodel.SignUpViewModel
import java.lang.IllegalArgumentException

class PaymentViewModelFactory(private val repository: ProductsRepository):
        ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            return if (modelClass.isAssignableFrom(PaymentViewModel::class.java)) {
                PaymentViewModel(repository) as T
            } else {
                throw IllegalArgumentException("ViewModel Class not found")
            }
        }
}

