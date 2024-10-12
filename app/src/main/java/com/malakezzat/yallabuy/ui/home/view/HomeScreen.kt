package com.malakezzat.yallabuy.ui.home.view

import android.util.Log
import android.widget.Toast
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.animateScrollBy
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.itemsIndexed
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.BottomNavigation
import androidx.compose.material.BottomNavigationItem
import androidx.compose.material.Chip
import androidx.compose.material.ChipDefaults
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.Snackbar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.sharp.Favorite
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
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
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.currentBackStackEntryAsState
import coil.compose.rememberAsyncImagePainter
import coil.compose.rememberImagePainter
import com.google.firebase.auth.FirebaseAuth
import com.malakezzat.yallabuy.R
import com.malakezzat.yallabuy.data.remote.ApiState
import com.malakezzat.yallabuy.data.sharedpref.CurrencyPreferences
import com.malakezzat.yallabuy.data.util.CurrencyConverter
import com.malakezzat.yallabuy.model.CustomCollection
import com.malakezzat.yallabuy.model.DraftOrder
import com.malakezzat.yallabuy.model.DraftOrderRequest
import com.malakezzat.yallabuy.model.LineItem
import com.malakezzat.yallabuy.model.Product
import com.malakezzat.yallabuy.model.Property
import com.malakezzat.yallabuy.model.SmartCollection
import com.malakezzat.yallabuy.ui.Screen
import com.malakezzat.yallabuy.ui.home.viewmodel.HomeScreenViewModel
import com.malakezzat.yallabuy.ui.product_info.ProductInfoViewModel
import com.malakezzat.yallabuy.ui.theme.AppColors
import kotlinx.coroutines.delay
import kotlin.random.Random

val TAG = "HomeScreen"

