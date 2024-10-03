package com.malakezzat.yallabuy.ui

sealed class Screen(val route:String) {
    data object HomeScreen:Screen("homeScreen")
}