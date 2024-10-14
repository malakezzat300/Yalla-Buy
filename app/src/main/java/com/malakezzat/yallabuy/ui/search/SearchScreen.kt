package com.malakezzat.yallabuy.ui.search

import android.util.Log
import android.widget.Toast
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
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.List
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.sharp.Favorite
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderColors
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldColors
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
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import coil.compose.AsyncImage
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
import com.malakezzat.yallabuy.ui.home.viewmodel.HomeScreenViewModel
import com.malakezzat.yallabuy.ui.product_info.AddToFavorites
import com.malakezzat.yallabuy.ui.product_info.ProductInfoViewModel
import com.malakezzat.yallabuy.ui.theme.AppColors

//@Preview(showSystemUi = true)
@Composable
fun SearchScreen(viewModel: SearchViewModel,
                 navController: NavController
) {
    val searchQuery by viewModel.searchQuery.collectAsState()
    val filteredProducts by viewModel.filteredProducts.collectAsState()
    var query by remember { mutableStateOf("") }
    var sliderPosition by remember { mutableStateOf(100.0f) }

    val draftOrderId by viewModel.draftOrderId.collectAsState()
    val wishListDraftOrderState by viewModel.wishListDraftOrder.collectAsState()

    var draftOrderIdSaved by remember { mutableLongStateOf(0L) }
    var wishListDraftOrder by remember { mutableStateOf(DraftOrder(0L, "", listOf(), "")) }

    var wishListAddButton by remember { mutableStateOf("") }
    var isAddedWishList by remember { mutableStateOf(false) }
    //Currency
    LaunchedEffect(Unit) { viewModel.getDraftOrders() }
    val context = LocalContext.current
    CurrencyConverter.initialize(context)

    when (draftOrderId) {
        is ApiState.Error -> Log.i("draftOrderTest", "Error: ${(draftOrderId as ApiState.Error).message}")
        ApiState.Loading -> {}
        is ApiState.Success -> {
            draftOrderIdSaved = (draftOrderId as ApiState.Success).data.draft_order.id ?: 0L
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


    Scaffold(
       topBar = { CustomTopBar(navController,"",Color.White) },
        containerColor = Color.White,
        content = { paddingValues ->
            Column(
                modifier = Modifier
                    .padding(paddingValues)
                    .fillMaxWidth()
                    .padding(18.dp)
                    .background(Color.White)
            ) {
                Spacer(modifier = Modifier.height(8.dp))
                OutlinedTextField(
                    value = query,
                    onValueChange = {input->
                        query=input
                        viewModel.onSearchQueryChanged(input,sliderPosition)
                    },
                    placeholder = { Text("Search") },
                    leadingIcon = { Image(
                        painter = painterResource(id = R.drawable.search_normal), // logo
                        contentDescription = "Search Icon",
                        modifier = Modifier.size(16.dp)
                    ) },
                    shape = RoundedCornerShape(20.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(color = Color.White)
                        .padding(10.dp),
                    colors =  OutlinedTextFieldDefaults.colors(unfocusedBorderColor = AppColors.Teal)
                )

                Spacer(modifier = Modifier.height(16.dp))

                // Price slider

                Spacer(modifier = Modifier.height(16.dp))
                // Recent Searches List
                if(searchQuery.isEmpty()){

                }else{
                    val price = sliderPosition.toInt()
                    CurrencyConverter.changeCurrency(price.toDouble())?.let {
                        Text(text = "Max Price: ${it} ",
                            style = MaterialTheme.typography.bodyMedium)
                    }
                    /*Text(text = "Max Price: ${sliderPosition.toInt()} ")*/
                    Slider(
                        value = sliderPosition,
                        onValueChange = { input ->
                            sliderPosition = input
                            // viewModel.onSearchQueryChanged(searchQuery, input)
                        },
                        valueRange = 0f..200f,
                        modifier = Modifier.fillMaxWidth()
                        , onValueChangeFinished = {
                            viewModel.onSearchQueryChanged(searchQuery, sliderPosition)
                        },
                        colors = SliderDefaults.colors(
                            thumbColor = AppColors.MintGreen,
                            activeTrackColor = AppColors.MintGreen,
                            inactiveTrackColor = Color.Gray
                        )
                    )

                    LazyVerticalGrid(
                        columns = GridCells.Fixed(2),
                        contentPadding = PaddingValues(8.dp),
                        modifier = Modifier.background(Color.White)
                    ) {
                        items(filteredProducts) { searchItem ->
                            RecentSearchItem(searchItem,navController,viewModel,wishListDraftOrder){ added ->
                                wishListAddButton = added
                            }
                        }
                    }
                }

            }
        })

}
@Composable
fun RecentSearchItem(product: Product,
                     navController: NavController,
                     viewModel: SearchViewModel,
                     oldDraftOrder : DraftOrder,
                     onAddedToFavorite : (String) -> Unit) {

    Box(
        modifier = Modifier
            .padding(8.dp)
            .background(Color.White, shape = RoundedCornerShape(8.dp))
            .clickable { /* Handle click */ }
    ) {
        Column(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth()
        ) {
            // Placeholder for product image
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(100.dp)
                    .background(Color.White)
                    .clickable { navController.navigate("${Screen.ProductInfScreen.route}/${product.id}") }
            ) {
                if (product.image != null) {
                    AsyncImage(
                        model = product.image.src,
                        contentDescription = product.title,
                        modifier = Modifier
                            .size(128.dp)
                            .clip(RoundedCornerShape(8.dp))
                            .background(Color.LightGray),
                        contentScale = ContentScale.Crop
                    )
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            // Product name and price
            Text(text = product?.title ?: "title", style = MaterialTheme.typography.bodyMedium)
            val price2 = product?.variants?.get(0)?.price ?: "200"
            CurrencyConverter.changeCurrency(price2.toDouble())?.let {
                Text(text = it, style = MaterialTheme.typography.bodyMedium)
            }
        }

        // Icon positioned at the end of the box
        Box(
            modifier = Modifier
                .align(Alignment.TopEnd) // Adjust this alignment as needed
                .padding(10.dp)
        ) {
//            Icon(
//                imageVector = Icons.Default.FavoriteBorder,
//                contentDescription = "Favorite",
//                tint = AppColors.Teal,
//                modifier = Modifier.size(35.dp)
//            )
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
@Composable
fun AddToFavorites(
    viewModel: SearchViewModel,
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
            title = { Text(text = "Guest", color = AppColors.Teal) },
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
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = AppColors.Teal)
                ) {
                    Text("Login", color = Color.White)
                }
            },
            dismissButton = {
                TextButton(onClick = { guestClicked = false },
                    colors = ButtonDefaults.buttonColors(containerColor = AppColors.Teal)) {
                    Text("Cancel", color = Color.White)
                }
            }
        )
    }
}
