package com.malakezzat.yallabuy.ui

sealed class Screen(val route : String) {
    data object HomeScreen:Screen("homeScreen")
    data object SignUpScreen:Screen("signUpScreen")
    data object LogInScreen:Screen("logInScreen")
    data object SearchScreen:Screen("searchScreen")
}