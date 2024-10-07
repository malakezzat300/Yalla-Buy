package com.malakezzat.yallabuy.ui.productbycategory.view

import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.navigation.NavController
import com.malakezzat.yallabuy.ui.categories.view.CustomTopBarCategory
import com.malakezzat.yallabuy.ui.productbycategory.viewmodel.ProductsByCollectionIdViewModel

@Composable
fun ProductsByCategoryScreen(
    viewModel: ProductsByCollectionIdViewModel,
    navController: NavController,
    id: String?,
    body: String?
) {
    val categoryId: Long? = id?.toLongOrNull()
    /*categoryId?.let {
    }*/
    Scaffold(
        topBar = { body?.let { CustomTopBarCategory(navController, it) } },
        containerColor = Color.White
    ) {

    }

}