@Composable
fun HomeScreen(
    viewModel: HomeScreenViewModel,
    navController: NavController,
) {
    val productState by viewModel.productList.collectAsStateWithLifecycle()
    val categoriesState by viewModel.categoriesList.collectAsStateWithLifecycle()
    val brandsState by viewModel.brandsList.collectAsStateWithLifecycle()

    val draftOrderId by viewModel.draftOrderId.collectAsState()
    val wishListDraftOrderState by viewModel.wishListDraftOrder.collectAsState()

    var draftOrderIdSaved by remember { mutableLongStateOf(0L) }
    var wishListDraftOrder by remember { mutableStateOf(DraftOrder(0L, "", listOf(), "")) }


    //Currency
    val context = LocalContext.current
    CurrencyConverter.initialize(context)
    LaunchedEffect(Unit) {
        Log.d(TAG, categoriesState.toString())
        viewModel.getAllProducts()
        viewModel.getAllCategories()
        viewModel.getDraftOrders()
    }
    when (draftOrderId) {
        is ApiState.Error -> Log.i("draftOrderTest", "Error: ${(draftOrderId as ApiState.Error).message}")
        ApiState.Loading -> {}
        is ApiState.Success -> {
            draftOrderIdSaved = (draftOrderId as ApiState.Success).data.draft_order.id ?: 0L
        }
        else -> {}
    }
    when (wishListDraftOrderState) {
        is ApiState.Error -> Log.i("draftOrderTest", "Error: ${(wishListDraftOrderState as ApiState.Error).message}")
        ApiState.Loading -> {}
        is ApiState.Success -> wishListDraftOrder = (wishListDraftOrderState as ApiState.Success).data
    }
    Scaffold(
        topBar = { CustomTopBarHome(navController) },
        containerColor = Color.White,
        //  bottomBar = { BottomNavigationBar(navController) }
    ) {
        Column(
            modifier = Modifier
                .padding(it)
                .verticalScroll(rememberScrollState())
                .background(color = Color.White)
        ) {
            AdList(viewModel)

            when (brandsState) {
                is ApiState.Loading -> {
                    CircularProgressIndicator(modifier = Modifier.align(Alignment.CenterHorizontally),
                        color = AppColors.Teal

                    )
                }

                is ApiState.Success -> {
                    val brands = (brandsState as ApiState.Success<List<SmartCollection>>).data
                    BrandsList(brands, navController)
                }

                is ApiState.Error -> {
                    /*Text(
                        text = "Error: ${(brandsState as ApiState.Error).message}",
                        color = Color.Red
                    )*/
                    Log.i(TAG, "HomeScreen: Error: ${(brandsState as ApiState.Error).message}")
                    CircularProgressIndicator(modifier = Modifier.align(Alignment.CenterHorizontally),
                        color = AppColors.Teal

                    )
                }
            }
            when (categoriesState) {
                is ApiState.Loading -> {
                    CircularProgressIndicator(modifier = Modifier.align(Alignment.CenterHorizontally),
                        color = AppColors.Teal)
                }

                is ApiState.Success -> {
                    val categories =
                        (categoriesState as ApiState.Success<List<CustomCollection>>).data
                    CategoriesSection(categories, navController)
                    Log.d(TAG, "$categoriesState")
                }

                is ApiState.Error -> {
                    Text(
                        text = "Error: ${(categoriesState as ApiState.Error).message}",
                        color = Color.Red
                    )
                }
            }
            when (productState) {
                is ApiState.Loading -> {
                    CircularProgressIndicator(modifier = Modifier.align(Alignment.CenterHorizontally),
                        color = AppColors.Teal)
                }

                is ApiState.Success -> {
                    val products = (productState as ApiState.Success<List<Product>>).data
                    LatestProductsSection(products, navController,viewModel,wishListDraftOrder)
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
fun BrandsList(brands: List<SmartCollection>, navController: NavController) {
    Column(modifier = Modifier.padding(16.dp)) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text("Brands", color = AppColors.Teal, style = MaterialTheme.typography.titleLarge)
        }
        LazyVerticalGrid(
            columns = GridCells.Fixed(4),
            modifier = Modifier
                .padding(top = 8.dp)
                .height(290.dp)
                .fillMaxWidth()
            ,
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            itemsIndexed(brands) { index, brand ->
                val visibleState = remember { mutableStateOf(false) }
                LaunchedEffect(Unit) {
                    delay(index * 200L)
                    visibleState.value = true
                }
                AnimatedVisibility(
                    visible = visibleState.value,
                    enter = slideInHorizontally(
                        initialOffsetX = { -it },
                        animationSpec = tween(durationMillis = 2000)
                    ) + fadeIn(animationSpec = tween(durationMillis = 1500)),
                    modifier = Modifier.padding(4.dp)
                ) {
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable {
                                Log.i(TAG, "BrandsList: clicked${brand.id}/${brand.title}")
                                navController.navigate("${Screen.ProductsByBrandScreen.route}/${brand.id}/${brand.title}")
                            },
                        shape = RoundedCornerShape(50.dp),
                        elevation = CardDefaults.cardElevation(defaultElevation = 8.dp),
                        colors = CardDefaults.cardColors(containerColor = Color.White)
                    ) {
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            modifier = Modifier
                                .padding(8.dp) // Padding inside the card
                        ) {
                            Image(
                                painter = rememberImagePainter(brand.image.src),
                                contentDescription = brand.title,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .aspectRatio(1f) // Make the image square
                            )
                        }
                    }
                }
            }
        }
    }
}




//@Preview(showBackground = true, showSystemUi = true)
@Composable
fun CustomTopBarHome(navController: NavController) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 12.dp, start = 10.dp, end = 10.dp, bottom = 10.dp)
            .background(Color.White),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Image(
                painter = painterResource(id = R.drawable.logo), // logo
                contentDescription = "Logo",
                modifier = Modifier.size(30.dp)
            )
            Text(
                text = "YallaBuy",
                style = MaterialTheme.typography.titleLarge, color = AppColors.Teal,
                modifier = Modifier.padding(start = 4.dp)
            )
        }
        Row(verticalAlignment = Alignment.CenterVertically) {
            IconButton(onClick = { navController.navigate(Screen.SearchScreen.route) }) {
                Image(
                    painter = painterResource(id = R.drawable.search_normal),
                    contentDescription = "Search Icon",
                    modifier = Modifier.size(24.dp)
                )
            }
        }
    }
}


