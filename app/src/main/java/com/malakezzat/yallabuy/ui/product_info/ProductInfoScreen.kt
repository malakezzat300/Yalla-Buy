package com.malakezzat.yallabuy.ui.product_info

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.Remove
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment

import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.tooling.preview.Preview

import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import coil.compose.rememberAsyncImagePainter
import com.google.firebase.auth.FirebaseAuth
import com.malakezzat.yallabuy.R
import com.malakezzat.yallabuy.data.remote.ApiState
import com.malakezzat.yallabuy.data.util.CurrencyConverter
import com.malakezzat.yallabuy.model.Customer
import com.malakezzat.yallabuy.model.DraftOrder
import com.malakezzat.yallabuy.model.LineItem
import com.malakezzat.yallabuy.model.Product
import com.malakezzat.yallabuy.model.*
import java.util.Properties


@Composable
fun ProductInfoScreen (viewModel: ProductInfoViewModel, navController: NavController,productId : Long) {
    Column(
    modifier = Modifier
        .fillMaxSize()
        .padding(16.dp)
    ) {
        val productState by viewModel.searchProductsList.collectAsState()
        val customerId by viewModel.customerId.collectAsState()
        val draftOrderId by viewModel.draftOrderId.collectAsState()
        var draftOrderIdSaved by remember { mutableLongStateOf(0L) }
        var shoppingCartDraftOrder by remember { mutableStateOf(DraftOrder(0L,"", listOf(),"")) }
        var wishListDraftOrder by remember { mutableStateOf(DraftOrder(0L,"", listOf(),"")) }
        val shoppingCartDraftOrderState by viewModel.shoppingCartDraftOrder.collectAsState()
        val wishListDraftOrderState by viewModel.wishListDraftOrder.collectAsState()


        when(draftOrderId){
            is ApiState.Error -> Log.i("draftOrderTest", "ProductInfoScreen: draftOrder ${(draftOrderId as ApiState.Error).message}")
            ApiState.Loading -> {}
            is ApiState.Success ->
                draftOrderIdSaved = (draftOrderId as ApiState.Success).data.draft_order.id ?: 0L
        }

//        when(customerId){
//            is ApiState.Error -> Log.i("draftOrderTest", "ProductInfoScreen: customer ${(customerId as ApiState.Error).message}")
//            ApiState.Loading -> {}
//            is ApiState.Success -> Log.i("draftOrderTest", "ProductInfoScreen: customer ${(customerId as ApiState.Success).data.customers[0].id}")
//        }

        when(shoppingCartDraftOrderState){
            is ApiState.Error -> Log.i("draftOrderTest", "ProductInfoScreen: draftOrder ${(shoppingCartDraftOrderState as ApiState.Error).message}")
            ApiState.Loading -> {}
            is ApiState.Success -> shoppingCartDraftOrder = (shoppingCartDraftOrderState as ApiState.Success).data
        }

        when(wishListDraftOrderState){
            is ApiState.Error -> Log.i("draftOrderTest", "ProductInfoScreen: draftOrder ${(wishListDraftOrderState as ApiState.Error).message}")
            ApiState.Loading -> {}
            is ApiState.Success -> wishListDraftOrder = (wishListDraftOrderState as ApiState.Success).data
        }


        // Trigger the function to get the product by id
        LaunchedEffect(key1 = productId) {
            viewModel.getProductById(productId)
//            viewModel.getCustomerId(FirebaseAuth.getInstance().currentUser?.email.toString())
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
                //Log.i("productTest", "ProductInfoScreen: ${(productState as ApiState.Success<Product>).data.toString()}")
                // Show the product details
                val product = (productState as ApiState.Success<Product>).data
                ProductInfoSection(product)

                Spacer(modifier = Modifier.height(16.dp))

                ReviewsSection(product)

                Spacer(modifier = Modifier.height(16.dp))

                DescriptionSection(product)
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                AddToCart(
                    viewModel,
                    product,
                    FirebaseAuth.getInstance().currentUser?.email.toString(),
                    shoppingCartDraftOrder)
                AddToFavorites(
                    viewModel,
                    product,
                    FirebaseAuth.getInstance().currentUser?.email.toString(),
                    wishListDraftOrder)
                }
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
    //Currency
    val context = LocalContext.current
    CurrencyConverter.initialize(context)

    Column {
        // Image Carousel (Placeholder for actual image carousel)
        LazyRow {
            items(product.images) { imageUrl ->
               // product.images.firstOrNull()?.let { image ->
                Box(modifier = Modifier.fillMaxWidth()
                ) {
                    Image(
                        painter = rememberAsyncImagePainter(imageUrl.src),
                        contentDescription = null,
                        modifier = Modifier
                            .width(400.dp)
                            .height(300.dp)

                    )
                }

              //  }
            }
        }

        Text(text = product.title, fontWeight = FontWeight.Bold, fontSize = 20.sp)
//        if (product.sizes.isNotEmpty()) {
//            Text(text = "Available Sizes: ${product.sizes.joinToString()}")
//        }
        val price = product.variants.get(0).price
        CurrencyConverter.changeCurrency(price.toDouble())?.let {
            Text(text = it,
                style = MaterialTheme.typography.bodyMedium)
        }
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
fun AddToFavorites(viewModel: ProductInfoViewModel,product : Product,email : String,oldDraftOrder : DraftOrder){
    Button(onClick = {
        val properties = listOf(Property(name = "imageUrl",value = product.image.src))
        Log.i("propertiesTest", "AddToCart: $properties")
        if(oldDraftOrder.id == 0L) {
            val lineItems = listOf(LineItem(product.title,product.variants[0].price,product.variants[0].id,1, properties = properties,product.id?:0))
            val draftOrder = DraftOrder(note = "wishList", line_items = lineItems, email = email)
            val draftOrderRequest = DraftOrderRequest(draftOrder)
            viewModel.createDraftOrder(draftOrderRequest)
        } else {
            if(!oldDraftOrder.line_items.contains(LineItem(product.title,product.variants[0].price,product.variants[0].id,1, properties = properties,product.id?:0))) {
                val lineItems = oldDraftOrder.line_items + listOf(
                    LineItem(
                        product.title,
                        product.variants[0].price,
                        product.variants[0].id,
                        1,
                        properties = properties,
                        product.id?:0
                    )
                )
                val draftOrder = DraftOrder(note = "wishList", line_items = lineItems, email = email)
                val draftOrderRequest = DraftOrderRequest(draftOrder)
                oldDraftOrder.id?.let { viewModel.updateDraftOrder(it,draftOrderRequest) }
            }
        }
    }) {
        Text("Add to Favorites")
    }
}

@Composable
fun AddToCart(viewModel: ProductInfoViewModel,product : Product,email : String,oldDraftOrder : DraftOrder) {
        Button(onClick = {
            val properties = listOf(Property(name = "imageUrl",value = product.image.src))
            Log.i("propertiesTest", "AddToCart: $properties")
            if(oldDraftOrder.id == 0L) {
                val lineItems = listOf(LineItem(product.title,product.variants[0].price,product.variants[0].id,1, properties = properties,product.id?:0))
                val draftOrder = DraftOrder(note = "shoppingCart", line_items = lineItems, email = email)
                val draftOrderRequest = DraftOrderRequest(draftOrder)
                viewModel.createDraftOrder(draftOrderRequest)
            } else {
                if(!oldDraftOrder.line_items.contains(LineItem(product.title,product.variants[0].price,product.variants[0].id,1, properties = properties,product.id?:0))) {
                    val lineItems = oldDraftOrder.line_items + listOf(
                        LineItem(
                            product.title,
                            product.variants[0].price,
                            product.variants[0].id,
                            1,
                            properties = properties,
                            product.id?:0
                        )
                    )
                    val draftOrder = DraftOrder(note = "shoppingCart", line_items = lineItems, email = email)
                    val draftOrderRequest = DraftOrderRequest(draftOrder)
                    oldDraftOrder.id?.let { viewModel.updateDraftOrder(it,draftOrderRequest) }
                }
            }
        }) {
            Text("Add to Cart")
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
@Preview
@Composable
fun ProductInfoScreenPreview(){
//    Column(
//        modifier = Modifier
//            .fillMaxSize()
//            .background(Color.White)
//            .padding(16.dp)
//    ) {
//        TopAppBar(
//            title = { Text("Product Details") },
//            navigationIcon = {
//                IconButton(onClick = { /*navController.popBackStack()*/ }) {
//                    Icon(painterResource(id = R.drawable.ic_back), contentDescription = "Back")
//                }
//            },
//            actions = {
//                IconButton(onClick = { /* favorite logic */ }) {
//                    Icon(painterResource(id = R.drawable.wishlist), contentDescription = "Favorite")
//                }
//            }
//        )
//
//        Spacer(modifier = Modifier.height(16.dp))
//
//        // Product Image
//        Image(
//            painter = rememberAsyncImagePainter("https://your-image-url"),
//            contentDescription = null,
//            modifier = Modifier
//                .fillMaxWidth()
//                .aspectRatio(1f)
//                .clip(RoundedCornerShape(16.dp))
//                .background(Color.LightGray)
//        )
//
//        Spacer(modifier = Modifier.height(16.dp))
//
//        // Product title and price
//        Text(
//            text = "Loop Silicone Strong Magnetic Watch",
//            fontSize = 22.sp,
//            fontWeight = FontWeight.Bold
//        )
//
//        Row(
//            verticalAlignment = Alignment.CenterVertically
//        ) {
//            Icon(
//                painter = painterResource(id = R.drawable.wishlist),
//                contentDescription = "Star Icon",
//                tint = Color.Yellow,
//                modifier = Modifier.size(16.dp)
//            )
//            Text(text = "4.5 (2,495 reviews)", fontSize = 14.sp)
//        }
//
//        Spacer(modifier = Modifier.height(8.dp))
//
//        Row(
//            verticalAlignment = Alignment.CenterVertically
//        ) {
//            Text(
//                text = "$15.25",
//                fontSize = 20.sp,
//                fontWeight = FontWeight.Bold,
//                color = Color.Green
//            )
//            Spacer(modifier = Modifier.width(8.dp))
//            Text(
//                text = "$20.00",
//                fontSize = 16.sp,
//                textDecoration = TextDecoration.LineThrough,
//                color = Color.Gray
//            )
//        }
//
//        Spacer(modifier = Modifier.height(8.dp))
//
//        // Color options
//        Row(
//            modifier = Modifier.padding(vertical = 8.dp)
//        ) {
//            listOf(Color.Black, Color.Blue, Color.Gray).forEach { color ->
//                Box(
//                    modifier = Modifier
//                        .size(40.dp)
//                        .clip(CircleShape)
//                        .background(color)
//                        .clickable { /* select color */ }
//                        .padding(8.dp)
//                )
//                Spacer(modifier = Modifier.width(8.dp))
//            }
//        }
//
//        Spacer(modifier = Modifier.height(16.dp))
//
//        // Quantity Selector
//        Row(
//            verticalAlignment = Alignment.CenterVertically
//        ) {
//            IconButton(onClick = { /* decrease quantity */ }) {
//                Icon(Icons.Default.Remove, contentDescription = "Decrease")
//            }
//            Text(text = "1", fontSize = 16.sp)
//            IconButton(onClick = { /* increase quantity */ }) {
//                Icon(Icons.Default.Add, contentDescription = "Increase")
//            }
//        }
//
//        Spacer(modifier = Modifier.height(16.dp))
//
//        // Buttons
//        Row(
//            horizontalArrangement = Arrangement.SpaceBetween,
//            modifier = Modifier.fillMaxWidth()
//        ) {
//            Button(
//                onClick = { /* Buy now action */ },
//                colors = ButtonDefaults.buttonColors(containerColor = Color.Black),
//                modifier = Modifier.weight(1f).padding(end = 8.dp)
//            ) {
//                Text("Buy Now", color = Color.White)
//            }
//            Button(
//                onClick = { /* Add to cart action */ },
//                colors = ButtonDefaults.buttonColors(containerColor = Color.Gray),
//                modifier = Modifier.weight(1f)
//            ) {
//                Text("Add To Cart", color = Color.White)
//            }
//        }
//    }
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
    ) {
        // Top Bar
        TopAppBar(
            title = { },
            navigationIcon = {
                IconButton(onClick = { /* Handle back press */ }) {
                    Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                }
            },
            actions = {
                IconButton(onClick = { /* Handle favorite */ }) {
                    Icon(Icons.Default.FavoriteBorder, contentDescription = "Favorite")
                }
            },
            backgroundColor = Color.White,
            elevation = 0.dp
        )

        // Product Image with carousel
        Box(modifier = Modifier.fillMaxWidth()) {
            // Image Placeholder
            Image(
                painter = rememberAsyncImagePainter("https://via.placeholder.com/300"),
                contentDescription = "Product Image",
                modifier = Modifier
                    .fillMaxWidth()
                    .aspectRatio(1f)
            )

            // Image Indicator (dots)
            Row(
                horizontalArrangement = Arrangement.Center,
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .padding(8.dp)
            ) {
                repeat(5) {
                    Box(
                        modifier = Modifier
                            .size(8.dp)
                            .clip(CircleShape)
                            .background(if (it == 0) Color.Gray else Color.LightGray)
                            .padding(4.dp)
                    )
                }
            }
        }

        // Product Title and Info
        Column(modifier = Modifier.padding(16.dp) ) {
            Text(
                text = "Loop Silicone Strong Magnetic Watch",
                style = MaterialTheme.typography.bodySmall,
                fontWeight = FontWeight.Bold
            )

            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    Icons.Filled.Star,
                    contentDescription = "Rating Star",
                    tint = Color(0xFFFFD700), // Gold color
                    modifier = Modifier.size(16.dp)
                )
                Text(
                    text = "4.5 (2,495 reviews)",
                    style = MaterialTheme.typography.bodyMedium,
                    modifier = Modifier.padding(start = 4.dp)
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                    text = "$15.25",
                    style = MaterialTheme.typography.bodySmall.copy(color = Color.Green),
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "$20.00",
                    style = MaterialTheme.typography.bodyMedium.copy(
                        textDecoration = TextDecoration.LineThrough,
                        color = Color.Gray
                    )
                )
            }
        }

        // Color Options
        Row(
            modifier = Modifier
                .padding(horizontal = 16.dp)
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            listOf(Color.Black, Color.Blue, Color.Gray, Color.Cyan, Color.Cyan).forEach { color ->
                Box(
                    modifier = Modifier
                        .size(36.dp)
                        .clip(CircleShape)
                        .background(color)
                        .border(
                            width = 1.dp,
                            color = if (color == Color.Black) Color.Blue else Color.Transparent,
                            shape = CircleShape
                        )
                        .clickable { /* Handle color selection */ }
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Quantity Selector
        Row(
            modifier = Modifier
                .padding(horizontal = 16.dp)
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(text = "Quantity", style = MaterialTheme.typography.bodyMedium)
            Row(verticalAlignment = Alignment.CenterVertically) {
                IconButton(onClick = { /* Decrease quantity */ }) {
                    Icon(Icons.Default.Remove, contentDescription = "Decrease")
                }
                Text(text = "1", style = MaterialTheme.typography.bodyMedium)
                IconButton(onClick = { /* Increase quantity */ }) {
                    Icon(Icons.Default.Add, contentDescription = "Increase")
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Action Buttons (Buy Now and Add to Cart)
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Button(
                onClick = { /* Handle Buy Now */ },
                modifier = Modifier
                    .weight(1f)
                    .padding(end = 8.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color.Black)
            ) {
                Text(text = "Buy Now", color = Color.White)
            }

            Button(
                onClick = { /* Handle Add to Cart */ },
                modifier = Modifier.weight(1f),
                colors = ButtonDefaults.buttonColors(containerColor = Color.Gray)
            ) {
                Text(text = "Add to Cart", color = Color.White)
            }
        }
    }
}