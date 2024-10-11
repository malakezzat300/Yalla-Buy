package com.malakezzat.yallabuy.ui.productbycategory.view

import android.util.Log
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.scaleIn
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideInVertically
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
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
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
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
import com.malakezzat.yallabuy.data.util.CurrencyConverter
import com.malakezzat.yallabuy.model.Product
import com.malakezzat.yallabuy.ui.CustomTopBar
import com.malakezzat.yallabuy.ui.Screen
import com.malakezzat.yallabuy.ui.home.view.LatestProductsSection
import com.malakezzat.yallabuy.ui.home.view.TAG
import com.malakezzat.yallabuy.ui.productbycategory.viewmodel.ProductsByCollectionIdViewModel
import com.malakezzat.yallabuy.ui.theme.AppColors
import kotlinx.coroutines.delay

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
        topBar = { body?.let { CustomTopBar(navController, it) } },
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
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        CircularProgressIndicator(color = AppColors.Teal, modifier =Modifier.align(Alignment.Center) )

                    }
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
        topBar = { body?.let { CustomTopBar(navController, it) } },
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
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        CircularProgressIndicator(color = AppColors.Teal)
                    }
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
    Column(modifier = Modifier.padding(16.dp)) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(1000.dp)
                .padding(top = 25.dp)
        ) {
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(0.dp),
                verticalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                itemsIndexed(products) { index, product ->
                    val visibleState = remember { mutableStateOf(false) }
                    LaunchedEffect(Unit) {
                        delay(index * 200L)
                        visibleState.value = true
                    }

                    this@Column.AnimatedVisibility(
                        visible = visibleState.value,
                        enter = slideInVertically(
                            initialOffsetY = { it },
                            animationSpec = tween(durationMillis = 800)
                        ) + fadeIn(animationSpec = tween(durationMillis = 800)),
                        modifier = Modifier
                    ) {
                        ProductCard(product = product, navController)
                    }
                    Spacer(modifier = Modifier.height(10.dp))
                }
            }
        }
    }
}

@Composable
fun ProductCard(product: Product, navController: NavController) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable {
                navController.navigate("${Screen.ProductInfScreen.route}/${product.id}")
            },
        shape = RoundedCornerShape(30.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Box {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(10.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    product.images.firstOrNull()?.let { image ->
                        val imageVisibleState = remember { mutableStateOf(false) }
                        LaunchedEffect(Unit) {
                            delay(700)
                            imageVisibleState.value = true
                        }

                        AnimatedVisibility(
                            visible = imageVisibleState.value,
                            enter = scaleIn(initialScale = 0.8f) + slideInHorizontally(animationSpec = tween(durationMillis = 1000)),
                            modifier = Modifier.size(120.dp)
                        ) {
                            Image(
                                painter = rememberAsyncImagePainter(image.src),
                                contentDescription = "Product Image",
                                modifier = Modifier
                                    .size(120.dp)
                                    .clip(RoundedCornerShape(30.dp)),
                                contentScale = ContentScale.Crop
                            )
                        }
                    }

                    Column(
                        modifier = Modifier.padding(start = 10.dp)
                    ) {
                        val titleVisibleState = remember { mutableStateOf(false) }
                        LaunchedEffect(Unit) {
                            delay(1800)
                            titleVisibleState.value = true
                        }

                        AnimatedVisibility(
                            visible = titleVisibleState.value,
                            enter = fadeIn(animationSpec = tween(durationMillis = 1000))
                        ) {
                            Text(product.title, style = TextStyle(fontSize = 14.sp, fontWeight = FontWeight.Bold))
                        }

                        val vendorVisibleState = remember { mutableStateOf(false) }
                        LaunchedEffect(Unit) {
                            delay(2200)
                            vendorVisibleState.value = true
                        }

                        AnimatedVisibility(
                            visible = vendorVisibleState.value,
                            enter = fadeIn(animationSpec = tween(durationMillis = 1000))
                        ) {
                            Text(product.vendor, color = AppColors.Teal)
                        }
                    }
                }

            }

            val iconVisibleState = remember { mutableStateOf(false) }
            LaunchedEffect(Unit) {
                delay(2500)
                iconVisibleState.value = true
            }

            this@Card.AnimatedVisibility(
                visible = iconVisibleState.value,
                enter = scaleIn(initialScale = 0.8f),
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .padding(10.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.FavoriteBorder,
                    contentDescription = "Favorite",
                    tint = AppColors.Teal,
                    modifier = Modifier.size(35.dp)
                )
            }
        }



    }
}

