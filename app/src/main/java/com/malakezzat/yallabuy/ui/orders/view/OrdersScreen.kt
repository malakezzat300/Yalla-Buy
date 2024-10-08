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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
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
import com.malakezzat.yallabuy.ui.Screen
import com.malakezzat.yallabuy.ui.home.view.CustomTopBar
import com.malakezzat.yallabuy.ui.orders.viewmodel.OrdersViewModel
import com.malakezzat.yallabuy.ui.theme.AppColors

@Composable
fun OrdersScreen(
    viewModel: OrdersViewModel,
    navController: NavController
){

    val customerData by viewModel.customerDataByEmail.collectAsStateWithLifecycle()
    LaunchedEffect(Unit) {
        viewModel.getUserByEmail()
    }
    Scaffold(
        topBar = { CustomTopBarr(navController) },
        containerColor = Color.White,
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
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

}







@Preview(showBackground = true, showSystemUi = true)
@Composable
fun OrderHistoryScreen() {
    Scaffold(
        topBar = {  },
        containerColor = Color.White,
        //  bottomBar = { BottomNavigationBar(navController) }
    ){
        Column(
            modifier = Modifier
                .fillMaxSize()
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

}

@Composable
fun CustomTopBarr(navController: NavController) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 12.dp, start = 10.dp, end = 10.dp, bottom = 10.dp)
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
                    modifier = Modifier.size(24.dp)
                )
            }
            Text(
                text = "Your Orders",
                style = TextStyle(
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold
                ),
                modifier = Modifier.padding(start = 4.dp)
            )
        }
    }
}