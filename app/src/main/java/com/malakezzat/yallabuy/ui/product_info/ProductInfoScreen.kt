package com.malakezzat.yallabuy.ui.product_info

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
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.List
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderColors
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Text

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment

import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight

import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import coil.compose.rememberAsyncImagePainter
import coil.compose.rememberImagePainter
import com.malakezzat.yallabuy.data.remote.ApiState
import com.malakezzat.yallabuy.model.Product
import com.malakezzat.yallabuy.ui.Screen


@Composable
fun ProductInfoScreen (viewModel: ProductInfoViewModel, navController: NavController,productId : Long) {
    Column(
    modifier = Modifier
    .fillMaxSize()
    .padding(16.dp)
    ) {
        val productState by viewModel.searchProductsList.collectAsState()

        // Trigger the function to get the product by id
        LaunchedEffect(key1 = productId) {
            viewModel.getProductById(productId)
        }

        // UI based on the state
        when (productState) {
            is ApiState.Loading -> {
                // Show a loading indicator
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    CircularProgressIndicator()
                }
            }

            is ApiState.Success -> {
                // Show the product details
                val product = (productState as ApiState.Success<Product>).data
                ProductInfoSection(product)

                Spacer(modifier = Modifier.height(16.dp))

                ReviewsSection(product)

                Spacer(modifier = Modifier.height(16.dp))

                DescriptionSection(product)

                AddToFavoritesAndCart(navController)

               // NavigationButtons(navController)
            }

            is ApiState.Error -> {
                // Show the error message
                val errorMessage = (productState as ApiState.Error).message
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Text(text = "Error: $errorMessage", color = MaterialTheme.colorScheme.error)
                }
            }
        }

    }
}

@Composable
fun ProductInfoSection(product: Product) {

    Column {
        // Image Carousel (Placeholder for actual image carousel)
        LazyRow {
            items(product.images) { imageUrl ->
               // product.images.firstOrNull()?.let { image ->
                    Image(
                        painter = rememberAsyncImagePainter(imageUrl.src),
                        contentDescription = null,
                        modifier = Modifier
                            .size(300.dp)
                            .padding(4.dp)
                    )
              //  }
            }
        }

        Text(text = product.title, fontWeight = FontWeight.Bold, fontSize = 20.sp)
//        if (product.sizes.isNotEmpty()) {
//            Text(text = "Available Sizes: ${product.sizes.joinToString()}")
//        }
        Text(text = "Price: ${product.variants.get(0).price} EGP")
        Text(text = "Rating:  ⭐")
    }
}

@Composable
fun ReviewsSection(product: Product) {
    Column {
        Text(text = "Reviews", fontWeight = FontWeight.Bold, fontSize = 18.sp)
        LazyColumn {
//            items(product.reviews.take(3)) { review ->
//                ReviewItem(review)
//            }
//            item {
//                TextButton(onClick = { /* Navigate to full reviews */ }) {
//                    Text("View More")
//                }
//            }
        }
    }
}

//@Composable
//fun ReviewItem(review: Review) {
//    Card(
//        modifier = Modifier
//            .fillMaxWidth()
//            .padding(4.dp),
//        elevation = 4.dp
//    ) {
//        Column(modifier = Modifier.padding(8.dp)) {
//            Text(text = review.userName, fontWeight = FontWeight.Bold)
//            Text(text = review.comment)
//            Text(text = "Rating: ${review.rating} ⭐")
//        }
//    }
//}

@Composable
fun DescriptionSection(product: Product) {
    Text(text = "Description", fontWeight = FontWeight.Bold, fontSize = 18.sp)
    //ScrollableColumn {
        Text(text = product.body_html)
   // }
}

@Composable
fun AddToFavoritesAndCart(navController: NavController) {
    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
        Button(onClick = { /* Add to favorites logic */ }) {
            Text("Add to Favorites")
        }
        Button(onClick = { /*Add to cart logic*/ }) {
            Text("Add to Cart")
        }
    }
}

@Composable
fun NavigationButtons(navController: NavController) {
    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {

        Button(onClick = { navController.navigate("favorites") }) {
            Text("Favorites")
        }
        Button(onClick = { navController.navigate("cart") }) {
            Text("Shopping Cart")
        }
    }
}

@Composable
fun ProductInfoScreenPreview(){
   // ProductInfoScreen(Product(0,"","","","","", em,),NavController(LocalContext.current))
}