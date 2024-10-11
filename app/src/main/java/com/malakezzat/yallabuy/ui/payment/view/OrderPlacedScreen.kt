package com.malakezzat.yallabuy.ui.payment.view

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.malakezzat.yallabuy.R
import com.malakezzat.yallabuy.ui.Screen
import com.malakezzat.yallabuy.ui.theme.AppColors

@Composable
fun OrderPlacedScreen(navController: NavController) {
    Scaffold(
        containerColor = Color.White,
        content = { paddingValues ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
                    .padding(16.dp),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Image(
                    painter = painterResource(id = R.drawable.order_placed),
                    contentDescription = "Empty Cart",
                    modifier = Modifier.size(280.dp)
                )

                Text(
                    text = "Your order has been placed successfully",
                    color = AppColors.Rose,
                    style = MaterialTheme.typography.headlineLarge,
                    modifier = Modifier.padding(bottom = 8.dp)
                )

                Text(
                    text = "Thank you for choosing us! Feel free to continue shopping and explore our wide range of products. Happy Shopping!",
                    color = AppColors.GrayDark,
                    style = MaterialTheme.typography.bodyMedium,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.padding(horizontal = 24.dp)
                )


                Button(
                    onClick = {
                        navController.popBackStack(Screen.OrderPlacedScreen.route, inclusive = true)
                        navController.navigate(Screen.HomeScreen.route)
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 16.dp)
                        .height(56.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = Color.Black),
                    shape = RoundedCornerShape(10.dp)
                ) {
                    Text(
                        text = "Continue Shopping",
                        color = Color.White,
                        fontSize = 16.sp
                    )
                }
            }
        })
    //  bottomBar = { BottomNavigationBar(navController) }

}
