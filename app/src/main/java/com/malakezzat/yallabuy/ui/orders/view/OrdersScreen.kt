package com.malakezzat.yallabuy.ui.orders.view

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.malakezzat.yallabuy.R
import com.malakezzat.yallabuy.data.remote.ApiState
import com.malakezzat.yallabuy.model.Order
import com.malakezzat.yallabuy.ui.Screen
import com.malakezzat.yallabuy.ui.orders.viewmodel.OrdersViewModel
import com.malakezzat.yallabuy.ui.theme.AppColors

/*@Composable
fun OrdersScreen(
    viewModel: OrdersViewModel,
    navController: NavController
){

    val customerData by viewModel.customerDataByEmail.collectAsStateWithLifecycle()
    val customerOrders by viewModel.customerOrders.collectAsStateWithLifecycle()
    LaunchedEffect(Unit) {
        viewModel.getUserByEmail()
    }
    Scaffold(
        topBar = { CustomTopBarr(navController) },
        containerColor = Color.White,
        content = { paddingValues ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
                    .padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {


                // Image (replace with actual image)
                Image(
                    painter = painterResource(id = R.drawable.no_order_img),
                    contentDescription = null,
                    modifier = Modifier
                        .size(250.dp)
                        .padding(bottom = 16.dp)
                )

                // No completed order text
                Text(
                    text = "No completed order",
                    color = AppColors.Rose,
                    style = MaterialTheme.typography.headlineLarge,
                    modifier = Modifier.padding(bottom = 8.dp)
                )

                // Additional text
                Text(
                    text = "We don’t have any past orders that have been completed. Start shopping now and create your first order with us.",
                    color = AppColors.GrayDark,
                    style = MaterialTheme.typography.bodyMedium,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.padding(horizontal = 24.dp)
                )

                Spacer(modifier = Modifier.height(32.dp))

                // Explore Categories Button
                Button(
                    onClick = { navController.navigate(Screen.CategoriesScreen.route) },
                    modifier = Modifier.fillMaxWidth(),
                    colors = ButtonDefaults.buttonColors(containerColor = AppColors.Rose),
                    shape = RoundedCornerShape(20.dp)
                ) {
                    Text(text = "Explore Categories", color = Color.White)
                }

                Spacer(modifier = Modifier.height(16.dp))

            }

        }
    )
}*/





@Composable
fun OrdersScreen(
    viewModel: OrdersViewModel,
    navController: NavController
) {
    val customerOrders by viewModel.customerOrders.collectAsStateWithLifecycle()
    when (customerOrders) {
        is ApiState.Loading -> {
            CircularProgressIndicator()
        }

        is ApiState.Success -> {
            val orders = (customerOrders as ApiState.Success<List<Order>>).data

            if (orders.isNullOrEmpty()) {
                NoOrdersScreen(navController)
            } else {
                CompletedOrdersScreen(orders,navController)
            }
        }

        is ApiState.Error -> {
            Text(text = "Error loading orders: ${(customerOrders as ApiState.Error).message}")
        }
    }
}

@Composable
fun NoOrdersScreen(navController: NavController) {
    Scaffold(
        topBar = { CustomTopBarr(navController) },
        containerColor = Color.White,
        content = { paddingValues ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
                    .padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {

                Image(
                    painter = painterResource(id = R.drawable.no_order_img),
                    contentDescription = null,
                    modifier = Modifier
                        .size(250.dp)
                        .padding(bottom = 16.dp)
                )
                Text(
                    text = "No completed order",
                    color = AppColors.Rose,
                    style = MaterialTheme.typography.headlineLarge,
                    modifier = Modifier.padding(bottom = 8.dp)
                )
                Text(
                    text = "We don’t have any past orders that have been completed. Start shopping now and create your first order with us.",
                    color = AppColors.GrayDark,
                    style = MaterialTheme.typography.bodyMedium,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.padding(horizontal = 24.dp)
                )

                Spacer(modifier = Modifier.height(32.dp))
                Button(
                    onClick = { navController.navigate(Screen.CategoriesScreen.route) },
                    modifier = Modifier.fillMaxWidth(),
                    colors = ButtonDefaults.buttonColors(containerColor = AppColors.Rose),
                    shape = RoundedCornerShape(20.dp)
                ) {
                    Text(text = "Explore Categories", color = Color.White)
                }

                Spacer(modifier = Modifier.height(16.dp))
            }
        }
    )
}

@Composable
fun CompletedOrdersScreen(orders: List<Order>,navController: NavController) {
    Scaffold(
        topBar = { CustomTopBarr(navController) },
        containerColor = Color.White,
        content = { paddingValues ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
                    .padding(16.dp)
            ) {

                LazyColumn {
                    itemsIndexed(orders) {_,order ->
                        OrderItem(order = order)
                    }
                }
            }
        }
    )
}

