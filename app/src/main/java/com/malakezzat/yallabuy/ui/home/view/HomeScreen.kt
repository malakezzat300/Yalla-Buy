package com.malakezzat.yallabuy.ui.home.view

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.gestures.animateScrollBy
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.BottomNavigation
import androidx.compose.material.BottomNavigationItem
import androidx.compose.material.Scaffold
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.MailOutline
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.malakezzat.yallabuy.R
import com.malakezzat.yallabuy.ui.home.viewmodel.HomeScreenViewModel
import kotlinx.coroutines.delay

@Composable
fun HomeScreen(
    viewModel: HomeScreenViewModel,
    navController: NavController
){
    Scaffold(
        topBar = { TopBar() },
        bottomBar = { BottomNavigationBar() }
    ) {
        Column(
            modifier = Modifier
                .padding(it)
                .verticalScroll(rememberScrollState())
        ) {
            AdList()
            CategoriesSection()
            LatestProductsSection()
        }
    }

}




@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopBar() {
    CenterAlignedTopAppBar(
        title = {
            Text("Yalla Buy", style = MaterialTheme.typography.titleLarge)
        },
        actions = {
            IconButton(onClick = { /* Search Action */ }) {
                Icon(Icons.Default.Search, contentDescription = "Search")
            }
        },
        colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
            containerColor = Color.White,
            titleContentColor = Color.Black,
            actionIconContentColor = Color.Black
        )
    )
}


