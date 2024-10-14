package com.malakezzat.yallabuy.ui.productbycategory.view

import android.util.Log
import android.widget.Toast
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
import com.google.firebase.auth.FirebaseAuth
import com.malakezzat.yallabuy.R
import com.malakezzat.yallabuy.data.remote.ApiState
import com.malakezzat.yallabuy.data.util.CurrencyConverter
import com.malakezzat.yallabuy.model.DraftOrder
import com.malakezzat.yallabuy.model.DraftOrderRequest
import com.malakezzat.yallabuy.model.LineItem
import com.malakezzat.yallabuy.model.Product
import com.malakezzat.yallabuy.model.Property
import com.malakezzat.yallabuy.ui.CustomTopBar
import com.malakezzat.yallabuy.ui.Screen
import com.malakezzat.yallabuy.ui.home.view.LatestProductsSection
import com.malakezzat.yallabuy.ui.home.view.TAG
import com.malakezzat.yallabuy.ui.home.viewmodel.HomeScreenViewModel
import com.malakezzat.yallabuy.ui.product_info.ProductInfoViewModel
import com.malakezzat.yallabuy.ui.productbycategory.viewmodel.ProductsByCollectionIdViewModel
import com.malakezzat.yallabuy.ui.theme.AppColors
import kotlinx.coroutines.delay
import kotlin.math.log

val TAG = "ProductsByCollectionIdS"
@Composable
fun ProductsByCategoryScreen(
    viewModel: ProductsByCollectionIdViewModel,
    navController: NavController,
    id: String?,
    body: String?
) {
    val context = LocalContext.current
    val productState by viewModel.productList.collectAsStateWithLifecycle()
    val categoryId: Long? = id?.toLongOrNull()

    val draftOrderId by viewModel.draftOrderId.collectAsState()
    val wishListDraftOrderState by viewModel.wishListDraftOrder.collectAsState()

    var draftOrderIdSaved by remember { mutableLongStateOf(0L) }
    var wishListDraftOrder by remember { mutableStateOf(DraftOrder(0L, "", listOf(), "")) }

    var wishListAddButton by remember { mutableStateOf("") }
    var isAddedWishList by remember { mutableStateOf(false) }

    when (draftOrderId) {
        is ApiState.Error -> Log.i("draftOrderTest", "Error: ${(draftOrderId as ApiState.Error).message}")
        ApiState.Loading -> {}
        is ApiState.Success -> {
            draftOrderIdSaved = (draftOrderId as ApiState.Success).data.draft_order.id ?: 0L
            Log.i("TAGGGGG", "ProductsByCategoryScreen: ${(draftOrderId as ApiState.Success).data.draft_order.id}")
            viewModel.getDraftOrders()
        }
        else -> {}
    }
    when (wishListDraftOrderState) {
        is ApiState.Error -> Log.i("draftOrderTest", "Error: ${(wishListDraftOrderState as ApiState.Error).message}")
        ApiState.Loading -> {}
        is ApiState.Success -> {
            wishListDraftOrder = (wishListDraftOrderState as ApiState.Success).data
            isAddedWishList = true
        }
    }

    LaunchedEffect(wishListAddButton,isAddedWishList) {
        if(wishListAddButton.isNotBlank() && isAddedWishList){
            Toast.makeText(context, wishListAddButton, Toast.LENGTH_SHORT).show()
            wishListAddButton = ""
        }
        isAddedWishList = false
    }

    Log.i("TAG", "ProductsByCategoryScreen: ${draftOrderIdSaved}")
    LaunchedEffect(Unit) {
        //Log.d(TAG, categoriesState.toString())
        categoryId?.let {
        viewModel.getProductsByCollectionId(it)
    }
        viewModel.getDraftOrders()
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
                    LatestProductsSectionById(products, navController,viewModel,wishListDraftOrder){ added ->
                        wishListAddButton = added
                    }
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
    body: String?,
    onAddedToFavorite: (String) -> Unit
) {
    val productState by viewModel.productList.collectAsStateWithLifecycle()
    val categoryId: Long? = id?.toLongOrNull()

    val draftOrderId by viewModel.draftOrderId.collectAsState()
    val wishListDraftOrderState by viewModel.wishListDraftOrder.collectAsState()

    var draftOrderIdSaved by remember { mutableLongStateOf(0L) }
    var wishListDraftOrder by remember { mutableStateOf(DraftOrder(0L, "", listOf(), "")) }

    when (draftOrderId) {
        is ApiState.Error -> Log.i("draftOrderTest", "Error: ${(draftOrderId as ApiState.Error).message}")
        ApiState.Loading -> {}
        is ApiState.Success -> {draftOrderIdSaved = (draftOrderId as ApiState.Success).data.draft_order.id ?: 0L
            Log.i("TAGGGGG", "ProductsByCategoryScreen: ${(draftOrderId as ApiState.Success).data.draft_order.id}")
        }
        else -> {}
    }
    when (wishListDraftOrderState) {
        is ApiState.Error -> Log.i("draftOrderTest", "Error: ${(wishListDraftOrderState as ApiState.Error).message}")
        ApiState.Loading -> {}
        is ApiState.Success -> wishListDraftOrder = (wishListDraftOrderState as ApiState.Success).data
    }

    LaunchedEffect(Unit) {
        //Log.d(TAG, categoriesState.toString())
        categoryId?.let {
            viewModel.getProductsByCollectionId(it)

        }
        viewModel.getDraftOrders()
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
                    LatestProductsSectionById(products, navController,viewModel,wishListDraftOrder){ added ->
                        onAddedToFavorite(added)
                    }
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
fun LatestProductsSectionById(products: List<Product>,
                              navController: NavController,
                              viewModel: ProductsByCollectionIdViewModel,
                              oldDraftOrder : DraftOrder,
                              onAddedToFavorite: (String) -> Unit
                              ) {
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
                        delay( 50L)
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
                        ProductCard(product = product, navController, viewModel ,oldDraftOrder){ added ->
                            onAddedToFavorite(added)
                        }
                    }
                    Spacer(modifier = Modifier.height(10.dp))
                }
            }
        }
    }
}

