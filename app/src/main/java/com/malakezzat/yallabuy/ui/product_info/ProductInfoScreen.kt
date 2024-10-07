package com.malakezzat.yallabuy.ui.product_info

import android.util.Log
import androidx.compose.foundation.Image
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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
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
import androidx.compose.ui.text.font.FontWeight

import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import coil.compose.rememberAsyncImagePainter
import com.google.firebase.auth.FirebaseAuth
import com.malakezzat.yallabuy.data.remote.ApiState
import com.malakezzat.yallabuy.model.Customer
import com.malakezzat.yallabuy.model.DraftOrder
import com.malakezzat.yallabuy.model.LineItem
import com.malakezzat.yallabuy.model.Product
import com.malakezzat.yallabuy.model.*


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
fun AddToFavorites(viewModel: ProductInfoViewModel,product : Product,email : String,oldDraftOrder : DraftOrder){
    Button(onClick = {
        //val properties = Properties(listOf(Property(name = "imageUrl",value = product.image.src)))
        if(oldDraftOrder.id == 0L) {
            val lineItems = listOf(LineItem(product.title,product.variants[0].price,product.variants[0].id,1))
            val draftOrder = DraftOrder(note = "wishList", line_items = lineItems, email = email)
            val draftOrderRequest = DraftOrderRequest(draftOrder)
            viewModel.createDraftOrder(draftOrderRequest)
        } else {
            val lineItems = oldDraftOrder.line_items + listOf(LineItem(product.title,product.variants[0].price,product.variants[0].id,1))
            val draftOrder = DraftOrder(note = "wishList", line_items = lineItems, email = email)
            val draftOrderRequest = DraftOrderRequest(draftOrder)
            oldDraftOrder.id?.let { viewModel.updateDraftOrder(it,draftOrderRequest) }
        }
    }) {
        Text("Add to Favorites")
    }
}

@Composable
fun AddToCart(viewModel: ProductInfoViewModel,product : Product,email : String,oldDraftOrder : DraftOrder) {
        Button(onClick = {
            //val properties = Properties(listOf(Property(name = "imageUrl",value = product.image.src)))
            if(oldDraftOrder.id == 0L) {
                val lineItems = listOf(LineItem(product.title,product.variants[0].price,product.variants[0].id,1))
                val draftOrder = DraftOrder(note = "shoppingCart", line_items = lineItems, email = email)
                val draftOrderRequest = DraftOrderRequest(draftOrder)
                viewModel.createDraftOrder(draftOrderRequest)
            } else {
                val lineItems = oldDraftOrder.line_items + listOf(LineItem(product.title,product.variants[0].price,product.variants[0].id,1))
                val draftOrder = DraftOrder(note = "shoppingCart", line_items = lineItems, email = email)
                val draftOrderRequest = DraftOrderRequest(draftOrder)
                oldDraftOrder.id?.let { viewModel.updateDraftOrder(it,draftOrderRequest) }
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

@Composable
fun ProductInfoScreenPreview(){
   // ProductInfoScreen(Product(0,"","","","","", em,),NavController(LocalContext.current))
}