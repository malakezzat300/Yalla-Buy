package com.malakezzat.yallabuy.ui

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.malakezzat.yallabuy.ui.auth.view.LogInScreen
import com.malakezzat.yallabuy.ui.auth.viewmodel.SignUpViewModel
import com.malakezzat.yallabuy.ui.auth.viewmodel.SignUpViewModelFactory
import com.malakezzat.yallabuy.ui.auth.viewmodel.login.LogInViewModel
import com.malakezzat.yallabuy.ui.auth.viewmodel.login.LogInViewModelFactory
import com.malakezzat.yallabuy.ui.home.view.BottomNavigationBar
import com.malakezzat.yallabuy.ui.home.view.HomeScreen
import com.malakezzat.yallabuy.ui.home.viewmodel.HomeScreenViewModel
import com.malakezzat.yallabuy.ui.home.viewmodel.HomeScreenViewModelFactory
import com.malakezzat.yallabuy.ui.payment.view.CheckoutView
import com.malakezzat.yallabuy.ui.payment.view.OrderScreen
import com.malakezzat.yallabuy.ui.payment.view.PaymentScreen
import com.malakezzat.yallabuy.ui.payment.viewmodel.PaymentViewModel
import com.malakezzat.yallabuy.ui.payment.viewmodel.PaymentViewModelFactory
import com.malakezzat.yallabuy.ui.search.SearchScreen
import com.malakezzat.yallabuy.ui.search.SearchViewModel
import com.malakezzat.yallabuy.ui.search.SearchViewModelFactory
import com.malakezzat.yallabuy.ui.shoppingcart.view.ShoppingCartScreen
import com.malakezzat.yallabuy.ui.shoppingcart.viewmodel.ShoppingCartViewModel
import com.malakezzat.yallabuy.ui.shoppingcart.viewmodel.ShoppingCartViewModelFactory

@Composable
fun NavigationApp(
    homeScreenViewModelFactory: HomeScreenViewModelFactory,
    signUpViewModelFactory: SignUpViewModelFactory,
    logInViewModelFactory: LogInViewModelFactory,
    paymentViewModelFactory: PaymentViewModelFactory,
    searchViewModelFactory: SearchViewModelFactory,
    shoppingCartViewModelFactory: ShoppingCartViewModelFactory,
    navController: NavHostController = rememberNavController()
) {
        // Using Scaffold to manage layout
        Scaffold(
            bottomBar = {
                BottomNavigationBar(navController)
            }
        ) { paddingValues ->
            NavHost(navController = navController, startDestination = Screen.SplashScreen.route, Modifier.padding(paddingValues)) {
                composable(Screen.SplashScreen.route) {
                    SplashScreen(navController)
                }
                composable(Screen.HomeScreen.route) {
                    val viewModel: HomeScreenViewModel = viewModel(factory = homeScreenViewModelFactory)
                    HomeScreen(viewModel = viewModel, navController)
                }
                composable(Screen.SignUpScreen.route) {
                    val viewModel: SignUpViewModel = viewModel(factory = signUpViewModelFactory)
                    SignupScreen(viewModel, navController)
                }
                composable(Screen.LogInScreen.route) {
                    val viewModel: LogInViewModel = viewModel(factory = logInViewModelFactory)
                    LogInScreen(viewModel, navController)
                }
                composable(Screen.OrderScreen.route) {
                    val viewModel: PaymentViewModel = viewModel(factory = paymentViewModelFactory)
                    OrderScreen(viewModel, navController)
                }
                composable(
                    route = Screen.CheckoutScreen.route,
                    arguments = listOf(navArgument("orderId") { type = NavType.StringType })
                ) { backStackEntry ->
                    val orderId = backStackEntry.arguments?.getString("orderId")
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
            }
        }
    }



