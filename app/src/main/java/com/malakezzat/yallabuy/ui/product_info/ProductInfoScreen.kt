package com.malakezzat.yallabuy.ui.product_info

import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
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
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material.icons.sharp.Favorite
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
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
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import coil.compose.rememberAsyncImagePainter
import com.google.firebase.auth.FirebaseAuth
import com.malakezzat.yallabuy.data.remote.ApiState
import com.malakezzat.yallabuy.data.util.CurrencyConverter
import com.malakezzat.yallabuy.model.DraftOrder
import com.malakezzat.yallabuy.model.LineItem
import com.malakezzat.yallabuy.model.Product
import com.malakezzat.yallabuy.model.*
import com.malakezzat.yallabuy.ui.CustomTopBar
import com.malakezzat.yallabuy.ui.theme.AppColors


//@Composable
//fun ProductInfoScreen (viewModel: ProductInfoViewModel, navController: NavController,productId : Long) {
//    Column(
//        modifier = Modifier
//            .fillMaxSize()
//            .padding(16.dp)
//    ) {
//        val productState by viewModel.searchProductsList.collectAsState()
//        val customerId by viewModel.customerId.collectAsState()
//        val draftOrderId by viewModel.draftOrderId.collectAsState()
//        var draftOrderIdSaved by remember { mutableLongStateOf(0L) }
//        var shoppingCartDraftOrder by remember { mutableStateOf(DraftOrder(0L,"", listOf(),"")) }
//        var wishListDraftOrder by remember { mutableStateOf(DraftOrder(0L,"", listOf(),"")) }
//        val shoppingCartDraftOrderState by viewModel.shoppingCartDraftOrder.collectAsState()
//        val wishListDraftOrderState by viewModel.wishListDraftOrder.collectAsState()
//
//
//        when(draftOrderId){
//            is ApiState.Error -> Log.i("draftOrderTest", "ProductInfoScreen: draftOrder ${(draftOrderId as ApiState.Error).message}")
//            ApiState.Loading -> {}
//            is ApiState.Success ->
//                draftOrderIdSaved = (draftOrderId as ApiState.Success).data.draft_order.id ?: 0L
//        }
//
////        when(customerId){
////            is ApiState.Error -> Log.i("draftOrderTest", "ProductInfoScreen: customer ${(customerId as ApiState.Error).message}")
////            ApiState.Loading -> {}
////            is ApiState.Success -> Log.i("draftOrderTest", "ProductInfoScreen: customer ${(customerId as ApiState.Success).data.customers[0].id}")
////        }
//
//        when(shoppingCartDraftOrderState){
//            is ApiState.Error -> Log.i("draftOrderTest", "ProductInfoScreen: draftOrder ${(shoppingCartDraftOrderState as ApiState.Error).message}")
//            ApiState.Loading -> {}
//            is ApiState.Success -> shoppingCartDraftOrder = (shoppingCartDraftOrderState as ApiState.Success).data
//        }
//
//        when(wishListDraftOrderState){
//            is ApiState.Error -> Log.i("draftOrderTest", "ProductInfoScreen: draftOrder ${(wishListDraftOrderState as ApiState.Error).message}")
//            ApiState.Loading -> {}
//            is ApiState.Success -> wishListDraftOrder = (wishListDraftOrderState as ApiState.Success).data
//        }
//
//
//        // Trigger the function to get the product by id
//        LaunchedEffect(key1 = productId) {
//            viewModel.getProductById(productId)
////            viewModel.getCustomerId(FirebaseAuth.getInstance().currentUser?.email.toString())
//        }
//
//        // UI based on the state
//        when (productState) {
//            is ApiState.Loading -> {
//                // Show a loading indicator
//                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
//                    CircularProgressIndicator()
//                }
//            }
//
//            is ApiState.Success -> {
//                //Log.i("productTest", "ProductInfoScreen: ${(productState as ApiState.Success<Product>).data.toString()}")
//                // Show the product details
//                val product = (productState as ApiState.Success<Product>).data
//                //ProductInfoSection(product)
//                val context = LocalContext.current
//                CurrencyConverter.initialize(context)
//                LazyColumn {
//                    item{
//                        Column {
//                            LazyRow {
//                                items(product.images) { imageUrl ->
//                                    Image(
//                                        painter = rememberAsyncImagePainter(imageUrl.src),
//                                        contentDescription = null,
//                                        modifier = Modifier
//                                            .size(300.dp)
//                                            .padding(4.dp)
//                                    )
//                                }
//                            }
//
//                            Text(text = product.title, fontWeight = FontWeight.Bold, fontSize = 20.sp)
////        if (product.sizes.isNotEmpty()) {
////            Text(text = "Available Sizes: ${product.sizes.joinToString()}")
////        }
//                            val price = product.variants.get(0).price
//                            CurrencyConverter.changeCurrency(price.toDouble())?.let {
//                                Text(text = it,
//                                    style = MaterialTheme.typography.bodyMedium)
//                            }
//                            Text(text = "Rating:  ⭐")
//                            Text(text = "Description", fontWeight = FontWeight.Bold, fontSize = 18.sp)
//                            //ScrollableColumn {
//                            Text(text = product.body_html)
//                        }
//                        Spacer(modifier = Modifier.height(16.dp))
//
//                        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
//                            AddToCart(
//                                viewModel,
//                                product,
//                                FirebaseAuth.getInstance().currentUser?.email.toString(),
//                                shoppingCartDraftOrder)
//                            AddToFavorites(
//                                viewModel,
//                                product,
//                                FirebaseAuth.getInstance().currentUser?.email.toString(),
//                                wishListDraftOrder)
//                        } } } }
//            is ApiState.Error -> {
//                // Show the error message
//                val errorMessage = (productState as ApiState.Error).message
//                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
//                    Text(text = "Error: $errorMessage", color = MaterialTheme.colorScheme.error)
//                }
//            }
//        }
//
//    }
//}

