package com.malakezzat.yallabuy.ui.categories.view

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.itemsIndexed
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.malakezzat.yallabuy.data.remote.ApiState
import com.malakezzat.yallabuy.model.CustomCollection
import com.malakezzat.yallabuy.ui.CustomTopBar
import com.malakezzat.yallabuy.ui.categories.viewmodel.CategoriesViewModel
import com.malakezzat.yallabuy.ui.home.view.CategoryItem
import com.malakezzat.yallabuy.ui.theme.AppColors

val TAG = "CategoriesScreen"

@Composable
fun CategoriesScreen(
    viewModel: CategoriesViewModel,
    navController: NavController
) {
    // Collect categories state
    val categoriesState by viewModel.categoriesList.collectAsStateWithLifecycle()

    // Launch effect to trigger the API call only if necessary
    LaunchedEffect(categoriesState) {
        if (categoriesState !is ApiState.Success && categoriesState !is ApiState.Loading) {
            viewModel.getAllCategories()
        }
    }

    Scaffold(
        topBar = { CustomTopBar(navController, "Categories") },
        containerColor = Color.White
    ) {
        Column(
            modifier = Modifier
                .padding(it)
                .verticalScroll(rememberScrollState())
                .background(color = Color.White)
        ) {
            when (categoriesState) {
                is ApiState.Loading -> {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        CircularProgressIndicator(
                            color = AppColors.Teal,
                            modifier = Modifier.align(Alignment.Center)
                        )
                    }
                }

                is ApiState.Success -> {
                    val categories =
                        (categoriesState as ApiState.Success<List<CustomCollection>>).data
                    CategoriesSectionInCategoriesScreen(categories, navController)
                    Log.d(TAG, "$categoriesState")
                }

                is ApiState.Error -> {

                    Log.i(TAG, "Error: ${(categoriesState as ApiState.Error).message}")
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        /*Text(
                            text = "Error: ${(categoriesState as ApiState.Error).message}",
                            color = Color.Red
                        )*/
                        CircularProgressIndicator(
                            color = AppColors.Teal,
                            modifier = Modifier.align(Alignment.Center)
                        )
                    }
                }
            }
        }
    }
}


@Composable
fun CategoriesSectionInCategoriesScreen(
    categories: List<CustomCollection>,
    navController: NavController
) {
    //categories: List<CustomCollection>
    Log.d(com.malakezzat.yallabuy.ui.home.view.TAG, "3. ${categories}")
    Column(modifier = Modifier.padding(16.dp)) {

        Spacer(modifier = Modifier.height(18.dp))
        LazyVerticalGrid(
            columns = GridCells.Fixed(2),
            modifier = Modifier
                .fillMaxSize()
                .height(600.dp),
            horizontalArrangement = Arrangement.spacedBy(10.dp),
            verticalArrangement = Arrangement.spacedBy(6.dp),
            contentPadding = PaddingValues(0.dp)

        ) {
            itemsIndexed(categories) { _, category ->
                CategoryItem(category, navController)
            }
        }
    }
}



