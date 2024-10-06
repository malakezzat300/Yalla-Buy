package com.malakezzat.yallabuy.ui.shoppingcart.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.malakezzat.yallabuy.data.ProductsRepository
import com.malakezzat.yallabuy.data.remot.ApiState
import com.malakezzat.yallabuy.model.CustomCollection
import com.malakezzat.yallabuy.model.DraftOrder
import com.malakezzat.yallabuy.model.DraftOrderResponse
import com.malakezzat.yallabuy.model.DraftOrdersResponse
import com.malakezzat.yallabuy.model.Product
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch

class ShoppingCartViewModel(private val repository: ProductsRepository) : ViewModel() {

    private val TAG = "ShoppingCartViewModel"

    private val _draftOrders = MutableStateFlow<ApiState<DraftOrdersResponse>>(ApiState.Loading)
    val draftOrders : StateFlow<ApiState<DraftOrdersResponse>> get() = _draftOrders

    private val _singleDraftOrders = MutableStateFlow<ApiState<DraftOrderResponse>>(ApiState.Loading)
    val singleDraftOrders : StateFlow<ApiState<DraftOrderResponse>> get() = _singleDraftOrders

    init {
        fetchAllDraftOrders()
    }

    private fun fetchAllDraftOrders() {
        viewModelScope.launch {
            repository.getAllDraftOrders()
                .onStart {
                    _draftOrders.value = ApiState.Loading
                }
                .catch { e ->
                    _draftOrders.value = ApiState.Error(e.message ?: "Unknown error")
                }
                .collect { response ->
                    _draftOrders.value = ApiState.Success(response)
                }
        }
    }

    fun getDraftOrder(draftOrderId: Long) {
        viewModelScope.launch {
            repository.getDraftOrder(draftOrderId)
                .onStart {
                    _singleDraftOrders.value = ApiState.Loading
                }
                .catch { e ->
                    _singleDraftOrders.value = ApiState.Error(e.message ?: "Unknown error")
                }
                .collect { draftOrderResponse ->
                    _singleDraftOrders.value = ApiState.Success(draftOrderResponse)
                }
        }
    }

    fun createDraftOrder(draftOrder: DraftOrder) {
        viewModelScope.launch {
            repository.createDraftOrder(draftOrder)
                .onStart {
                    _singleDraftOrders.value = ApiState.Loading
                }
                .catch { e ->
                    _singleDraftOrders.value = ApiState.Error(e.message ?: "Failed to create draft order")
                }
                .collect { response ->
                    _singleDraftOrders.value = ApiState.Success(response)
                    fetchAllDraftOrders()
                }
        }
    }

    fun updateDraftOrder(draftOrderId: Long, draftOrder: DraftOrder) {
        viewModelScope.launch {
            repository.updateDraftOrder(draftOrderId, draftOrder)
                .onStart {
                    _singleDraftOrders.value = ApiState.Loading
                }
                .catch { e ->
                    _singleDraftOrders.value = ApiState.Error(e.message ?: "Failed to update draft order")
                }
                .collect { response ->
                    _singleDraftOrders.value = ApiState.Success(response)
                    fetchAllDraftOrders()
                }
        }
    }

    fun deleteDraftOrder(draftOrderId: Long) {
        viewModelScope.launch {
            try {
                repository.deleteDraftOrder(draftOrderId)
                fetchAllDraftOrders()
            } catch (e: Exception) {
                Log.e(TAG, "Failed to delete draft order: ${e.message}")
            }
        }
    }

    fun finalizeDraftOrder(draftOrderId: Long) {
        viewModelScope.launch {
            repository.finalizeDraftOrder(draftOrderId)
                .onStart {
                    _singleDraftOrders.value = ApiState.Loading
                }
                .catch { e ->
                    _singleDraftOrders.value = ApiState.Error(e.message ?: "Failed to finalize draft order")
                }
                .collect { response ->
                    _singleDraftOrders.value = ApiState.Success(response)
                    fetchAllDraftOrders()
                }
        }
    }


}