@Composable
fun ProductInfoScreen(
    viewModel: ProductInfoViewModel,
    navController: NavController,
    productId: Long
) {
    // Collecting state variables
    val productState by viewModel.searchProductsList.collectAsState()
    val draftOrderId by viewModel.draftOrderId.collectAsState()
    val shoppingCartDraftOrderState by viewModel.shoppingCartDraftOrder.collectAsState()
    val wishListDraftOrderState by viewModel.wishListDraftOrder.collectAsState()

    var draftOrderIdSaved by remember { mutableLongStateOf(0L) }
    var shoppingCartDraftOrder by remember { mutableStateOf(DraftOrder(0L, "", listOf(), "")) }
    var wishListDraftOrder by remember { mutableStateOf(DraftOrder(0L, "", listOf(), "")) }

    // Handle draft orders and product states
    when (draftOrderId) {
        is ApiState.Error -> Log.i("draftOrderTest", "Error: ${(draftOrderId as ApiState.Error).message}")
        ApiState.Loading -> {}
        is ApiState.Success -> draftOrderIdSaved = (draftOrderId as ApiState.Success).data.draft_order.id ?: 0L
    }
    when (shoppingCartDraftOrderState) {
        is ApiState.Error -> Log.i("draftOrderTest", "Error: ${(shoppingCartDraftOrderState as ApiState.Error).message}")
        ApiState.Loading -> {}
        is ApiState.Success -> shoppingCartDraftOrder = (shoppingCartDraftOrderState as ApiState.Success).data
    }
    when (wishListDraftOrderState) {
        is ApiState.Error -> Log.i("draftOrderTest", "Error: ${(wishListDraftOrderState as ApiState.Error).message}")
        ApiState.Loading -> {}
        is ApiState.Success -> wishListDraftOrder = (wishListDraftOrderState as ApiState.Success).data
    }

    // Fetch product data by ID
    LaunchedEffect(key1 = productId) {
        viewModel.getProductById(productId)
    }
    Scaffold(
        topBar = { CustomTopBar(navController,"Product Details",AppColors.Teal) },
        containerColor = Color.White,
        content = { paddingValues ->
    // Main UI layout
    Box(modifier = Modifier.fillMaxSize()
         .padding(paddingValues)
    ) {
        // Loading and Error State Handling
        when (productState) {
            is ApiState.Loading -> {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    CircularProgressIndicator()
                }
            }
            is ApiState.Success -> {
                val product = (productState as ApiState.Success<Product>).data
                LazyRow(
                    modifier = Modifier.fillMaxWidth()
                    ,
                    //horizontalArrangement = Arrangement.spacedBy(8.dp) // Space between items
                ) {
                    items(product.images) { product ->
                       // Box (){
                            Image(
                                painter = rememberAsyncImagePainter(product.src),
                                contentDescription = null,
                                contentScale = ContentScale.FillHeight,
                                modifier = Modifier
                                    .width(400.dp)
                                    .height(200.dp)
                                    .align(Alignment.Center)
                            )
                        //}

                    }
                }

                // Heart and Back Arrow Icons
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp)
                        .align(Alignment.TopEnd),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    IconButton(onClick ={} ) {
                        Icon(
                            imageVector = Icons.Default.ArrowBack,
                            contentDescription = "W",
                            tint = Color.White
                        )
                    }
                    AddToFavorites(viewModel, product, FirebaseAuth.getInstance().currentUser?.email.toString(), wishListDraftOrder)
                }

                // Product Details
                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(top = 200.dp)
                ) {
                    item {
                        Surface(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clip(RoundedCornerShape(topStart = 30.dp, topEnd = 30.dp))
                                .border(1.dp, shape = RoundedCornerShape(topStart = 30.dp, topEnd = 30.dp), color = AppColors.Teal)
                            ,
                            shadowElevation = 3.dp,
                            color = Color.White
                        ) {
                            Column(
                                modifier = Modifier.padding(16.dp)
                            ) {
                                Spacer(modifier = Modifier.height(16.dp))
                                // Product Title
                                Text(
                                    text = product.title,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 20.sp
                                )
                                Spacer(modifier = Modifier.height(10.dp))
                                // Product Price
                                val price = product.variants.get(0).price
                                CurrencyConverter.changeCurrency(price.toDouble())?.let {
                                    Text(
                                        text = it,
                                        style = MaterialTheme.typography.bodyMedium
                                    )
                                }
                                Spacer(modifier = Modifier.height(10.dp))


                                val colors = product.options.get(1).values // Example list of color names
                                ColorCirclesRow(colorNames = colors)
                                // Product Description
                                val sizes = product.options.get(0).values
                                SizeCirclesRow(sizes)
                                Text(
                                    text = "Description",
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 18.sp
                                )
                                Text(
                                    text = product.body_html,
                                    style = MaterialTheme.typography.bodyMedium,
                                    color = Color.Gray
                                )

                                Spacer(modifier = Modifier.height(16.dp))

                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween
                                ) {
                                    AddToCart(viewModel, product, FirebaseAuth.getInstance().currentUser?.email.toString(), shoppingCartDraftOrder)

                                }
                            }
                        }
                    }
                }
            }
            is ApiState.Error -> {
                val errorMessage = (productState as ApiState.Error).message
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Text(text = "Error: $errorMessage", color = MaterialTheme.colorScheme.error)
                }
            }
        }
    }
        })
}


