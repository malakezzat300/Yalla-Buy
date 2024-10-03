package com.malakezzat.yallabuy.ui.home.view

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.animateScrollBy
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
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

@Preview(showBackground = true, showSystemUi = true)
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

@Composable
fun CategoriesSection() {
    Column(modifier = Modifier.padding(16.dp)) {
        Text("Categories", style = TextStyle(fontSize = 18.sp, fontWeight = FontWeight.Bold))
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
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Icon(icon, contentDescription = name, modifier = Modifier.size(40.dp))
        Text(name, style = TextStyle(fontSize = 12.sp))
    }
}


//@Preview(showBackground = true, showSystemUi = true)
@Composable
fun LatestProductsSection() {
    Column(modifier = Modifier.padding(16.dp)) {
        Text("Latest Products", style = TextStyle(fontSize = 18.sp, fontWeight = FontWeight.Bold))
        LazyRow(
            modifier = Modifier.padding(top = 8.dp),
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            items(5) {
                ProductCard("Nike air jordan retro", "$126.00", "$186.00")
                ProductCard("Classic black glasses", "$8.50", "$10.00")
            }
        }
    }
}

@Composable
fun ProductCard(productName: String, currentPrice: String, originalPrice: String) {
    Column(
        modifier = Modifier
            .width(150.dp)
            .clip(RoundedCornerShape(8.dp))
            .background(Color.LightGray)
            .padding(8.dp)
    ) {
        Image(
            painter = painterResource(id = R.drawable.ic_launcher_background), // استبدل بـ resource حقيقي
            contentDescription = productName,
            modifier = Modifier
                .fillMaxWidth()
                .height(100.dp),
            contentScale = ContentScale.Crop
        )
        Text(productName, style = TextStyle(fontSize = 14.sp, fontWeight = FontWeight.Bold))
        Text(currentPrice, color = Color.Green)
        Text(originalPrice, textDecoration = TextDecoration.LineThrough)
    }
}
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
