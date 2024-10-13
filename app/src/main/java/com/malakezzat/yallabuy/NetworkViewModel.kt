package com.malakezzat.yallabuy

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class NetworkViewModel : ViewModel() {
    private val _isConnected = MutableLiveData<Boolean>(true)
    val isConnected: LiveData<Boolean> = _isConnected

    fun updateNetworkStatus(connected: Boolean) {
        // Use postValue instead of setValue to update LiveData from a background thread
        _isConnected.postValue(connected)
    }
}