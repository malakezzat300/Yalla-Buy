package com.malakezzat.yallabuy.ui.shoppingcart.view

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.ModalBottomSheetLayout
import androidx.compose.material.ModalBottomSheetState
import androidx.compose.material.ModalBottomSheetValue
import androidx.compose.material.rememberModalBottomSheetState
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.malakezzat.yallabuy.ui.theme.YallaBuyTheme
import com.malakezzat.yallabuy.R
import kotlinx.coroutines.launch
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.navigation.NavController
import com.malakezzat.yallabuy.data.ProductsRepository
import com.malakezzat.yallabuy.ui.Screen
import com.malakezzat.yallabuy.ui.shoppingcart.viewmodel.ShoppingCartViewModel

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun ShoppingCartScreen(
    viewModel: ShoppingCartViewModel,
    navController: NavController,
) {

    val bottomSheetState = rememberModalBottomSheetState(initialValue = ModalBottomSheetValue.Hidden)

    ModalBottomSheetLayout(
        sheetState = bottomSheetState,
        sheetContent = {
            VoucherBottomSheet()
        },
        content = {
            val list = listOf("1", "2")
            if (list.isNotEmpty()) {
                Scaffold(
                    topBar = { CustomTopBar(bottomSheetState) },
                    content = { paddingValues ->
                        Box(
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(paddingValues)
                        ) {
                            LazyColumn(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .align(Alignment.TopCenter)
                                    .padding(bottom = 230.dp)
                            ) {
                                items(list.size) {
                                    ShoppingItem()
                                }
                            }

                            ShoppingView(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .background(MaterialTheme.colorScheme.surface)
                                    .align(Alignment.BottomCenter)
                                    .padding(16.dp)
                            )
                        }
                    }
                )
            } else {
                ShoppingEmpty()
            }
        }
    )
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun CustomTopBar(sheetState: ModalBottomSheetState) {
    val coroutineScope = rememberCoroutineScope()

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 38.dp, start = 10.dp, end = 10.dp, bottom = 10.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Spacer(modifier = Modifier.padding(2.dp))
            IconButton(
                modifier = Modifier.size(30.dp),
                onClick = { /* Decrease action */ }
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_back),
                    contentDescription = "back"
                )
            }

            Spacer(modifier = Modifier.padding(6.dp))
            Text(
                text = "My Cart",
                style = TextStyle(fontSize = 20.sp),
                modifier = Modifier.padding(start = 4.dp)
            )
        }

        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.clickable {
                coroutineScope.launch {
                    sheetState.show()
                }
            }
        ) {
            Text(
                text = "Voucher Code",
                style = TextStyle(
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Medium
                ),
                color = Color.Green
            )
        }
    }
}

@Composable
fun ShoppingItem(){
    Row(
        modifier = Modifier
            .padding(8.dp)
            .fillMaxWidth()
            .height(140.dp)
    ) {
        Card(
            modifier = Modifier
                .weight(1.2f)
                .padding(8.dp)
                .fillMaxHeight(),
            shape = RoundedCornerShape(12.dp),
            elevation = CardDefaults.cardElevation(12.dp),
        ) {
            Image(
                modifier = Modifier
                    .fillMaxHeight()
                    .clip(shape = RoundedCornerShape(10)),
                painter = painterResource(R.drawable.ad2),
                contentDescription = "ad",
                contentScale = ContentScale.FillBounds,
            )
        }

        Column(
            modifier = Modifier
                .padding(4.dp)
                .weight(2f)
                .fillMaxHeight(),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Column {
                Text(
                    text = "Loop Silicone Strong Magnetic Watch Band",
                    style = MaterialTheme.typography.bodyLarge,
                    modifier = Modifier.padding(top = 4.dp,bottom = 4.dp)
                )
                Text(
                    text = "$15.25",  // Second Text
                    style = MaterialTheme.typography.bodyMedium,
                )
            }

            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.fillMaxWidth()
            ) {
                IconButton(
                    onClick = { /* decrease action */ }
                ) {
                    Icon(
                        painter = painterResource(R.drawable.ic_minus),
                        contentDescription = "minus",
                    )
                }

                Text(
                    text = "1",
                    style = MaterialTheme.typography.bodyLarge,
                    fontWeight = FontWeight.Bold
                )

                IconButton(
                    onClick = { /* increase action */ }
                ) {
                    Icon(
                        painter = painterResource(R.drawable.ic_add),
                        contentDescription = "add",
                    )
                }

                Spacer(modifier = Modifier.width(16.dp))

                IconButton(
                    onClick = { /* Delete action */ }
                ) {
                    Icon(
                        painter = painterResource(R.drawable.ic_delete),
                        contentDescription = "Delete",
                        tint = Color.Red
                    )
                }
            }
        }
    }
}

