package com.malakezzat.yallabuy.ui.wishlist

import android.util.Log
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
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
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Remove
import androidx.compose.material.icons.outlined.Delete
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import coil.compose.AsyncImagePainter.State.Empty.painter
import coil.compose.rememberAsyncImagePainter
import com.malakezzat.yallabuy.R
import com.malakezzat.yallabuy.data.remote.ApiState
import com.malakezzat.yallabuy.data.util.CurrencyConverter
import com.malakezzat.yallabuy.model.DraftOrder
import com.malakezzat.yallabuy.model.DraftOrderRequest
import com.malakezzat.yallabuy.model.LineItem
import com.malakezzat.yallabuy.model.Variant
import com.malakezzat.yallabuy.ui.Screen
import com.malakezzat.yallabuy.ui.home.view.TAG
import com.malakezzat.yallabuy.ui.search.SearchViewModel
import com.malakezzat.yallabuy.ui.shoppingcart.view.DeleteConfirmationDialog
import com.malakezzat.yallabuy.ui.shoppingcart.view.calculateSubtotal
import com.malakezzat.yallabuy.ui.shoppingcart.viewmodel.ShoppingCartViewModel


@Composable
fun WishlistScreen(viewModel: WishlistViewModel, navController: NavController) {

    val wishlistItems by viewModel.wishlistDraftOrder.collectAsStateWithLifecycle()
    var isLoading by remember { mutableStateOf( false ) }
    var orderItems by remember { mutableStateOf( emptyList<LineItem>() ) }
    var draftOrder by remember { mutableStateOf( DraftOrder() ) }
    var showDialog by remember { mutableStateOf(false) }
    var isWishlistEmpty by remember { mutableStateOf(false) }
    val variantState by viewModel.variantId.collectAsState()
    var variant by remember { mutableStateOf(Variant()) }
    var variantSet by remember { mutableStateOf(mutableSetOf<Variant>() ) }

    LaunchedEffect(Unit) {
        viewModel.getDraftOrders()
    }

    when(wishlistItems){
        is ApiState.Error ->{
            isLoading = false

           // Log.i("shoppingCartTest", "ShoppingCartScreen: draftOrder ${(shoppingCartOrder as ApiState.Error).message}")
        }
        ApiState.Loading -> {
            isLoading = true
            CircularProgressIndicator()
        }
        is ApiState.Success -> {
            isLoading = false
            orderItems = (wishlistItems as ApiState.Success).data.line_items
            draftOrder = (wishlistItems as ApiState.Success).data
            LaunchedEffect (Unit){
                viewModel.getVariantById(orderItems.get(0).variant_id)
            }
        }
    }
    if(orderItems.isNotEmpty()){
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.White)
                .padding(16.dp)
        ) {
            TopAppBar(
                title = { Text(text = "Wishlist") },
                navigationIcon = {
                    IconButton(onClick = { navController.navigate(Screen.HomeScreen.route) }) {
                        Image(
                            painter = painterResource(id = R.drawable.back_arrow),
                            contentDescription = "Search Icon",
                            modifier = Modifier.size(24.dp)
                        )
                    }
                },
                backgroundColor = Color.White,
                elevation = 0.dp
            )

            Spacer(modifier = Modifier.height(16.dp))

            LazyColumn {
                items(orderItems) { product ->
                    WishlistItem(viewModel,product,draftOrder,navController)
                  //  orderItems = orderItems.filter { it != product }
                }
            }
        }
    }else{
        if(!isLoading){
            EmptyWishlistScreen(navController)
        }
    }

}

