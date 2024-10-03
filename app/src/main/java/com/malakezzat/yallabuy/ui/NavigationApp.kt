package com.malakezzat.yallabuy.ui

import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.Navigation
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.malakezzat.yallabuy.ui.home.view.HomeScreen
import com.malakezzat.yallabuy.ui.home.viewmodel.HomeScreenViewModel
import com.malakezzat.yallabuy.ui.home.viewmodel.HomeScreenViewModelFactory

@Composable
fun NavigationApp(

    homeScreenViewModelFactory: HomeScreenViewModelFactory,
    navController: NavHostController = rememberNavController()

) {
    NavHost(navController = navController, startDestination = Screen.HomeScreen.route){
        composable(Screen.HomeScreen.route) {
            val viewModel: HomeScreenViewModel = viewModel(factory = homeScreenViewModelFactory)
            HomeScreen(viewModel = viewModel, navController)
        }
    }


    }