package com.malakezzat.yallabuy.ui

import android.util.Log
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.malakezzat.yallabuy.ui.auth.view.LogInScreen
import com.malakezzat.yallabuy.ui.auth.viewmodel.SignUpViewModel
import com.malakezzat.yallabuy.ui.auth.viewmodel.SignUpViewModelFactory
import com.malakezzat.yallabuy.ui.auth.viewmodel.login.LogInViewModel
import com.malakezzat.yallabuy.ui.auth.viewmodel.login.LogInViewModelFactory
import com.malakezzat.yallabuy.ui.categories.view.CategoriesScreen
import com.malakezzat.yallabuy.ui.categories.viewmodel.CategoriesViewModel
import com.malakezzat.yallabuy.ui.categories.viewmodel.CategoriesViewModelFactory
import com.malakezzat.yallabuy.ui.home.view.BottomNavigationBar
import com.malakezzat.yallabuy.ui.home.view.HomeScreen
import com.malakezzat.yallabuy.ui.home.viewmodel.HomeScreenViewModel
import com.malakezzat.yallabuy.ui.home.viewmodel.HomeScreenViewModelFactory
import com.malakezzat.yallabuy.ui.orders.view.OrdersScreen
import com.malakezzat.yallabuy.ui.orders.viewmodel.OrdersViewModel
import com.malakezzat.yallabuy.ui.orders.viewmodel.OrdersViewModelFactory
import com.malakezzat.yallabuy.ui.payment.view.CheckoutView
import com.malakezzat.yallabuy.ui.payment.view.OrderScreen
import com.malakezzat.yallabuy.ui.payment.view.PaymentScreen
import com.malakezzat.yallabuy.ui.payment.viewmodel.PaymentViewModel
import com.malakezzat.yallabuy.ui.payment.viewmodel.PaymentViewModelFactory
import com.malakezzat.yallabuy.ui.product_info.ProductInfoScreen
import com.malakezzat.yallabuy.ui.product_info.ProductInfoViewModel
import com.malakezzat.yallabuy.ui.product_info.ProductInfoViewModelFactory
import com.malakezzat.yallabuy.ui.productbycategory.view.ProductsByBrandScreen
import com.malakezzat.yallabuy.ui.productbycategory.view.ProductsByCategoryScreen
import com.malakezzat.yallabuy.ui.productbycategory.viewmodel.ProductsByCollectionIdViewModel
import com.malakezzat.yallabuy.ui.productbycategory.viewmodel.ProductsByCollectionIdViewModelFactory
import com.malakezzat.yallabuy.ui.profile.view.ProfileScreen
import com.malakezzat.yallabuy.ui.profile.viewmodel.ProfileScreenViewModel
import com.malakezzat.yallabuy.ui.profile.viewmodel.ProfileScreenViewModelFactory
import com.malakezzat.yallabuy.ui.search.SearchScreen
import com.malakezzat.yallabuy.ui.search.SearchViewModel
import com.malakezzat.yallabuy.ui.search.SearchViewModelFactory
import com.malakezzat.yallabuy.ui.settings.view.AddressScreen
import com.malakezzat.yallabuy.ui.settings.view.MapScreen
import com.malakezzat.yallabuy.ui.settings.view.SettingsScreen
import com.malakezzat.yallabuy.ui.settings.viewmodel.SettingsViewModel
import com.malakezzat.yallabuy.ui.settings.viewmodel.SettingsViewModelFactory
import com.malakezzat.yallabuy.ui.shoppingcart.view.ShoppingCartScreen
import com.malakezzat.yallabuy.ui.shoppingcart.viewmodel.ShoppingCartViewModel
import com.malakezzat.yallabuy.ui.shoppingcart.viewmodel.ShoppingCartViewModelFactory
import com.malakezzat.yallabuy.ui.wishlist.WishlistScreen
import com.malakezzat.yallabuy.ui.wishlist.WishlistViewModel
import com.malakezzat.yallabuy.ui.wishlist.WishlistViewModelFactory