@Composable
fun OrderItem(order: Order) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        elevation = CardDefaults.cardElevation(4.dp),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
           // Text(text = "Order ID: ${order.id}", style = MaterialTheme.typography.bodyMedium)
            order.line_items.forEach { item ->
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = item.title,
                        fontSize = 22.sp,
                        style = MaterialTheme.typography.bodySmall,
                        color = AppColors.Teal
                    )
                    /*Text(
                        text = item.title,
                        fontSize = 15.sp,
                        style = MaterialTheme.typography.bodySmall
                    )*/
                }

                Spacer(modifier = Modifier.height(8.dp))
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Item variant: ",
                        fontSize = 18.sp,
                        style = MaterialTheme.typography.bodySmall.copy(fontWeight = FontWeight.Bold),
                        color = AppColors.GrayDark
                    )
                    Text(
                        text = "${item.variant_title}",
                        fontSize = 12.sp,
                        style = MaterialTheme.typography.bodySmall.copy(fontWeight = FontWeight.Bold),
                        color = AppColors.GrayLight
                    )
                }

                Spacer(modifier = Modifier.height(8.dp))

                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Quantity: ",
                        fontSize = 18.sp,
                        style = MaterialTheme.typography.bodySmall.copy(fontWeight = FontWeight.Bold),
                        color = AppColors.GrayDark
                    )
                    Text(
                        text = "${item.quantity}",
                        fontSize = 18.sp,
                        style = MaterialTheme.typography.bodySmall,
                        color = AppColors.GrayLight
                    )
                }

                Spacer(modifier = Modifier.height(8.dp))

                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Price: ",
                        fontSize = 18.sp,
                        style = MaterialTheme.typography.bodySmall.copy(fontWeight = FontWeight.Bold),
                        color = AppColors.GrayDark
                    )
                    Text(
                        text = "${item.price} EGP",
                        fontSize = 18.sp,
                        style = MaterialTheme.typography.bodySmall,
                        color = AppColors.GrayLight
                    )
                }

                Spacer(modifier = Modifier.height(8.dp))
            }
            Spacer(modifier = Modifier.height(8.dp))
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Order Total: ",
                    fontSize = 18.sp,
                    style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Bold)
                )
                Text(
                    text = "${order.total_price} EGP",
                    fontSize = 18.sp,
                    style = MaterialTheme.typography.bodyMedium,
                    color = AppColors.Teal
                )
            }

        }
    }
}


@Preview(showBackground = true, showSystemUi = true)
@Composable
fun OrderHistoryScreen() {
    Scaffold(
        topBar = {  },
        containerColor = Color.White,
        content = { paddingValues ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
                    .padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {


            // Image (replace with actual image)
            Image(
                painter = painterResource(id = R.drawable.no_order_img),
                contentDescription = null,
                modifier = Modifier
                    .size(250.dp)
                    .padding(bottom = 16.dp)
            )

            // No completed order text
            Text(
                text = "No completed order",
                color = AppColors.Rose,
                style = MaterialTheme.typography.headlineLarge,
                modifier = Modifier.padding(bottom = 8.dp)

            )

            // Additional text
            Text(
                text = "We don’t have any past orders that have been completed. Start shopping now and create your first order with us.",
                color = AppColors.GrayDark,
                style = MaterialTheme.typography.bodyMedium,
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(horizontal = 24.dp)
            )

            Spacer(modifier = Modifier.height(32.dp))

            // Explore Categories Button
            Button(
                onClick = { /* Action to explore categories */ },
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.buttonColors(containerColor = AppColors.Rose),
                shape = RoundedCornerShape(20.dp)
            ) {
                Text(text = "Explore Categories", color = Color.White)
            }

            Spacer(modifier = Modifier.height(16.dp))

        }
    }
    )
}

@Composable
fun CustomTopBarr(navController: NavController) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 24.dp, start = 10.dp, end = 10.dp, bottom = 10.dp)
            .background(Color.White),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            /*Image(
                painter = painterResource(id = R.drawable.logo), // logo
                contentDescription = "Logo",
                modifier = Modifier.size(30.dp)
            )*/

            IconButton(onClick = { navController.navigate(Screen.HomeScreen.route) }) {
                Image(
                    painter = painterResource(id = R.drawable.back_arrow),
                    contentDescription = "Search Icon",
                    modifier = Modifier.size(24.dp),
                )
            }
            Text(
                text = "Your Orders",
                style = TextStyle(
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold
                    , color = AppColors.Teal
                ),
                modifier = Modifier.padding(start = 4.dp)
            )
        }
    }
}