@Composable
fun AddToFavorites(viewModel: ProductInfoViewModel,product : Product,email : String,oldDraftOrder : DraftOrder){
    var clicked by remember { mutableStateOf(false) }
    IconButton(onClick = {
        clicked=true
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
        if(clicked){
            Icon(
                imageVector = Icons.Sharp.Favorite,
                contentDescription = "Favorite",
                tint = AppColors.Teal,
                modifier = Modifier.size(35.dp)
            )
        }else{
            Icon(
                imageVector = Icons.Default.FavoriteBorder,
                contentDescription = "Favorite",
                tint = AppColors.Teal,
                modifier = Modifier.size(35.dp)
            )
        }

    }
}

@Composable
fun AddToCart(viewModel: ProductInfoViewModel,product : Product,email : String,oldDraftOrder : DraftOrder) {
    Row(modifier = Modifier.fillMaxWidth()) {
        OutlinedButton(
             onClick = {
            val properties = listOf(Property(name = "imageUrl", value = product.image.src))
            Log.i("propertiesTest", "AddToCart: $properties")
            if (oldDraftOrder.id == 0L) {
                val lineItems = listOf(
                    LineItem(
                        product.title,
                        product.variants[0].price,
                        product.variants[0].id,
                        1,
                        properties = properties,
                        product.id ?: 0
                    )
                )
                val draftOrder =
                    DraftOrder(note = "shoppingCart", line_items = lineItems, email = email)
                val draftOrderRequest = DraftOrderRequest(draftOrder)
                viewModel.createDraftOrder(draftOrderRequest)
            } else {
                if (!oldDraftOrder.line_items.contains(
                        LineItem(
                            product.title,
                            product.variants[0].price,
                            product.variants[0].id,
                            1,
                            properties = properties,
                            product.id ?: 0
                        )
                    )
                ) {
                    val lineItems = oldDraftOrder.line_items + listOf(
                        LineItem(
                            product.title,
                            product.variants[0].price,
                            product.variants[0].id,
                            1,
                            properties = properties,
                            product.id ?: 0
                        )
                    )
                    val draftOrder =
                        DraftOrder(note = "shoppingCart", line_items = lineItems, email = email)
                    val draftOrderRequest = DraftOrderRequest(draftOrder)
                    oldDraftOrder.id?.let { viewModel.updateDraftOrder(it, draftOrderRequest)
                    }

                }
            }
        }, modifier = Modifier.weight(1f),
            colors = ButtonDefaults.outlinedButtonColors(contentColor = AppColors.Teal),
            border = BorderStroke(1.dp, AppColors.Teal)

        ) {
            Icon(Icons.Default.ShoppingCart, contentDescription = null, tint = AppColors.Teal)
            Spacer(modifier = Modifier.width(4.dp))
            Text(text = "Add To Cart", color = AppColors.Teal)
        }
    }
}

@Composable
fun ColorCirclesRow(colorNames: List<String>) {
    Text(text = "Available Colors:")
    LazyRow(
        modifier = Modifier
            .padding(16.dp)
            .height(60.dp) // Adjust the height if needed
    ) {
        items(colorNames.size) { index ->
            val color = getColorFromName(colorNames[index])
            Log.i("color", "ColorCirclesRow: ${colorNames[index]}")
            Box(
                modifier = Modifier
                    .size(40.dp)  // Set the size for each circle
                    .padding(4.dp)
                    .background(color = color, shape = CircleShape)
                    .border(0.5.dp, shape = CircleShape, color = Color.Black)// Make the background a circle
                    .clickable {  }
            )
        }
    }
}

@Composable
fun getColorFromName(colorName: String): Color {
    return when (colorName.toLowerCase()) {
        "black" -> Color.Black
        "blue" -> Color.Blue
        "gray" -> Color.Gray
        "cyan" -> Color.Cyan
        "white" -> Color.White
        "red"->Color.Red
        "yellow"->Color.Yellow
        "beige" -> Color(0xFFF5F5DC)
        "light_brown" -> Color(0xFFD2B48C)
        "burgandy" -> Color(0xFF800020)
        "green" -> Color.Green
        else -> Color.LightGray // Default color if the name is not recognized
    }
}

@Composable
fun SizeCirclesRow(Itemsizes: List<String>) {
   Text(text = "Available Sizes:")
    LazyRow(
        modifier = Modifier
            .padding(8.dp)
            .height(60.dp) // Adjust the height if needed
    ) {
        items(Itemsizes.size) { index ->
           // Log.i("color", "ColorCirclesRow: ${colorNames[index]}")
            if(Itemsizes[index] != "size"){
            Box(
                modifier = Modifier
                    .size(50.dp)  // Set the size for each circle
                    .padding(4.dp)
                    .background(color = Color.White, shape = RectangleShape)
                    .border(0.5.dp, shape = CircleShape, color = Color.Black)// Make the background a circle
                    .clickable {  }
            ){

                    Text(text = Itemsizes[index], textAlign = TextAlign.Center, fontSize = 14.sp,
                        modifier = Modifier.align(Alignment.Center) )
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
    LazyColumn {
        item{
            Column {
                // Image Carousel (Placeholder for actual image carousel)
                LazyRow {
                    items(product.images) { imageUrl ->
                        Image(
                            painter = rememberAsyncImagePainter(imageUrl.src),
                            contentDescription = null,
                            modifier = Modifier
                                .size(300.dp)
                                .padding(4.dp)
                        )
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
                Text(text = "Description", fontWeight = FontWeight.Bold, fontSize = 18.sp)
                //ScrollableColumn {
                Text(text = product.body_html)
            }
        }
    }

}