@Composable
fun WishlistItem(viewModel: WishlistViewModel,
                 item: LineItem,
                 draftOrder: DraftOrder,
                 navController: NavController
                 ) {
    val context = LocalContext.current
    CurrencyConverter.initialize(context)
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(120.dp) // Adjust height as needed
            .padding(8.dp)
            .clickable { navController.navigate("${Screen.ProductInfScreen.route}/${item.product_id}") },
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 8.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        border = BorderStroke(1.dp, Color(0xFFE0E0E0))
    ) {
        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically // Align items vertically
        ) {
            // Product image
            Image(
                painter = rememberAsyncImagePainter(item.properties[0].value),
                contentDescription = null,
                modifier = Modifier
                    .size(80.dp)
                    .clip(RoundedCornerShape(8.dp))
                    .background(Color.LightGray)
            )

            Spacer(modifier = Modifier.width(16.dp))

            // Product details
            Column(
                modifier = Modifier.weight(1f) // Let the column take available space
            ) {
                Text(
                    text = item.title,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                Spacer(modifier = Modifier.height(4.dp))
                val price = item.price
                CurrencyConverter.changeCurrency(price.toDouble())?.let {
                    Text(text = it,
                        style = MaterialTheme.typography.bodyMedium)
                }
                /*Text(
                    text = item.price,
                    color = Color.Black,
                    fontSize = 14.sp
                )*/

            }
            var showDialog by remember { mutableStateOf(false) }
            // Delete button
            IconButton(onClick = { showDialog = true }) {
                Icon(Icons.Outlined.Delete, contentDescription = "Delete item", tint = Color.Red)
            }

            if (showDialog) {
                DeleteConfirmationDialog2(
                    showDialog = showDialog,
                    onDismiss = { showDialog = false },
                    onConfirmDelete = {
                        showDialog = false
                        if (draftOrder.line_items.size > 1) {
                            val updatedDraftItems = draftOrder.line_items.filter { it != item }
                            val updatedDraftOrder = draftOrder.copy(line_items = updatedDraftItems)
                            val updatedDraftItemsRequest = DraftOrderRequest(updatedDraftOrder)
                            draftOrder.id?.let {
                                viewModel.updateDraftOrder(it, updatedDraftItemsRequest)
                            }
                        } else {
                            draftOrder.id?.let { viewModel.deleteDraftOrder(it) }
                            navController.popBackStack(Screen.WishlistScreen.route, inclusive = true)
                             navController.navigate(Screen.WishlistScreen.route)


                        }

                    }

                )
            }
        }
        }
    }

@Composable
fun EmptyWishlistScreen(navController: NavController) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Image(
            painter = painterResource(id = R.drawable.emptylist),
            contentDescription = null,
            modifier = Modifier
                .size(200.dp)
        )

        Spacer(modifier = Modifier.height(16.dp))


        Text(
            text = "Your wishlist is empty",
            style = MaterialTheme.typography.headlineMedium,
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(8.dp))

        // Subtext
        Text(
            text = "Tap heart button to start saving your favorite items",
            style = MaterialTheme.typography.bodyMedium,
            color = Color.Gray,
            textAlign = TextAlign.Center
        )
        Spacer(modifier = Modifier.height(32.dp))
        Button(
            onClick = { navController.navigate(Screen.CategoriesScreen.route) },
            modifier = Modifier.fillMaxWidth(),
            colors = ButtonDefaults.buttonColors(containerColor = Color.Black),
            shape = RoundedCornerShape(16.dp)
        ) {
            Text(text = "Explore Categories", color = Color.White)
        }

        Spacer(modifier = Modifier.height(16.dp))
    }
}
@Composable
fun DeleteConfirmationDialog2(
    showDialog: Boolean,
    onDismiss: () -> Unit,
    onConfirmDelete: () -> Unit
) {
    if (showDialog) {
        AlertDialog(
            onDismissRequest = { onDismiss() },
            title = {
                Text(text = "Delete Confirmation")
            },
            text = {
                Text("Are you sure you want to delete this item? This action cannot be undone.")
            },
            confirmButton = {
                Button(
                    onClick = {
                        onConfirmDelete()
                        onDismiss()
                    }
                ) {
                    Text("Delete")
                }
            },
            dismissButton = {
                Button(
                    onClick = { onDismiss() }
                ) {
                    Text("Cancel")
                }
            }
        )
    }
}