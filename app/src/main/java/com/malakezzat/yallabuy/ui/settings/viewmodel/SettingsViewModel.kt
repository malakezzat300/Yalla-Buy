package com.malakezzat.yallabuy.ui.settings.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.malakezzat.yallabuy.data.ProductsRepository
import com.malakezzat.yallabuy.data.remote.ApiState
import com.malakezzat.yallabuy.model.CurrencyResponse
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch

class SettingsViewModel(private val repository: ProductsRepository): ViewModel() {

    private val _conversionRate = MutableStateFlow<ApiState<CurrencyResponse?>>(ApiState.Loading)
    val conversionRate = _conversionRate.asStateFlow()

    fun getRate(){
        Log.i("currencyTest", "getRate: getting rate")
        viewModelScope.launch {
            repository.getConversionRate().onStart {
                _conversionRate.value = ApiState.Loading
            }
                .catch { e ->
                    _conversionRate.value = ApiState.Error(e.message ?: "Unknown error")
                }
                .collect { response ->
                    _conversionRate.value = ApiState.Success(response)
                }
        }
    }
}