@Composable
fun AdList() {
    val context = LocalContext.current
    val scrollState = rememberLazyListState()
    var scrollDirection by remember { mutableStateOf(1) }

    LazyRow(state = scrollState) {
        items(10){
            AdCard(painterResource(id = R.drawable.ad1))
            AdCard(painterResource(id = R.drawable.ad2))
        }
    }

    LaunchedEffect(Unit) {
        delay(2000)
        val needToScrollItems = 20 - scrollState.layoutInfo.visibleItemsInfo.size + 1
        val scrollWidth = needToScrollItems * ( context.resources.displayMetrics.widthPixels )
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
fun AdCard(painter : Painter) {
    Card (
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
//@Preview(showBackground = true, showSystemUi = true)
@Composable
fun CategoriesSection() {
    Column(modifier = Modifier.padding(16.dp)) {
        Row (
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ){
            Text("Categories", style = TextStyle(fontSize = 20.sp, fontWeight = FontWeight.Bold))
            Text("SEE ALL", style = TextStyle(fontSize = 14.sp, fontWeight = FontWeight.Medium),
                color = Color.Cyan
            )
        }

        LazyRow(
            modifier = Modifier.padding(top = 8.dp),
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            items(25) { CategoryItem("Electronics", Icons.Default.MailOutline)}
        }
    }
}

@Composable
fun CategoryItem(name: String, icon: ImageVector) {
    Column(modifier = Modifier
        .padding(8.dp)
        .clip(RoundedCornerShape(12.dp))
        .background(Color(0xFFF7F7F7))
        .border(BorderStroke(1.dp, Color(0xFFE0E0E0)))
        .size(90.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center) {
        Icon(Icons.Default.MailOutline, contentDescription = "name", modifier = Modifier.size(40.dp))
        Text(text = "name",
            style = TextStyle(fontSize = 14.sp, fontWeight = FontWeight.Medium),
            modifier = Modifier.padding(top = 4.dp))
    }
}
//@Preview(showBackground = true, showSystemUi = true)
@Composable
fun CategoryItemtest() {
    Column(modifier = Modifier
        .padding(8.dp)
        .clip(RoundedCornerShape(12.dp))
        .background(Color(0xFFF7F7F7))
        .border(BorderStroke(1.dp, Color(0xFFE0E0E0)))
        .size(90.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center) {
        Icon(Icons.Default.MailOutline, contentDescription = "name", modifier = Modifier.size(40.dp))
        Text(text = "name",
            style = TextStyle(fontSize = 14.sp, fontWeight = FontWeight.Medium),
            modifier = Modifier.padding(top = 4.dp))
    }
}


@Preview(showBackground = true, showSystemUi = true)
@Composable
fun LatestProductsSection() {
    Column(modifier = Modifier.padding(16.dp)) {
        Text("All Products", style = TextStyle(fontSize = 18.sp, fontWeight = FontWeight.Bold)
        ,modifier = Modifier.padding(26.dp)
        )

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(400.dp)
        ) {
            LazyVerticalGrid(
                columns = GridCells.Fixed(2),
                modifier = Modifier.fillMaxSize(),
                horizontalArrangement = Arrangement.spacedBy(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                items(15) { index ->
                    ProductCard("Product ${index + 1}", "$${(index + 1) * 10}.00", "$${(index + 1) * 15}.00")
                }
            }
        }
    }
}


//@Preview(showBackground = true, showSystemUi = true)
@Composable
fun ProductCard(productName: String, currentPrice: String, originalPrice: String) {
    Box(
        modifier = Modifier
            .width(150.dp)
            .clip(RoundedCornerShape(12.dp))
            .background(Color.White)
            .padding(14.dp)
    ) {
        // Background image of the product
        Image(
            painter = painterResource(id = R.drawable.rectangle9),
            contentDescription = productName,
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
            Text(productName, style = TextStyle(fontSize = 14.sp, fontWeight = FontWeight.Bold))
            Text(currentPrice, color = Color.Green)
            Text(originalPrice, textDecoration = TextDecoration.LineThrough)
        }
    }
}



/*@Composable
fun ProductCard() {
    Box(
        modifier = Modifier
            .width(150.dp)
            .clip(RoundedCornerShape(12.dp))
            .background(Color.White)
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
            Text("currentPrice", color = Color.Green)
            Text("originalPrice", textDecoration = TextDecoration.LineThrough)
        }
    }
}*/

//@Preview(showBackground = true, showSystemUi = true)
@Composable
fun BottomNavigationBar() {
    BottomNavigation(
        backgroundColor = Color.White,
        contentColor = Color.Black,
        modifier = Modifier.padding(bottom = 8.dp)
    ) {
        BottomNavigationItem(
            icon = {
                Image(
                    painter = painterResource(id = R.drawable.home_2),
                    contentDescription = "Home",
                    modifier = Modifier.size(24.dp)
                )
            },
            label = { Text("Home", style = TextStyle(fontSize = 12.sp)) },
            selected = true,
            onClick = { /* Home Action */ }
        )
        BottomNavigationItem(
            icon = { Image(
                painter = painterResource(id = R.drawable.category_2),
                contentDescription = "categories",
                modifier = Modifier.size(24.dp)
            )
    },
            label = { Text("categories", style = TextStyle(fontSize = 11.5.sp)) },
            selected = true,
            onClick = { /* Home Action */ }
        )
        BottomNavigationItem(
            icon = { Image(
                painter = painterResource(id = R.drawable.shopping_cart),
                contentDescription = "Home",
                modifier = Modifier.size(24.dp)
            )
            },
            label = { Text("My Cart", style = TextStyle(fontSize = 12.sp)) },
            selected = false,
            onClick = { /* My Cart Action */ }
        )
        BottomNavigationItem(
            icon = { Image(
                painter = painterResource(id = R.drawable.heart),
                contentDescription = "Home",
                modifier = Modifier.size(24.dp)
            )
            },
            label = { Text("Wishlist", style = TextStyle(fontSize = 12.sp)) },
            selected = false,
            onClick = { /* Wishlist Action */ }
        )
        BottomNavigationItem(
            icon = { Image(
                painter = painterResource(id = R.drawable.profile),
                contentDescription = "Home",
                modifier = Modifier.size(24.dp)
            )
            },
            label = { Text("Profile", style = TextStyle(fontSize = 12.sp)) },
            selected = false,
            onClick = { /* Profile Action */ }
        )
    }
}