@Composable
fun AdList(viewModel: HomeScreenViewModel) {
    val context = LocalContext.current
    val scrollState = rememberLazyListState()
    var scrollDirection by remember { mutableStateOf(1) }
    val priceRuleIds by remember { mutableStateOf(mutableSetOf<Long?>()) }
    val discountCodeIds by remember { mutableStateOf(mutableSetOf<String?>()) }
    var fetchDone by remember { mutableStateOf(true) }

    val priceRuleList by viewModel.priceRules.collectAsState()
    val discountCodeList by viewModel.discountCodes.collectAsState()

    if (fetchDone) {
        repeat(3) {
            for (item in priceRuleList) {
                item.id?.let { viewModel.fetchDiscountCodes(it) }
                fetchDone = false
            }
        }
    }

    if (discountCodeList.isNotEmpty()) {
        for (item in discountCodeList) {
            discountCodeIds.add(item.code)
        }
    }
    LazyRow(state = scrollState) {
        // Static ads first
        item {
            AdCard(painterResource(id = R.drawable.ad1))
        }
        item {
            AdCard(painterResource(id = R.drawable.ad2))
        }
        item {
            if (discountCodeIds.isNotEmpty()) {
                val randomCoupon = discountCodeIds.random()
                CouponsCard(randomCoupon)
            }
        }
//        items(filteredDiscountCodes.size) { index ->
//            Log.i("couponsTest", "LazyRow: discount code at index $index : ${filteredDiscountCodes[index]}")
//            CouponsCard(filteredDiscountCodes[index])
//        }
    }

//    // Fetch discount codes for each price rule
//    for (item in priceRuleList) {
//        Log.i("couponsTest", "LazyRow: item :${item.id}")
//        item.id?.let { viewModel.fetchDiscountCodes(it) }
//    }

    LaunchedEffect(Unit) {
        viewModel.fetchPriceRules()
        delay(2000)
        val needToScrollItems = 20 - scrollState.layoutInfo.visibleItemsInfo.size + 1
        val scrollWidth = needToScrollItems * context.resources.displayMetrics.widthPixels
        while (true) {
            scrollState.animateScrollBy(
                value = (scrollDirection * scrollWidth).toFloat(),
                animationSpec = tween(
                    durationMillis = 3000 * needToScrollItems,
                    easing = LinearEasing
                )
            )
            scrollDirection *= -1
        }
    }
}


@Composable
fun AdCard(painter: Painter) {
    Card(
        modifier = Modifier
            .padding(24.dp)
            .fillMaxWidth()
            .height(200.dp),
        shape = RoundedCornerShape(12.dp),
        elevation = CardDefaults.cardElevation(12.dp),
    ) {
        Image(
            painter = painter,
            contentDescription = "ad",
        )

    }

}
@Composable
fun CouponsCard(code: String?) {
    val discount = code?.takeLast(2)
    val painter = when (discount) {
        "10" -> painterResource(R.drawable.coupon10)
        "30" -> painterResource(R.drawable.coupon30)
        "50" -> painterResource(R.drawable.coupon50)
        else -> painterResource(R.drawable.ad1)
    }

    val clipboardManager = LocalClipboardManager.current
    val context = LocalContext.current

    Card(
        modifier = Modifier
            .padding(24.dp)
            .fillMaxWidth()
            .height(200.dp),
        shape = RoundedCornerShape(12.dp),
        elevation = CardDefaults.cardElevation(12.dp),
        onClick = {
            code?.let {
                clipboardManager.setText(AnnotatedString(it))
                Toast.makeText(context, "Code: $code copied to clipboard", Toast.LENGTH_SHORT)
                    .show()
            }
        }
    ) {
        Image(
            painter = painter,
            contentDescription = "Coupon",
        )
    }
}