@Composable
fun ProductCard(product: Product,
                navController: NavController,
                viewModel: ProductsByCollectionIdViewModel,
                oldDraftOrder : DraftOrder,
                onAddedToFavorite: (String) -> Unit) {
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
//                Icon(
//                    imageVector = Icons.Default.FavoriteBorder,
//                    contentDescription = "Favorite",
//                    tint = AppColors.Teal,
//                    modifier = Modifier.size(35.dp)
//                )
                var email = FirebaseAuth.getInstance().currentUser?.email
                //Log.i(TAG, "ProductCard: ${product.variants[0]}")
                AddToFavorites(
                    viewModel,
                    product,
                    FirebaseAuth.getInstance().currentUser?.email.toString(),
                    oldDraftOrder,
                    navController
                ) { added ->
                    onAddedToFavorite(added)
                }
            }
        }



    }
}

@Composable
fun AddToFavorites(
    viewModel: ProductsByCollectionIdViewModel,
    product: Product,
    email: String,
    oldDraftOrder: DraftOrder,
    navController: NavController,
    onAddedToFavorite: (String) -> Unit
) {
    var guestClicked by remember { mutableStateOf(false) }
    var clicked by remember { mutableStateOf(false) }
    var isProcessing by remember { mutableStateOf(false) }
    val productState by viewModel.searchProductsList.collectAsState()

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

        IconButton(onClick = {
            isProcessing = true
            viewModel.getProductById(product.id?:0L)
            clicked = oldDraftOrder.line_items.any { it.product_id == product.id }
            when (productState) {
                is ApiState.Error -> {}
                ApiState.Loading -> {
                    Log.i("TAG", "AddToFavorites: loading")}
                is ApiState.Success -> {
                    val p = (productState as ApiState.Success<Product>).data
                    val properties = listOf(Property(name = "imageUrl",value = p.image.src), Property(name = "size",value = p.variants[0].option1), Property(name = "color",value = p.variants[0].option2)
                    )
                    if (oldDraftOrder.id == 0L) {
                        clicked = true
                        val lineItems = listOf(
                            LineItem(
                                title = p.title,
                                price = p.variants[0].price,
                                variant_id = p.variants[0].id,
                                quantity = 1,
                                properties = properties,
                                product_id = p.id ?: 0
                            )
                        )
                        val draftOrder = DraftOrder(note = "wishList", line_items = lineItems, email = email)
                        val draftOrderRequest = DraftOrderRequest(draftOrder)
                        viewModel.createDraftOrder(draftOrderRequest)
                        onAddedToFavorite("Added to WishList")
                        Log.i("catTest", "AddToFavorites: create $draftOrder")
                    } else {
                        if (!oldDraftOrder.line_items.any { it.variant_id == p.variants[0].id }) {
                            clicked = true
                            val lineItems = oldDraftOrder.line_items + listOf(
                                LineItem(
                                    title = p.title,
                                    price = p.variants[0].price,
                                    variant_id = p.variants[0].id,
                                    quantity = 1,
                                    properties = properties,
                                    product_id = p.id ?: 0
                                )
                            )
                            val draftOrder = DraftOrder(note = "wishList", line_items = lineItems, email = email)
                            val draftOrderRequest = DraftOrderRequest(draftOrder)
                            oldDraftOrder.id?.let { viewModel.updateDraftOrder(it, draftOrderRequest) }
                            onAddedToFavorite("Added to WishList")
                            Log.i("catTest", "AddToFavorites: update $draftOrder")
                        } else {
                            clicked = false
                            val lineItems = oldDraftOrder.line_items.filterNot { it.variant_id == p.variants[0].id }
                            val draftOrder = DraftOrder(note = "wishList", line_items = lineItems, email = email)
                            val draftOrderRequest = DraftOrderRequest(draftOrder)
                            if(lineItems.isEmpty()){
                                oldDraftOrder.id?.let { viewModel.deleteDraftOrder(it) }
                            } else {
                                oldDraftOrder.id?.let { viewModel.updateDraftOrder(it, draftOrderRequest) }
                            }
                            onAddedToFavorite("Removed from WishList")
                            Log.i("catTest", "AddToFavorites: remove $draftOrder")
                        }
                    }
                    isProcessing = false
                }
            }
//            val properties = listOf(
//                Property(name = "imageUrl", value = product.image.src),
//                Property(name = "size", value = product.variants[0].option1),
//                Property(name = "color", value = product.variants[0].option2)
//            )


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
            onDismissRequest = { guestClicked = false },
            title = { Text(text = "Guest") },
            text = { Text("You need to login first.") },
            confirmButton = {
                TextButton(
                    onClick = {
                        navController.navigate(Screen.LogInScreen.route) {
                            popUpTo(navController.graph.startDestinationId) {
                                inclusive = true
                            }
                        }
                        guestClicked = false
                    }
                ) {
                    Text("Login", color = AppColors.Teal)
                }
            },
            dismissButton = {
                TextButton(onClick = { guestClicked = false }) {
                    Text("Cancel", color = AppColors.Rose)
                }
            }
        )
    }
}
