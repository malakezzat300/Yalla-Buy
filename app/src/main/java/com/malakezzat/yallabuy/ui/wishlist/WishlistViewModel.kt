package com.malakezzat.yallabuy.ui.wishlist

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import com.malakezzat.yallabuy.data.ProductsRepository
import com.malakezzat.yallabuy.data.remote.ApiState
import com.malakezzat.yallabuy.model.DraftOrder
import com.malakezzat.yallabuy.model.DraftOrderRequest
import com.malakezzat.yallabuy.model.DraftOrderResponse
import com.malakezzat.yallabuy.model.DraftOrdersResponse
import com.malakezzat.yallabuy.model.VariantResponse
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch

class WishlistViewModel(var repository : ProductsRepository) : ViewModel() {
    private val _draftOrders = MutableStateFlow<ApiState<DraftOrdersResponse>>(ApiState.Loading)
    val draftOrders : StateFlow<ApiState<DraftOrdersResponse>> get() = _draftOrders

    private val _wishlistDraftOrder = MutableStateFlow<ApiState<DraftOrder>>(ApiState.Loading)
    val wishlistDraftOrder = _wishlistDraftOrder.asStateFlow()

    private val _variantId = MutableStateFlow<ApiState<VariantResponse>>(ApiState.Loading)
    val variantId = _variantId.asStateFlow()

    private val _singleDraftOrders = MutableStateFlow<ApiState<DraftOrderResponse>>(ApiState.Loading)
    val singleDraftOrders : StateFlow<ApiState<DraftOrderResponse>> get() = _singleDraftOrders

    init {
        getDraftOrders()
    }
    fun getDraftOrders(){
        viewModelScope.launch {
            repository.getAllDraftOrders().onStart {
                _draftOrders.value = ApiState.Loading
            }
                .catch { e ->
                    _draftOrders.value = ApiState.Error(e.message ?: "Unknown error")
                }
                .collect { draftOrdersResponse ->
                    val draftOrders = draftOrdersResponse.draft_orders.filter {
                        it.email == FirebaseAuth.getInstance().currentUser?.email
                    }
                    val shoppingCartDraftOrder = draftOrders.filter{
                        it.note == "wishList"
                    }
                    if (shoppingCartDraftOrder.isNotEmpty()) {
                        _wishlistDraftOrder.value = ApiState.Success(draftOrders.filter{
                            it.note == "wishList"
                        }[0])
                    } else {
                        _wishlistDraftOrder.value = ApiState.Error("wishlist not found")
                    }
                }
        }
    }
    fun getVariantById(variantId : Long){
        viewModelScope.launch {
            repository.getVariantById(variantId).onStart {
                _variantId.value = ApiState.Loading
            }
                .catch { e ->
                    _variantId.value = ApiState.Error(e.message ?: "Unknown error")
                }
                .collect { customerResponse ->
                    _variantId.value = ApiState.Success(customerResponse)
                }
        }
    }

    fun updateDraftOrder(draftOrderId: Long, draftOrder: DraftOrderRequest) {
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
                    getDraftOrders()
                }
        }
    }

    fun deleteDraftOrder(draftOrderId: Long) {
        viewModelScope.launch {
            try {
                repository.deleteDraftOrder(draftOrderId)
                getDraftOrders()
            } catch (e: Exception) {
                Log.e("TAG", "Failed to delete draft order: ${e.message}")
            }
        }
    }
}