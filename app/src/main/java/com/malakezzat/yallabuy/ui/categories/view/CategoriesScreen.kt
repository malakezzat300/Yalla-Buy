package com.malakezzat.yallabuy.ui.categories.view

import android.util.Log
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.itemsIndexed
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import coil.compose.rememberAsyncImagePainter
import com.malakezzat.yallabuy.data.remote.ApiState
import com.malakezzat.yallabuy.model.CustomCollection
import com.malakezzat.yallabuy.ui.CustomTopBar
import com.malakezzat.yallabuy.ui.Screen
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
        Box( // Use Box to handle proper alignment
            modifier = Modifier
                .padding(it)
                .fillMaxSize()
                .background(color = Color.White)
        ) {
            when (categoriesState) {
                is ApiState.Loading -> {
                    // Center the progress bar inside the Box
                    CircularProgressIndicator(
                        color = AppColors.Teal,
                        modifier = Modifier.align(Alignment.Center)
                    )
                }

                is ApiState.Success -> {
                    val categories =
                        (categoriesState as ApiState.Success<List<CustomCollection>>).data
                    Column(
                        modifier = Modifier
                            .verticalScroll(rememberScrollState())
                            .fillMaxSize()
                    ) {
                        CategoriesSectionInCategoriesScreen(categories, navController)
                        Log.d(TAG, "$categoriesState")
                    }
                }

                is ApiState.Error -> {
                    // Log error and center progress bar as a fallback
                    Log.i(TAG, "Error: ${(categoriesState as ApiState.Error).message}")
                    Column(
                        modifier = Modifier
                            .fillMaxSize(),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {
                        androidx.compose.material3.CircularProgressIndicator(
                            color = AppColors.Teal
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
        LazyColumn(
            modifier = Modifier.fillMaxSize()
                .height(900.dp)
            ,
            verticalArrangement = Arrangement.spacedBy(6.dp),
            contentPadding = PaddingValues(0.dp)
        ) {
            itemsIndexed(categories) { _, category ->
                CategoryItem2(category, navController)
            }
        }
    }
}

@Composable
fun CategoryItem2(category: CustomCollection, navController: NavController) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable {
                navController.navigate("${Screen.ProductsByCategoryScreen.route}/${category.id}/${category.body_html}")
            },
        shape = RoundedCornerShape(28.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 8.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        border = BorderStroke(1.dp, Color(0xFFE0E0E0))
    ) {
        Row(
            modifier = Modifier.padding(16.dp).fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Image(
                painter = rememberAsyncImagePainter(category.image?.src),
                contentDescription = "category item",
                modifier = Modifier
                    .width(200.dp)
                    .height(130.dp)
                    .clip(RoundedCornerShape(16.dp)),
                contentScale = ContentScale.FillBounds
            )
            Spacer(modifier = Modifier.width(10.dp))
            Box(
                modifier = Modifier.weight(1f).fillMaxHeight(),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = category.title,
                    fontSize = 20.sp,
                    style = MaterialTheme.typography.bodyMedium,
                    color = AppColors.Teal
                )
            }
        }
    }
}
