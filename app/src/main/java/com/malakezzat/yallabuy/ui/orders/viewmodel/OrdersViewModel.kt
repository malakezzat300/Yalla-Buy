package com.malakezzat.yallabuy.ui.orders.viewmodel

import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth
import com.malakezzat.yallabuy.data.ProductsRepository

class OrdersViewModel (private val repository: ProductsRepository): ViewModel() {

    private val userEmail = FirebaseAuth.getInstance().currentUser?.email
}