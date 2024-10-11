package com.malakezzat.yallabuy.ui.shoppingcart.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import com.malakezzat.yallabuy.data.ProductsRepository
import com.malakezzat.yallabuy.data.remote.ApiState
import com.malakezzat.yallabuy.model.DiscountCodeResponse
import com.malakezzat.yallabuy.model.DiscountCodesResponse
import com.malakezzat.yallabuy.model.DraftOrder
import com.malakezzat.yallabuy.model.DraftOrderRequest
import com.malakezzat.yallabuy.model.DraftOrderResponse
import com.malakezzat.yallabuy.model.DraftOrdersResponse
import com.malakezzat.yallabuy.model.PriceRule
import com.malakezzat.yallabuy.model.PriceRuleResponse
import com.malakezzat.yallabuy.model.PriceRulesResponse
import com.malakezzat.yallabuy.model.VariantResponse
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch

class ShoppingCartViewModel(private val repository: ProductsRepository) : ViewModel() {

    private val TAG = "ShoppingCartViewModel"

    private val _draftOrders = MutableStateFlow<ApiState<DraftOrdersResponse>>(ApiState.Loading)
    val draftOrders : StateFlow<ApiState<DraftOrdersResponse>> get() = _draftOrders

    private val _singleDraftOrders = MutableStateFlow<ApiState<DraftOrderResponse>>(ApiState.Loading)
    val singleDraftOrders : StateFlow<ApiState<DraftOrderResponse>> get() = _singleDraftOrders

    private val _shoppingCartDraftOrder = MutableStateFlow<ApiState<DraftOrder>>(ApiState.Loading)
    val shoppingCartDraftOrder = _shoppingCartDraftOrder.asStateFlow()

    private val _variantId = MutableStateFlow<ApiState<VariantResponse>>(ApiState.Loading)
    val variantId = _variantId.asStateFlow()

    private val _priceRules = MutableStateFlow<ApiState<PriceRulesResponse>>(ApiState.Loading)
    val priceRules = _priceRules.asStateFlow()

    private val _priceRule = MutableStateFlow<ApiState<PriceRuleResponse>>(ApiState.Loading)
    val priceRule = _priceRule.asStateFlow()

    private val _discountCodes = MutableStateFlow<ApiState<DiscountCodesResponse>>(ApiState.Loading)
    val discountCodes = _discountCodes.asStateFlow()

    private val _discountCode = MutableStateFlow<ApiState<DiscountCodeResponse>>(ApiState.Loading)
    val discountCode = _discountCode.asStateFlow()

    init {
        getDraftOrders()
        getPriceRules()
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
                        it.note == "shoppingCart"
                    }
                    if (shoppingCartDraftOrder.isNotEmpty()) {
                        _shoppingCartDraftOrder.value = ApiState.Success(draftOrders.filter{
                            it.note == "shoppingCart"
                        }[0])
                    } else {
                        _shoppingCartDraftOrder.value = ApiState.Error("shoppingCart not found")
                    }
                }
        }
    }

//    fun getDraftOrder(draftOrderId: Long) {
//        viewModelScope.launch {
//            repository.getDraftOrder(draftOrderId)
//                .onStart {
//                    _singleDraftOrders.value = ApiState.Loading
//                }
//                .catch { e ->
//                    _singleDraftOrders.value = ApiState.Error(e.message ?: "Unknown error")
//                }
//                .collect { draftOrderResponse ->
//                    _singleDraftOrders.value = ApiState.Success(draftOrderResponse)
//                }
//        }
//    }

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
                Log.e(TAG, "Failed to delete draft order: ${e.message}")
            }
        }
    }

//    fun finalizeDraftOrder(draftOrderId: Long) {
//        viewModelScope.launch {
//            repository.finalizeDraftOrder(draftOrderId)
//                .onStart {
//                    _singleDraftOrders.value = ApiState.Loading
//                }
//                .catch { e ->
//                    _singleDraftOrders.value = ApiState.Error(e.message ?: "Failed to finalize draft order")
//                }
//                .collect { response ->
//                    _singleDraftOrders.value = ApiState.Success(response)
//                    getDraftOrders()
//                }
//        }
//    }


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

    fun getPriceRules(){
        viewModelScope.launch {
            repository.getPriceRules().onStart {
                _priceRules.value = ApiState.Loading
            }
                .catch { e ->
                    _priceRules.value = ApiState.Error(e.message ?: "Unknown error")
                }
                .collect { data ->
                    _priceRules.value = ApiState.Success(data)
                }
        }
    }

    fun getPriceRule(priceRuleId : Long){
        viewModelScope.launch {
            repository.getSinglePriceRule(priceRuleId).onStart {
                _priceRule.value = ApiState.Loading
            }
                .catch { e ->
                    _priceRule.value = ApiState.Error(e.message ?: "Unknown error")
                }
                .collect { data ->
                    _priceRule.value = ApiState.Success(data)
                }
        }
    }

    fun getDiscountCodes(priceRuleId : Long){
        viewModelScope.launch {
            repository.getDiscountCodes(priceRuleId).onStart {
                _discountCodes.value = ApiState.Loading
            }
                .catch { e ->
                    _discountCodes.value = ApiState.Error(e.message ?: "Unknown error")
                }
                .collect { data ->
                    _discountCodes.value = ApiState.Success(data)
                }
        }
    }

//    fun getDiscountCode(priceRuleId : Long,discountCodeId : Long){
//        viewModelScope.launch {
//            repository.getSingleDiscountCodes(priceRuleId,discountCodeId).onStart {
//                _discountCode.value = ApiState.Loading
//            }
//                .catch { e ->
//                    _discountCode.value = ApiState.Error(e.message ?: "Unknown error")
//                }
//                .collect { data ->
//                    _discountCode.value = ApiState.Success(data)
//                }
//        }
//    }

}