@Composable
fun ShoppingView(
    modifier: Modifier
) {
    Box(
        modifier = modifier

    ) {
        Column(
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Text(
                text = "Order Info",
                style = MaterialTheme.typography.headlineSmall,
                modifier = Modifier.padding(bottom = 8.dp)
            )

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(text = "Subtotal", style = MaterialTheme.typography.bodyLarge)
                Text(text = "$27.25", style = MaterialTheme.typography.bodyLarge)
            }

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(text = "Discount", style = MaterialTheme.typography.bodyLarge)
                Text(text = "$0.00", style = MaterialTheme.typography.bodyLarge)
            }

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = "Total",
                    style = MaterialTheme.typography.bodyLarge,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = "$27.25",
                    style = MaterialTheme.typography.bodyLarge,
                    fontWeight = FontWeight.Bold
                )
            }

            Button(
                onClick = { /* Action for button click */ },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 16.dp)
                    .height(56.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color.Black),
                shape = RoundedCornerShape(10.dp)
            ) {
                Text(
                    text = "Checkout (2)",
                    color = Color.White,
                    fontSize = 16.sp
                )
            }
        }
    }
}
@Composable
fun ShoppingEmpty(){
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Image(
            painter = painterResource(id = R.drawable.empty_cart_image),
            contentDescription = "Empty Cart",
            modifier = Modifier.size(280.dp)
        )

        Text(
            text = "Your cart is empty",
            style = MaterialTheme.typography.headlineMedium,
            modifier = Modifier.padding(top = 16.dp),
            fontWeight = FontWeight.Bold
        )

        Text(
            text = "Looks like you have not added anything in your cart. Go ahead and explore top categories.",
            style = MaterialTheme.typography.bodyMedium,
            modifier = Modifier
                .padding(top = 8.dp)
                .wrapContentWidth(),
            textAlign = TextAlign.Center
        )


        Button(
            onClick = { /* Action for button click */ },
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 16.dp)
                .height(56.dp),
            colors = ButtonDefaults.buttonColors(containerColor = Color.Black),
            shape = RoundedCornerShape(10.dp)
        ) {
            Text(
                text = "Explore Categories",
                color = Color.White,
                fontSize = 16.sp
            )
        }
    }
}

@Composable
fun VoucherBottomSheet() {
    var voucherCode by remember { mutableStateOf("") }

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(topStart = 12.dp, topEnd = 12.dp))
            .background(MaterialTheme.colorScheme.surface)
            .padding(16.dp)
    ) {
        Column {
            Text(
                text = "Voucher Code",
                style = MaterialTheme.typography.titleLarge,
                modifier = Modifier.padding(bottom = 8.dp)
            )

            OutlinedTextField(
                value = voucherCode,
                onValueChange = { voucherCode = it },
                label = { Text("Enter voucher code") },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 16.dp)
            )

            Button(
                onClick = { /* Action for button click */ },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color.Black),
                shape = RoundedCornerShape(10.dp)
            ) {
                Text(
                    text = "Apply Voucher",
                    color = Color.White,
                    fontSize = 16.sp
                )
            }
        }
    }
}

/*
@Composable
fun ShoppingScreenPreview() {
    YallaBuyTheme {
        ShoppingCartScreen()
    }
}*/