@Composable
fun NavigationApp(
    homeScreenViewModelFactory: HomeScreenViewModelFactory,
    signUpViewModelFactory: SignUpViewModelFactory,
    logInViewModelFactory: LogInViewModelFactory,
    paymentViewModelFactory: PaymentViewModelFactory,
    searchViewModelFactory: SearchViewModelFactory,
    shoppingCartViewModelFactory: ShoppingCartViewModelFactory,
    productInfoViewModelFactory: ProductInfoViewModelFactory,
    categoriesViewModelFactory: CategoriesViewModelFactory,
    productsByCollectionIdViewModelFactory: ProductsByCollectionIdViewModelFactory,
    wishlistViewModelFactory: WishlistViewModelFactory,
    ordersViewModelFactory: OrdersViewModelFactory,
    settingsViewModelFactory: SettingsViewModelFactory,
    profileScreenViewModelFactory: ProfileScreenViewModelFactory,
    navController: NavHostController = rememberNavController()
) {
    val navBackStackEntry = navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry.value?.destination?.route

    // List of routes where the bottom navigation should be hidden
    val bottomNavHiddenRoutes = listOf(
        Screen.SplashScreen.route,
        Screen.LogInScreen.route,
        Screen.SignUpScreen.route,
        Screen.SearchScreen.route,
//        Screen.AddressScreen.route,
//        Screen.MapScreen.route
    )

    // Using Scaffold to manage layout
    Scaffold(
        bottomBar = {
            // Show bottom navigation only if the current route is not in the hidden routes
            if (currentRoute !in bottomNavHiddenRoutes) {
                BottomNavigationBar(navController)
            }
        }
        ) { paddingValues ->

            NavHost(navController = navController, startDestination = Screen.SettingsScreen.route, Modifier.padding(paddingValues)) {
                composable(Screen.SplashScreen.route) {
                    SplashScreen(navController)
                }
                composable(Screen.HomeScreen.route) {
                    val viewModel: HomeScreenViewModel = viewModel(factory = homeScreenViewModelFactory)
                    HomeScreen(viewModel = viewModel, navController)
                }
                composable(Screen.CategoriesScreen.route) {
                    val viewModel: CategoriesViewModel = viewModel(factory = categoriesViewModelFactory)
                    CategoriesScreen(viewModel = viewModel, navController)
                }
                composable(Screen.OrdersScreen.route) {
                    val viewModel: OrdersViewModel = viewModel(factory = ordersViewModelFactory)
                    OrdersScreen(viewModel = viewModel, navController)
                }
                composable(Screen.ProfileScreen.route) {
                    val viewModel: ProfileScreenViewModel = viewModel(factory = profileScreenViewModelFactory)
                    ProfileScreen(viewModel = viewModel, navController)
                }
                composable(Screen.OrderScreen.route) {
                    val viewModel: PaymentViewModel = viewModel(factory = paymentViewModelFactory)
                    OrderScreen(viewModel, navController)
                }
                composable("${Screen.ProductsByCategoryScreen.route}/{categoryId}/{body_html}") { backStackEntry ->
                    val categoryId = backStackEntry.arguments?.getString("categoryId")
                    val bodyHtml = backStackEntry.arguments?.getString("body_html")
                    val viewModel: ProductsByCollectionIdViewModel = viewModel(factory = productsByCollectionIdViewModelFactory)
                    ProductsByCategoryScreen(viewModel = viewModel, navController, categoryId, bodyHtml)
                }
                composable("${Screen.ProductsByBrandScreen.route}/{categoryId}/{body_html}") { backStackEntry ->
                    val categoryId = backStackEntry.arguments?.getString("categoryId")
                    val bodyHtml = backStackEntry.arguments?.getString("body_html")
                    val viewModel: ProductsByCollectionIdViewModel = viewModel(factory = productsByCollectionIdViewModelFactory)
                    ProductsByBrandScreen(viewModel = viewModel, navController, categoryId, bodyHtml)
                }
                composable(Screen.SignUpScreen.route) {
                    val viewModel: SignUpViewModel = viewModel(factory = signUpViewModelFactory)
                    SignupScreen(viewModel, navController)
                }
                composable(Screen.LogInScreen.route) {
                    val viewModel: LogInViewModel = viewModel(factory = logInViewModelFactory)
                    LogInScreen(viewModel, navController)
                }

                composable(
                    route = Screen.CheckoutScreen.route,
                    arguments = listOf(navArgument("orderId") { type = NavType.StringType })
                ) { backStackEntry ->
                    val orderId = backStackEntry.arguments?.getString("orderId") ?: return@composable
                    val viewModel: PaymentViewModel = viewModel(factory = paymentViewModelFactory)
                    CheckoutView(viewModel, navController, orderId)
                }
                composable(
                    route = Screen.PaymentScreen.route,
                    arguments = listOf(navArgument("paymentKey") { type = NavType.StringType })
                ) { backStackEntry ->
                    val paymentKey = backStackEntry.arguments?.getString("paymentKey")
                    PaymentScreen(
                        onPaymentSuccess = { /* Handle success */ },
                        viewModel = viewModel(factory = paymentViewModelFactory),
                        navController = navController,
                        paymentKey = paymentKey
                    )
                }
                composable(Screen.SearchScreen.route) {
                    val viewModel: SearchViewModel = viewModel(factory = searchViewModelFactory)
                    SearchScreen(viewModel, navController)
                }
                composable(Screen.ShoppingScreen.route) {
                    val viewModel: ShoppingCartViewModel = viewModel(factory = shoppingCartViewModelFactory)
                    ShoppingCartScreen(viewModel, navController)
                }
                composable(
                    route = "${Screen.ProductInfScreen.route}/{productId}",
                    arguments = listOf(navArgument("productId") {
                        type = NavType.LongType
                    })
                ) { backStackEntry ->
                    val productId = backStackEntry.arguments?.getLong("productId") ?: 0L
                    val viewModel: ProductInfoViewModel = viewModel(factory = productInfoViewModelFactory)
                    ProductInfoScreen(productId = productId, viewModel = viewModel, navController = navController)
                }
                composable( route = Screen.SettingsScreen.route
                ) {
                    val viewModel: SettingsViewModel = viewModel(factory = settingsViewModelFactory)
                    SettingsScreen(navController,viewModel)
                }
                composable(
                    route = Screen.AddressScreen.route,
                    arguments = listOf(
                        navArgument("address") { type = NavType.StringType }
                    )
                    ) { backStackEntry ->
                    val address = backStackEntry.arguments?.getString("address") ?: " "
                    val viewModel: SettingsViewModel = viewModel(factory = settingsViewModelFactory)
                    AddressScreen(navController,viewModel,address)
                }
                composable(
                    route = Screen.MapScreen.route,
                    arguments = listOf(
                        navArgument("latitude") { type = NavType.FloatType },
                        navArgument("longitude") { type = NavType.FloatType }
                    )
                ) { backStackEntry ->
                    val latitude = backStackEntry.arguments?.getDouble("latitude") ?: 0.0
                    val longitude = backStackEntry.arguments?.getDouble("longitude") ?: 0.0
                    MapScreen(navController,latitude = latitude, longitude = longitude)
                }
                composable(Screen.WishlistScreen.route) {
                    val viewModel: WishlistViewModel = viewModel(factory = wishlistViewModelFactory)
                    WishlistScreen(viewModel, navController)
                }
            }
        }
    }




