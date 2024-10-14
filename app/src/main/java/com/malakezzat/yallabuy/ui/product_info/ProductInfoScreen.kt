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
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
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
import com.malakezzat.yallabuy.ui.Screen
import com.malakezzat.yallabuy.ui.theme.AppColors


@Composable
fun ProductInfoScreen(
    viewModel: ProductInfoViewModel,
    navController: NavController,
    productId: Long
) {
    val context = LocalContext.current
    val productState by viewModel.searchProductsList.collectAsState()
    val draftOrderId by viewModel.draftOrderId.collectAsState()
    val shoppingCartDraftOrderState by viewModel.shoppingCartDraftOrder.collectAsState()
    val wishListDraftOrderState by viewModel.wishListDraftOrder.collectAsState()

    var draftOrderIdSaved by remember { mutableLongStateOf(0L) }
    var shoppingCartDraftOrder by remember { mutableStateOf(DraftOrder(0L, "", listOf(), "")) }
    var wishListDraftOrder by remember { mutableStateOf(DraftOrder(0L, "", listOf(), "")) }

    var color by remember { mutableStateOf("") }
    var size by remember { mutableStateOf("") }
    var quantity by remember { mutableStateOf(0L) }
    var index by remember { mutableStateOf(0L) }

    var wishListAddButton by remember { mutableStateOf("") }
    var shoppingCartAddButton by remember { mutableStateOf("") }

    var isAddedWishList by remember { mutableStateOf(false) }
    var isAddedShoppingCart by remember { mutableStateOf(false) }

    var variant by remember { mutableStateOf(0) }

    when (draftOrderId) {
        is ApiState.Error -> Log.i("draftOrderTest", "Error: ${(draftOrderId as ApiState.Error).message}")
        ApiState.Loading -> {}
        is ApiState.Success -> {
            draftOrderIdSaved = (draftOrderId as ApiState.Success).data.draft_order.id ?: 0L
            viewModel.getDraftOrders()
        }
    }

    when (shoppingCartDraftOrderState) {
        is ApiState.Error -> Log.i("draftOrderTest", "Error: ${(shoppingCartDraftOrderState as ApiState.Error).message}")
        ApiState.Loading -> {}
        is ApiState.Success -> {
            shoppingCartDraftOrder = (shoppingCartDraftOrderState as ApiState.Success).data
            isAddedShoppingCart = true
        }
    }
    when (wishListDraftOrderState) {
        is ApiState.Error -> Log.i("draftOrderTest", "Error: ${(wishListDraftOrderState as ApiState.Error).message}")
        ApiState.Loading -> {}
        is ApiState.Success -> {
            wishListDraftOrder = (wishListDraftOrderState as ApiState.Success).data
            isAddedWishList = true
        }
    }

    LaunchedEffect(shoppingCartAddButton,isAddedShoppingCart) {
        if(shoppingCartAddButton.isNotBlank() && isAddedShoppingCart){
            Toast.makeText(context, shoppingCartAddButton, Toast.LENGTH_SHORT).show()
            shoppingCartAddButton = ""
        }
        isAddedShoppingCart = false
    }

    LaunchedEffect(wishListAddButton,isAddedWishList) {
        if(wishListAddButton.isNotBlank() && isAddedWishList){
            Toast.makeText(context, wishListAddButton, Toast.LENGTH_SHORT).show()
            wishListAddButton = ""
        }
        isAddedWishList = false
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
    Box(modifier = Modifier
        .fillMaxSize()
        .padding(paddingValues)
    ) {

        when (productState) {
            is ApiState.Loading -> {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    androidx.compose.material.CircularProgressIndicator(color = AppColors.Teal)
                }
            }
            is ApiState.Success -> {
                val product = (productState as ApiState.Success<Product>).data
                size = product.variants[0].option1
                color = product.variants[0].option2
                index = product.variants[0].id
                //images
                LazyRow(
                    modifier = Modifier.fillMaxWidth()
                    ,

                ) {
                    items(product.images) { product ->

                            Image(
                                painter = rememberAsyncImagePainter(product.src),
                                contentDescription = null,
                                contentScale = ContentScale.FillHeight,
                                modifier = Modifier
                                    .width(400.dp)
                                    .height(200.dp)
                                    .align(Alignment.Center)
                            )


                    }
                }

                // Heart and Back
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
                    AddToFavorites(viewModel,
                        product,
                        FirebaseAuth.getInstance().currentUser?.email.toString(),
                        wishListDraftOrder,
                        navController){ added ->
                        wishListAddButton = added
                    }
                }

                // Product Details
                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                        .height(1500.dp)
                        .padding(top = 200.dp)
                ) {
                    item {
                        Surface(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(1000.dp)
                                .clip(RoundedCornerShape(topStart = 30.dp, topEnd = 30.dp))
                                .border(
                                    1.dp,
                                    shape = RoundedCornerShape(topStart = 30.dp, topEnd = 30.dp),
                                    color = AppColors.Teal
                                )
                            ,
                            shadowElevation = 10.dp,
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
                                var price = product.variants.get(0).price
                                for(item in product.variants){
                                    if((item.option1 == size)&&(item.option2==color)){
                                        price=item.price
                                        quantity = item.inventory_quantity
                                    }
                                }

                                CurrencyConverter.changeCurrency(price.toDouble())?.let {
                                    Text(
                                        text = it,
                                        style = MaterialTheme.typography.bodyMedium,
                                        fontWeight = FontWeight.Bold,
                                        color = AppColors.Teal
                                    )
                                }
                                Spacer(modifier = Modifier.height(10.dp))

                                Text(
                                    text = "In Stock: $quantity",
                                    style = MaterialTheme.typography.bodyMedium,
                                    fontWeight = FontWeight.Bold,
                                    color = AppColors.Teal
                                )

                                Spacer(modifier = Modifier.height(10.dp))

                                val colors = product.options.get(1).values
                                ColorCirclesRow(colorNames = colors, onColorChange = {c ->  color = c
                                    for(item in product.variants){
                                        if((item.option1 == size)&&(item.option2==color)){
                                            price=item.price
                                            index = item.id
                                            quantity = item.inventory_quantity
                                        }
                                    }
                                })
                                // Product Description
                                val sizes = product.options.get(0).values
                                SizeCirclesRow(sizes, onSizeChange = {s -> size = s
                                    for(item in product.variants){
                                        if((item.option1 == size)&&(item.option2==color)){
                                            price=item.price
                                            index = item.id
                                            quantity = item.inventory_quantity
                                        }
                                    }
                                })
                                Text(
                                    text = "Description",
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 18.sp
                                )
                                Spacer(Modifier.height(8.dp))
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
                                    AddToCart(viewModel, product, FirebaseAuth.getInstance().currentUser?.email.toString(), shoppingCartDraftOrder,
                                        price,size,color,index,navController){ added ->
                                        shoppingCartAddButton = added
                                    }

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
fun AddToFavorites(
    viewModel: ProductInfoViewModel,
    product: Product,
    email: String,
    oldDraftOrder: DraftOrder,
    navController: NavController,
    onAddedToFavorite: (String) -> Unit
) {
    var guestClicked by remember { mutableStateOf(false) }
    var clicked by remember { mutableStateOf(false) }
    var isProcessing by remember { mutableStateOf(false) }

    if (FirebaseAuth.getInstance().currentUser?.isAnonymous == true) {
        IconButton(onClick = { guestClicked = true }) {
            Icon(
                imageVector = Icons.Default.FavoriteBorder,
                contentDescription = "Favorite",
                tint = AppColors.Teal,
                modifier = Modifier.size(35.dp)
            )
        }
    } else {
        clicked = oldDraftOrder.line_items.any { it.product_id == product.id }

        IconButton(onClick = {
            isProcessing = true
            val properties = listOf(
                Property(name = "imageUrl", value = product.image.src),
                Property(name = "size", value = product.variants[0].option1),
                Property(name = "color", value = product.variants[0].option2)
            )

            if (oldDraftOrder.id == 0L) {
                clicked = true
                val lineItems = listOf(
                    LineItem(
                        title = product.title,
                        price = product.variants[0].price,
                        variant_id = product.variants[0].id,
                        quantity = 1,
                        properties = properties,
                        product_id = product.id ?: 0
                    )
                )
                val draftOrder = DraftOrder(note = "wishList", line_items = lineItems, email = email)
                val draftOrderRequest = DraftOrderRequest(draftOrder)
                viewModel.createDraftOrder(draftOrderRequest)
                onAddedToFavorite("Added to WishList")
            } else {
                if (!oldDraftOrder.line_items.any { it.variant_id == product.variants[0].id }) {
                    clicked = true
                    val lineItems = oldDraftOrder.line_items + listOf(
                        LineItem(
                            title = product.title,
                            price = product.variants[0].price,
                            variant_id = product.variants[0].id,
                            quantity = 1,
                            properties = properties,
                            product_id = product.id ?: 0
                        )
                    )
                    val draftOrder = DraftOrder(note = "wishList", line_items = lineItems, email = email)
                    val draftOrderRequest = DraftOrderRequest(draftOrder)
                    oldDraftOrder.id?.let { viewModel.updateDraftOrder(it, draftOrderRequest) }
                    onAddedToFavorite("Added to WishList")
                } else {
                    clicked = false
                    val lineItems = oldDraftOrder.line_items.filterNot { it.variant_id == product.variants[0].id }
                    val draftOrder = DraftOrder(note = "wishList", line_items = lineItems, email = email)
                    val draftOrderRequest = DraftOrderRequest(draftOrder)
                    if(lineItems.isEmpty()){
                        oldDraftOrder.id?.let { viewModel.deleteDraftOrder(it) }
                    } else {
                        oldDraftOrder.id?.let { viewModel.updateDraftOrder(it, draftOrderRequest) }
                    }
                    onAddedToFavorite("Removed from WishList")
                }
            }
            isProcessing = false
        }, enabled = !isProcessing)
        {
            if (clicked) {
                Icon(
                    imageVector = Icons.Sharp.Favorite,
                    contentDescription = "Favorite",
                    tint = AppColors.Teal,
                    modifier = Modifier.size(35.dp)
                )
            } else {
                Icon(
                    imageVector = Icons.Default.FavoriteBorder,
                    contentDescription = "Favorite",
                    tint = AppColors.Teal,
                    modifier = Modifier.size(35.dp)
                )
            }
        }
    }

    if (guestClicked) {
        AlertDialog(
            containerColor = Color.White,
            onDismissRequest = { guestClicked = false },
            title = { Text(text = "Guest") },
            text = { Text("You need to login first.") },
            confirmButton = {
                TextButton(
                    onClick = {
                        navController.navigate(Screen.LogInScreen.route) {
                            popUpTo(navController.graph.startDestinationId) {
                                inclusive = true // Remove all previous screens from the back stack
                            }
                        }
                        guestClicked = false // Close the dialog after confirming
                    }
                    ,colors = ButtonDefaults.buttonColors(containerColor = AppColors.Teal)
                ) {
                    Text("Login", color = Color.White)
                }
            },
            dismissButton = {
                TextButton(onClick = { guestClicked = false }
                    ,colors = ButtonDefaults.buttonColors(containerColor = AppColors.Teal)) {
                    Text("Cancel", color = Color.White)
                }
            }
        )
    }
}

@Composable
fun AddToCart(viewModel: ProductInfoViewModel,
              product : Product,
              email : String,
              oldDraftOrder : DraftOrder,
              price : String,
              size: String,
              color: String,
              id : Long,
              navController: NavController,
              onAddedToCart : (String) -> Unit) {
    var geustClicked by remember { mutableStateOf(false) }
    val context = LocalContext.current
    Row(modifier = Modifier.fillMaxWidth()) {
        if(FirebaseAuth.getInstance().currentUser?.isAnonymous==true){
            OutlinedButton(
                colors = ButtonDefaults.outlinedButtonColors(contentColor = AppColors.Teal),
                border = BorderStroke(1.dp, AppColors.Teal),
                onClick = { geustClicked=true },
                modifier = Modifier.weight(1f)) {
                Icon(Icons.Default.ShoppingCart, contentDescription = null, tint = AppColors.Teal)
                Spacer(modifier = Modifier.width(4.dp))
                Text(text = "Add To Cart", color = AppColors.Teal)
            }
        }else if((product.variants.find { it.id == id }?.inventory_quantity ?: 0L) <= 0L){
            OutlinedButton(
                colors = ButtonDefaults.outlinedButtonColors(contentColor = AppColors.Teal),
                border = BorderStroke(1.dp, AppColors.Teal),
                onClick = {
                    Toast.makeText(context, "Out of Stock", Toast.LENGTH_SHORT).show()
                },
                modifier = Modifier.weight(1f)) {
                Icon(Icons.Default.ShoppingCart, contentDescription = null, tint = AppColors.Gray)
                Spacer(modifier = Modifier.width(4.dp))
                Text(text = "Add To Cart", color = AppColors.Gray)
            }
        } else{
            OutlinedButton(
                onClick = {
                    val properties = listOf(Property(name = "imageUrl",value = product.image.src),
                        Property(name = "size",value = size),
                        Property(name = "color",value = color))
                    Log.i("propertiesTest", "AddToCart: id ${id}")
                    if (oldDraftOrder.id == 0L) {
                        val lineItems = listOf(
                            LineItem(
                                product.title,
                                price,
                                id,
                                1,
                                properties = properties,
                                product.id ?: 0,
                            )
                        )
                        val draftOrder =
                            DraftOrder(note = "shoppingCart", line_items = lineItems, email = email)
                        val draftOrderRequest = DraftOrderRequest(draftOrder)
                        viewModel.createDraftOrder(draftOrderRequest)
                        onAddedToCart("Added to Cart")
                    } else {
                        if (!oldDraftOrder.line_items.contains(
                                LineItem(
                                    product.title,
                                    price,
                                    id,
                                    1,
                                    properties = properties,
                                    product.id ?: 0,
                                )
                            )
                        ) {
                            val lineItems = oldDraftOrder.line_items + listOf(
                                LineItem(
                                    product.title,
                                    price,
                                    id,
                                    1,
                                    properties = properties,
                                    product.id ?: 0,
                                )
                            )
                            val draftOrder =
                                DraftOrder(note = "shoppingCart", line_items = lineItems, email = email)
                            val draftOrderRequest = DraftOrderRequest(draftOrder)
                            oldDraftOrder.id?.let {

                                viewModel.updateDraftOrder(it, draftOrderRequest)

                            }
                            onAddedToCart("Added to Cart")
                        } else {
                            onAddedToCart("Already in Shopping Cart")
                        }
                    }

                }, modifier = Modifier.weight(1f),
                colors = ButtonDefaults.outlinedButtonColors(contentColor = AppColors.Teal),
                border = BorderStroke(1.dp, AppColors.Teal),
            ) {

                Icon(Icons.Default.ShoppingCart, contentDescription = null, tint = AppColors.Teal)
                Spacer(modifier = Modifier.width(4.dp))
                Text(text = "Add To Cart", color = AppColors.Teal)

            }
        }
    }
    if(geustClicked){
        AlertDialog(
            containerColor = Color.White,
            onDismissRequest = { geustClicked = false},
            title = { Text("Guest") },
            text = { Text("You’re shopping as a guest. Log in for a faster checkout, exclusive deals, and to save your favorite products") },
            confirmButton = {
                TextButton(onClick = {
                    geustClicked=false
                    navController.navigate(Screen.LogInScreen.route)
                }
                    ,colors = ButtonDefaults.buttonColors(containerColor = AppColors.Teal)) {
                    Text("Login", color = Color.White)
                }
            },dismissButton = {
                TextButton(
                    onClick = { geustClicked = false }
                    ,colors = ButtonDefaults.buttonColors(containerColor = AppColors.Teal)
                ) {
                    Text("Cancel", color = Color.White)
                }
            }

        )
    }
}

@Composable
fun ColorCirclesRow(colorNames: List<String> ,onColorChange: (String) -> Unit) {
    var colorSelection by remember { mutableStateOf(0) }
    Text(text = "Available Colors:")
    LazyRow(
        modifier = Modifier
            .padding(16.dp)
            .height(60.dp)
    ) {
        items(colorNames.size) { index ->
            val color = getColorFromName(colorNames[index])
            if(index == colorSelection){
                Box(
                    modifier = Modifier
                        .size(40.dp)
                        .padding(4.dp)
                        .background(color = color, shape = CircleShape)
                        .border(0.8.dp, shape = CircleShape, color = Color.Black)
                        .clickable {
                            colorSelection = index
                            onColorChange(colorNames[colorSelection])
                        }
                )
            }
            else{
                Box(
                    modifier = Modifier
                        .size(40.dp)
                        .padding(4.dp)
                        .background(color = color, shape = CircleShape)
                        .border(0.5.dp, shape = CircleShape, color = Color.Gray)
                        .clickable {
                            colorSelection = index
                            onColorChange(colorNames[colorSelection])
                        }
                )
            }

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
        else -> Color.LightGray
    }
}

@Composable
fun SizeCirclesRow(Itemsizes: List<String>,onSizeChange: (String) -> Unit) {
    var sizeSelection by remember { mutableStateOf(0) }
   Text(text = "Available Sizes:")
    LazyRow(
        modifier = Modifier
            .padding(8.dp)
            .height(60.dp)
    ) {
        items(Itemsizes.size) { index ->
            if(Itemsizes[index] != "size"){
                if(index == sizeSelection){
                    Box(
                        modifier = Modifier
                            .size(50.dp)
                            .padding(4.dp)
                            .background(color = Color.White, shape = RectangleShape)
                            .border(0.5.dp, shape = CircleShape, color = AppColors.Teal)
                            .clickable {
                                sizeSelection = index
                                onSizeChange(Itemsizes[sizeSelection])
                            }
                    ){

                        Text(text = Itemsizes[index], textAlign = TextAlign.Center, fontSize = 14.sp,
                            modifier = Modifier.align(Alignment.Center) )
                    }
                }else{
                    Box(
                        modifier = Modifier
                            .size(50.dp)
                            .padding(4.dp)
                            .background(color = Color.White, shape = RectangleShape)
                            .border(0.8.dp, shape = CircleShape, color = Color.Gray)
                            .clickable {
                                sizeSelection = index
                                onSizeChange(Itemsizes[sizeSelection])
                            }
                    ){

                        Text(text = Itemsizes[index], textAlign = TextAlign.Center, fontSize = 14.sp,
                            modifier = Modifier.align(Alignment.Center) )
                    }
                }

            }
        }
    }
}


