package com.malakezzat.yallabuy.ui.productbycategory.view

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.itemsIndexed
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import coil.compose.rememberAsyncImagePainter
import com.malakezzat.yallabuy.R
import com.malakezzat.yallabuy.data.remote.ApiState
import com.malakezzat.yallabuy.model.Product
import com.malakezzat.yallabuy.ui.Screen
import com.malakezzat.yallabuy.ui.categories.view.CustomTopBarCategory
import com.malakezzat.yallabuy.ui.home.view.LatestProductsSection
import com.malakezzat.yallabuy.ui.home.view.TAG
import com.malakezzat.yallabuy.ui.productbycategory.viewmodel.ProductsByCollectionIdViewModel
import com.malakezzat.yallabuy.ui.theme.AppColors

val TAG = "ProductsByCollectionIdS"
@Composable
fun ProductsByCategoryScreen(
    viewModel: ProductsByCollectionIdViewModel,
    navController: NavController,
    id: String?,
    body: String?
) {
    val productState by viewModel.productList.collectAsStateWithLifecycle()
    val categoryId: Long? = id?.toLongOrNull()

    LaunchedEffect(Unit) {
        //Log.d(TAG, categoriesState.toString())
        categoryId?.let {
        viewModel.getProductsByCollectionId(it)
    }
    }
    Scaffold(
        topBar = { body?.let { CustomTopBarCategory(navController, it) } },
        containerColor = Color.White
    ) {

        Column(
            modifier = Modifier
                .padding(it)
                .verticalScroll(rememberScrollState())
                .background(color = Color.White)
        ) {
            when (productState) {
                is ApiState.Loading -> {
                    CircularProgressIndicator(modifier = Modifier.align(Alignment.CenterHorizontally))
                }

                is ApiState.Success -> {
                    val products = (productState as ApiState.Success<List<Product>>).data
                    LatestProductsSectionById(products, navController)
                }

                is ApiState.Error -> {
                    Text(
                        text = "Error: ${(productState as ApiState.Error).message}",
                        color = Color.Red
                    )
                }
            }
        }

    }
}


@Composable
fun ProductsByBrandScreen(
    viewModel: ProductsByCollectionIdViewModel,
    navController: NavController,
    id: String?,
    body: String?
) {
    val productState by viewModel.productList.collectAsStateWithLifecycle()
    val categoryId: Long? = id?.toLongOrNull()

    LaunchedEffect(Unit) {
        //Log.d(TAG, categoriesState.toString())
        categoryId?.let {
            viewModel.getProductsByCollectionId(it)
        }
    }
    Scaffold(
        topBar = { body?.let { CustomTopBarCategory(navController, it) } },
        containerColor = Color.White
    ) {

        Column(
            modifier = Modifier
                .padding(it)
                .verticalScroll(rememberScrollState())
                .background(color = Color.White)
        ) {
            when (productState) {
                is ApiState.Loading -> {
                    CircularProgressIndicator(modifier = Modifier.align(Alignment.CenterHorizontally))
                }

                is ApiState.Success -> {
                    val products = (productState as ApiState.Success<List<Product>>).data
                    LatestProductsSectionById(products, navController)
                }

                is ApiState.Error -> {
                    Text(
                        text = "Error: ${(productState as ApiState.Error).message}",
                        color = Color.Red
                    )
                }
            }
        }

    }
}



@Composable
fun LatestProductsSectionById(products: List<Product>, navController: NavController) {
    //
    Column(modifier = Modifier.padding(16.dp)) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                "All Products",
                style = TextStyle(fontSize = 18.sp, fontWeight = FontWeight.Bold),
                modifier = Modifier.padding(26.dp)
            )
            Text(
                "SEE ALL", style = TextStyle(fontSize = 14.sp, fontWeight = FontWeight.Medium),
                color = AppColors.MintGreen
            )
        }

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(400.dp)
        ) {
            LazyVerticalGrid(
                columns = GridCells.Fixed(2),
                modifier = Modifier.fillMaxSize(),
                horizontalArrangement = Arrangement.spacedBy(10.dp),
                verticalArrangement = Arrangement.spacedBy(6.dp),
                contentPadding = PaddingValues(0.dp)

            ) {
                itemsIndexed(products) { _, product ->
                    ProductCard(product = product, navController)
                }
            }
        }
    }
}


@Composable
fun ProductCard(product: Product, navController: NavController) {
    Card(
        modifier = Modifier
            .width(150.dp)
            .clickable {
                navController.navigate("${Screen.ProductInfScreen.route}/${product.id}")
            },
        shape = RoundedCornerShape(30.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),

        ) {
        Box {
            // Background image of the product
            product.images.firstOrNull()?.let { image ->
                Image(
                    painter = rememberAsyncImagePainter(image.src),
                    contentDescription = "Product Image",
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(100.dp)
                        .clip(RoundedCornerShape(30.dp)), // Clip the image to match the card shape
                    contentScale = ContentScale.Crop
                )
            }

            // Wishlist icon positioned at the top right corner
            Image(
                painter = painterResource(id = R.drawable.wishlist),
                contentDescription = "wishlist",
                modifier = Modifier
                    .size(40.dp)
                    .align(Alignment.TopEnd)
                    .padding(5.dp)
            )

            // Column for product details
            Column(
                modifier = Modifier
                    .padding(top = 110.dp) // Ensure text does not overlap with the image
                    .padding(10.dp) // Add some padding around the text
            ) {
                Text(product.title, style = TextStyle(fontSize = 14.sp, fontWeight = FontWeight.Bold))
                Text(product.vendor, color = AppColors.MintGreen)
            }
        }
    }
}