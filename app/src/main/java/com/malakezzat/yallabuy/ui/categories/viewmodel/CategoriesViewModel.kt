package com.malakezzat.yallabuy.ui.categories.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.malakezzat.yallabuy.data.ProductsRepository
import com.malakezzat.yallabuy.data.remote.ApiState
import com.malakezzat.yallabuy.model.CustomCollection
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch

class CategoriesViewModel (private val repository: ProductsRepository): ViewModel(){
    private val TAG = "CategoriesViewModel"

    private val _categoriesList = MutableStateFlow<ApiState<List<CustomCollection>>>(ApiState.Loading)
    val categoriesList: StateFlow<ApiState<List<CustomCollection>>> get() = _categoriesList


init {
    getAllCategories()
}

    fun getAllCategories(){
        viewModelScope.launch {
            repository.getCategories()
                .onStart {
                    _categoriesList.value = ApiState.Loading
                }.catch { e->
                    _categoriesList.value = ApiState.Error(e.message?:"Unknown error")
                }.collect{categories->
                    val filteredCategories = categories.drop(1)
                    _categoriesList.value = ApiState.Success(filteredCategories)
                   // Log.i(TAG, "getAllCategories: ${categories.get(0).title}")
                }
        }
    }
}