//@Preview(showBackground = true, showSystemUi = true)
@Composable
fun CategoriesSection(categories: List<CustomCollection>, navController: NavController) {
    Log.d(TAG, "3. ${categories}")
    Column(modifier = Modifier.padding(16.dp)) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text("Categories", color = AppColors.Teal, style = MaterialTheme.typography.titleLarge)
        }
        LazyRow(
            modifier = Modifier.padding(top = 8.dp),
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            itemsIndexed(categories) { index, category ->
                val visibleState = remember { mutableStateOf(false) }
                LaunchedEffect(Unit) {
                    delay(index * 200L)
                    visibleState.value = true
                }

                AnimatedVisibility(
                    visible = visibleState.value,
                    enter = slideInHorizontally(
                        initialOffsetX = { -it },
                        animationSpec = tween(durationMillis = 2000)
                    ) + fadeIn(animationSpec = tween(durationMillis = 2500)),
                    exit = slideOutHorizontally(
                        targetOffsetX = { -it },
                        animationSpec = tween(durationMillis = 300)
                    ),
                    modifier = Modifier.padding(4.dp)
                ) {
                    CategoryItem(category, navController)
                }
            }
        }
    }
}



//@Preview(showBackground = true, showSystemUi = true)
@Composable
fun CategoryItem(category: CustomCollection, navController: NavController) {
    Log.d(TAG, "4. ${category}")
    Card(
        modifier = Modifier
            .size(100.dp)
            .clickable {
                navController.navigate("${Screen.ProductsByCategoryScreen.route}/${category.id.toString()}/${category.body_html}")
            },
        shape = RoundedCornerShape(28.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 8.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        border = BorderStroke(1.dp, Color(0xFFE0E0E0))
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Image(
                painter = rememberAsyncImagePainter(category.image?.src),
                contentDescription = "category item",
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
                    .clip(RoundedCornerShape(28.dp)),
                contentScale = ContentScale.Crop
            )
            Text(
                text = category.title,
                style = MaterialTheme.typography.bodyMedium,
                color = AppColors.Teal,
                modifier = Modifier.padding(top = 10.dp, bottom = 10.dp)
            )
        }
    }
}


//@Preview(showBackground = true, showSystemUi = true)
@Composable
fun LatestProductsSection(products: List<Product>, navController: NavController,viewModel: HomeScreenViewModel,oldDraftOrder : DraftOrder) {
    //
    Column(modifier = Modifier.padding(16.dp)) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                "All Products",
                style = MaterialTheme.typography.titleLarge,
                color = AppColors.Teal,
                modifier = Modifier.padding(8.dp)
            )
            /*Text(
                "SEE ALL", style =  MaterialTheme.typography.titleSmall,
                color = AppColors.RoseLight,
            )*/
        }

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(3650.dp)
        ) {
            LazyVerticalGrid(
                columns = GridCells.Fixed(2),
                modifier = Modifier.fillMaxSize(),
                horizontalArrangement = Arrangement.spacedBy(7.dp),
                verticalArrangement = Arrangement.spacedBy(5.dp),
                contentPadding = PaddingValues(0.dp)

            ) {
                itemsIndexed(products) { _, product ->
                    ProductCard(product = product, navController,viewModel,oldDraftOrder)
                }
            }
        }
    }
}


