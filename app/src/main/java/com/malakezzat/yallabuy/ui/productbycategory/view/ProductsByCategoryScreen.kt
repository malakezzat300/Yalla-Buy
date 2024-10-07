package com.malakezzat.yallabuy.ui.productbycategory.view

import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.navigation.NavController
import com.malakezzat.yallabuy.ui.categories.view.CustomTopBarCategory
import com.malakezzat.yallabuy.ui.productbycategory.viewmodel.ProductsByCategoryViewModel

@Composable
fun ProductsByCategoryScreen(
    viewModel: ProductsByCategoryViewModel,
    navController: NavController
) {
    Scaffold(
        topBar = { CustomTopBarCategory(navController,"Products By Category") },
        containerColor = Color.White
    ) {

    }

}