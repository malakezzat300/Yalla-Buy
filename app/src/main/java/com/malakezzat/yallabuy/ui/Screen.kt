package com.malakezzat.yallabuy.ui

sealed class Screen(val route: String) {
    data object HomeScreen : Screen("homeScreen")
    data object SplashScreen : Screen("splashScreen")
    data object SignUpScreen : Screen("signUpScreen")
    data object LogInScreen : Screen("logInScreen")
    data object OrderScreen : Screen("orderScreen")
    data object CheckoutScreen : Screen("checkoutScreen/{orderId}")
    data object PaymentScreen : Screen("payment_screen/{paymentKey}")
    data object SearchScreen : Screen("searchScreen")
    data object ShoppingScreen : Screen("shoppingScreen")
    data object ProductInfScreen : Screen("productInfo")
    data object SettingsScreen : Screen("settingsScreen")
    data object AddressScreen : Screen("addressScreen/{address}") {
        fun createRoute(address: String) = "addressScreen/$address"
    }

    data object MapScreen : Screen("mapScreen/{latitude}/{longitude}") {
        fun createRoute(latitude: Float, longitude: Float) = "mapScreen/$latitude/$longitude"
    }

    data object CategoriesScreen : Screen("categoriesScreen")
    data object ProductsByCategoryScreen : Screen("productsByCategoryScreen")
    data object ProductsByBrandScreen : Screen("productsByBrandScreen")
    data object OrdersScreen : Screen("ordersScreen")
}