@Composable
fun ProductCard(product: Product, navController: NavController,viewModel: HomeScreenViewModel,oldDraftOrder : DraftOrder) {
    //Currency
    val context = LocalContext.current
    CurrencyConverter.initialize(context)
    // Create the scroll state directly in the Composable
    val scrollState = rememberScrollState()

    Card(
        modifier = Modifier
            .size(235.dp)
            .padding(2.dp)
            .clickable {
                navController.navigate("${Screen.ProductInfScreen.route}/${product.id}")
            },
        shape = RoundedCornerShape(30.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 12.dp),

        ) {
        Box(
            modifier = Modifier
                .padding(8.dp)
                .verticalScroll(scrollState)
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                product.images.firstOrNull()?.let { image ->
                    val offsetY = animateFloatAsState(targetValue = -scrollState.value / 4f)

                    Image(
                        painter = rememberAsyncImagePainter(image.src),
                        contentDescription = "Product Image",
                        modifier = Modifier
                            .size(120.dp)
                            .clip(RoundedCornerShape(16.dp))
                            .graphicsLayer(translationY = offsetY.value),
                        contentScale = ContentScale.Fit
                    )
                }

                Column(
                    modifier = Modifier.padding(start = 10.dp)
                ) {
                    Text(product.title, style = MaterialTheme.typography.titleSmall)
                    Text(product.vendor, color = AppColors.Teal)
                    val price = product.variants.first().price
                    CurrencyConverter.changeCurrency(price.toDouble())?.let {
                        Text(
                            text = it,
                            style = MaterialTheme.typography.bodyMedium
                        )
                    }
                }
            }

//            Icon(
//                imageVector = Icons.Default.FavoriteBorder,
//                contentDescription = "Favorite",
//                tint = AppColors.Teal,
//                modifier = Modifier
//                    .size(50.dp)
//                    .align(Alignment.TopEnd)
//                    .padding(8.dp)
//            )
            AddToFavorites(
                viewModel,
                product,
                FirebaseAuth.getInstance().currentUser?.email.toString(),
                oldDraftOrder,
                navController
            )

        }

    }
}
@Composable
fun AddToFavorites(viewModel: HomeScreenViewModel, product : Product, email : String, oldDraftOrder : DraftOrder, navController: NavController){
    var geustClicked by remember { mutableStateOf(false) }
    var clicked by remember { mutableStateOf(false) }
    var contains by remember { mutableStateOf(false) }
    if(FirebaseAuth.getInstance().currentUser?.isAnonymous==true){
        IconButton(onClick = {geustClicked=true}) {
            Icon(
                imageVector = Icons.Default.FavoriteBorder,
                contentDescription = "Favorite",
                tint = AppColors.Teal,
                modifier = Modifier.size(35.dp)
            )
        }
    }else{
        LaunchedEffect(Unit) {
            for(item in oldDraftOrder.line_items){
                if(product.id == item.product_id){
                    clicked=true
                }
            }
        }
        val properties = listOf(
            Property(name = "imageUrl",value = product.image.src),
            Property(name = "size",value = product.variants[0].option1),
            Property(name = "color",value = product.variants[0].option2)
        )
        contains = oldDraftOrder.line_items.contains(LineItem(
            product.title,
            product.variants[0].price,
            product.variants[0].id,
            1,
            properties = properties,
            product.id?:0
        ))
        IconButton(onClick = {



            Log.i("propertiesTest", "AddToFav: ${oldDraftOrder.id}")
            if(oldDraftOrder.id == 0L) {
                val lineItems = listOf(LineItem(product.title,product.variants[0].price,product.variants[0].id,1, properties = properties,product.id?:0))
                val draftOrder = DraftOrder(note = "wishList", line_items = lineItems, email = email)
                val draftOrderRequest = DraftOrderRequest(draftOrder)
                viewModel.createDraftOrder(draftOrderRequest)
            } else {
                    var lineItems = listOf<LineItem>()

                    if(contains){
                        clicked=false
                        lineItems = oldDraftOrder.line_items.filter { it.variant_id != product.variants[0].id }
                        Log.i(TAG, "AddToFavorites: contains ${lineItems}")
                    } else {
                        clicked=true
                        lineItems = oldDraftOrder.line_items + listOf(
                            LineItem(
                                product.title,
                                product.variants[0].price,
                                product.variants[0].id,
                                1,
                                properties = properties,
                                product.id ?: 0
                            )
                        )
                        Log.i(TAG, "AddToFavorites: not contains ${lineItems}")
                    }
                    val draftOrder = DraftOrder(note = "wishList", line_items = lineItems, email = email)
                    val draftOrderRequest = DraftOrderRequest(draftOrder)
                    oldDraftOrder.id?.let { viewModel.updateDraftOrder(it,draftOrderRequest) }
                }

        }) {
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
    if(geustClicked){
        AlertDialog(
            onDismissRequest = { geustClicked = false }, // Close dialog on dismiss
            title = { Text(text = "Guest") },
            text = { Text("You’re shopping as a guest. Log in for a faster checkout, exclusive deals, and to save your favorite products") },
            confirmButton = {
                TextButton(
                    onClick = {
                        navController.navigate(Screen.LogInScreen.route) {
                            popUpTo(navController.graph.startDestinationId) {
                                inclusive = true // Remove all previous screens from the back stack
                            }
                        }
                        geustClicked = false // Close the dialog after confirming
                    }
                ) {
                    Text("Login", color = AppColors.Teal)
                }
            },
            dismissButton = {
                TextButton(
                    onClick = { geustClicked = false }
                ) {
                    Text("Cancel", color = AppColors.Rose)
                }
            }
        )
    }
}


//@Preview(showBackground = true, showSystemUi = true)
@Composable
fun ProductCard() {
    Box(
        modifier = Modifier
            .width(150.dp)
            .clip(RoundedCornerShape(12.dp))
            .background(Color(0xFFE0E0E0))
            .padding(14.dp)
    ) {
        // Background image of the product
        Image(
            painter = painterResource(id = R.drawable.rectangle9),
            contentDescription = "productName",
            modifier = Modifier
                .fillMaxWidth()
                .height(100.dp),
            contentScale = ContentScale.Crop
        )

        // Wishlist icon positioned at the top right corner
        Image(
            painter = painterResource(id = R.drawable.wishlist),
            contentDescription = "wishlist",
            modifier = Modifier
                .size(40.dp) // Adjust size as needed
                .align(Alignment.TopEnd) // Align the image to the top end corner
                .padding(5.dp) // Add padding around the image
        )

        // Column for product details
        Column(
            modifier = Modifier.padding(top = 110.dp) // Ensure text does not overlap with the image
        ) {
            Text("productName", style = TextStyle(fontSize = 14.sp, fontWeight = FontWeight.Bold))
            Text("currentPrice", color = AppColors.MintGreen)
            Text("originalPrice", textDecoration = TextDecoration.LineThrough)
        }
    }
}

@Composable
fun BottomNavigationBar(navController: NavController) {
    val navBackStackEntry = navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry.value?.destination?.route
    var geustClicked by remember { mutableStateOf(false) }
    BottomNavigation(
        backgroundColor = Color.White,
        contentColor = Color.Black,
        modifier = Modifier.navigationBarsPadding()
    ) {
        BottomNavigationItem(
            icon = {
                Image(
                    painter = painterResource(id = if (currentRoute == Screen.HomeScreen.route) R.drawable.home_color else R.drawable.home_2),
                    contentDescription = "Home",
                    modifier = Modifier.size(24.dp)
                )
            },
            label = { Text("Home", fontSize = 10.sp, style = MaterialTheme.typography.titleSmall) },
            selected = currentRoute == Screen.HomeScreen.route,
            onClick = {
                if (currentRoute != Screen.HomeScreen.route) {
                    navController.popBackStack(Screen.HomeScreen.route, inclusive = false)
                    navController.navigate(Screen.HomeScreen.route) {
                        launchSingleTop = true
                    }
                }
            }
        )
        BottomNavigationItem(
            icon = {
                Image(
                    painter = painterResource(id = if (currentRoute == Screen.CategoriesScreen.route) R.drawable.category_colr else R.drawable.category_2),
                    contentDescription = "Categories",
                    modifier = Modifier.size(24.dp)
                )
            },
            label = {
                Text(
                    "Categories",
                    fontSize = 10.sp,
                    style = MaterialTheme.typography.titleSmall
                )
            },
            selected = currentRoute == Screen.CategoriesScreen.route,
            onClick = {
                if (currentRoute != Screen.CategoriesScreen.route) {
                    navController.popBackStack(Screen.CategoriesScreen.route, inclusive = false)
                    navController.navigate(Screen.CategoriesScreen.route) {
                        launchSingleTop = true
                    }
                }
            }
        )
        BottomNavigationItem(
            icon = {
                Image(
                    painter = painterResource(id = if (currentRoute == Screen.ShoppingScreen.route) R.drawable.shopping_cart_color else R.drawable.shopping_cart),
                    contentDescription = "My Cart",
                    modifier = Modifier.size(24.dp)
                )
            },
            label = {
                Text(
                    "My Cart",
                    fontSize = 10.sp,
                    style = MaterialTheme.typography.titleSmall
                )
            },
            selected = currentRoute == Screen.ShoppingScreen.route,
            onClick = {
                if(FirebaseAuth.getInstance().currentUser?.isAnonymous==true){
                    geustClicked=true
                }else{
                    if (currentRoute != Screen.ShoppingScreen.route) {
                        navController.popBackStack(Screen.ShoppingScreen.route, inclusive = false)
                        navController.navigate(Screen.ShoppingScreen.route) {
                            launchSingleTop = true
                        }
                    }
                }

            }
        )
        BottomNavigationItem(
            icon = {
                Image(
                    painter = painterResource(id = if (currentRoute == Screen.WishlistScreen.route) R.drawable.heart_color else R.drawable.heart),
                    contentDescription = "Wishlist",
                    modifier = Modifier.size(24.dp)
                )
            },
            label = {
                Text(
                    "Wishlist",
                    fontSize = 10.sp,
                    style = MaterialTheme.typography.titleSmall
                )
            },
            selected = currentRoute == Screen.WishlistScreen.route,
            onClick = {
                if(FirebaseAuth.getInstance().currentUser?.isAnonymous==true){
                    geustClicked=true
                }else {
                    if (currentRoute != Screen.WishlistScreen.route) {
                        navController.popBackStack(Screen.WishlistScreen.route, inclusive = false)
                        navController.navigate(Screen.WishlistScreen.route) {
                            launchSingleTop = true
                        }
                    }
                }
            }
        )
        BottomNavigationItem(
            icon = {
                Image(
                    painter = painterResource(id = if (currentRoute == Screen.ProfileScreen.route) R.drawable.profile_color else R.drawable.profile),
                    contentDescription = "Profile",
                    modifier = Modifier.size(24.dp)
                )
            },
            label = {
                Text(
                    "Profile",
                    fontSize = 10.sp,
                    style = MaterialTheme.typography.titleSmall
                )
            },
            selected = currentRoute == Screen.ProfileScreen.route,
            onClick = {
                if(FirebaseAuth.getInstance().currentUser?.isAnonymous==true){
                    geustClicked=true
                }else {
                    if (currentRoute != Screen.SettingsScreen.route) {
                        navController.popBackStack(Screen.ProfileScreen.route, inclusive = false)
                        navController.navigate(Screen.ProfileScreen.route) {
                            launchSingleTop = true
                        }
                    }
                }
            }
        )
    }
    if(geustClicked){
        AlertDialog(
            onDismissRequest = { geustClicked = false }, // Close dialog on dismiss
            title = { Text(text = "Guest") },
            text = { Text("You’re shopping as a guest. Log in for a faster checkout, exclusive deals, and to save your favorite products") },
            confirmButton = {
                TextButton(
                    onClick = {
                        navController.navigate(Screen.LogInScreen.route) {
                            popUpTo(navController.graph.startDestinationId) {
                                inclusive = true // Remove all previous screens from the back stack
                            }
                        }
                        geustClicked = false // Close the dialog after confirming
                    }
                ) {
                    Text("Login", color = AppColors.Teal)
                }
            },
            dismissButton = {
                TextButton(
                    onClick = { geustClicked = false }
                ) {
                    Text("Cancel", color = AppColors.Rose)
                }
            }
        )
    }
}