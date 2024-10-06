package com.malakezzat.yallabuy.ui.home.view

import android.util.Log
import android.widget.Toast
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
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.itemsIndexed
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.BottomNavigation
import androidx.compose.material.BottomNavigationItem
import androidx.compose.material.Chip
import androidx.compose.material.ChipDefaults
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
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
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.currentBackStackEntryAsState
import coil.compose.rememberAsyncImagePainter
import com.malakezzat.yallabuy.R
import com.malakezzat.yallabuy.data.remote.ApiState
import com.malakezzat.yallabuy.model.CustomCollection
import com.malakezzat.yallabuy.model.Product
import com.malakezzat.yallabuy.ui.Screen
import com.malakezzat.yallabuy.ui.home.viewmodel.HomeScreenViewModel
import kotlinx.coroutines.delay

private val TAG = "HomeScreen"
@Composable
fun HomeScreen(
    viewModel: HomeScreenViewModel,
    navController: NavController
){
    val productState by viewModel.productList.collectAsStateWithLifecycle()
    val categoriesState by viewModel.categoriesList.collectAsStateWithLifecycle()
    val brandsState by viewModel.brandsList.collectAsStateWithLifecycle()
    val context = LocalContext.current
    LaunchedEffect(Unit) {
        Log.d(TAG, categoriesState.toString())
        viewModel.getAllProducts()
        viewModel.getAllCategories()
    }
    Scaffold(
        topBar = { CustomTopBar(navController) },
      //  bottomBar = { BottomNavigationBar(navController) }
    ) {
        Column(
            modifier = Modifier
                .padding(it)
                .verticalScroll(rememberScrollState())
                .background(color = Color.White)
        ){
            AdList(viewModel)

            when (brandsState) {
                is ApiState.Loading -> {
                    CircularProgressIndicator(modifier = Modifier.align(Alignment.CenterHorizontally))
                }
                is ApiState.Success -> {
                    val brands = (brandsState as ApiState.Success<List<String>>).data
                    BrandsChips(brands)
                }
                is ApiState.Error -> {
                    Text(text = "Error: ${(brandsState as ApiState.Error).message}", color = Color.Red)
                }
            }
            when (categoriesState) {
                is ApiState.Loading -> {
                    CircularProgressIndicator(modifier = Modifier.align(Alignment.CenterHorizontally))
                }
                is ApiState.Success -> {
                    val categories = (categoriesState as ApiState.Success<List<CustomCollection>>).data
                    CategoriesSection(categories)
                    Log.d(TAG, "$categoriesState")
                }
                is ApiState.Error -> {
                    Text(text = "Error: ${(categoriesState as ApiState.Error).message}", color = Color.Red)
                }
            }
            when (productState) {
                is ApiState.Loading -> {
                    CircularProgressIndicator(modifier = Modifier.align(Alignment.CenterHorizontally))
                }
                is ApiState.Success -> {
                    val products = (productState as ApiState.Success<List<Product>>).data
                    LatestProductsSection(products)
                }
                is ApiState.Error -> {
                    Text(text = "Error: ${(productState as ApiState.Error).message}", color = Color.Red)
                }
            }


        }
    }
}


@OptIn(ExperimentalMaterialApi::class)
@Composable
fun BrandsChips(brands: List<String>) {
    Column(modifier = Modifier.padding(16.dp)) {
        Text("Brands", style = TextStyle(fontSize = 20.sp, fontWeight = FontWeight.Bold))
        LazyRow(
            modifier = Modifier.padding(top = 8.dp)
        ) {
            itemsIndexed(brands) { index, brand ->
                var isSelected by remember { mutableStateOf(false) }

                Chip(
                    onClick = {
                        isSelected = !isSelected
                    },
                    modifier = Modifier.padding(4.dp),
                    colors = ChipDefaults.chipColors(
                        backgroundColor = if (isSelected) Color.LightGray else Color.Cyan
                    )
                ) {
                    Text(text = brand, style = TextStyle(fontSize = 18.sp, fontWeight = FontWeight.Medium)
                    , color = Color.White
                    )
                }
            }
        }
    }
}


//@Preview(showBackground = true, showSystemUi = true)
@Composable
fun CustomTopBar(navController: NavController) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 38.dp, start = 10.dp, end = 10.dp, bottom = 10.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Image(
                painter = painterResource(id = R.drawable.logo), // logo
                contentDescription = "Logo",
                modifier = Modifier.size(30.dp)
            )
            Text(
                text = "YallaBuy",
                style = TextStyle(
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold
                ),
                modifier = Modifier.padding(start = 4.dp)
            )
        }
        Row(verticalAlignment = Alignment.CenterVertically) {
            IconButton(onClick = {navController.navigate(Screen.SearchScreen.route)}) {
                Image(
                    painter = painterResource(id = R.drawable.search_normal),
                    contentDescription = "Search Icon",
                    modifier = Modifier.size(24.dp)
                )
            }
            Image(
                painter = painterResource(id = R.drawable.rectangle1),
                contentDescription = "User Profile",
                modifier = Modifier
                    .size(36.dp)
                    .clip(RoundedCornerShape(18.dp))
            )
        }
    }
}



@Composable
fun AdList(viewModel: HomeScreenViewModel) {
    val context = LocalContext.current
    val scrollState = rememberLazyListState()
    var scrollDirection by remember { mutableStateOf(1) }
    val priceRuleIds by remember { mutableStateOf(mutableSetOf<Long?>()) }
    val discountCodeIds by remember { mutableStateOf(mutableSetOf<String?>()) }
    var fetchDone by remember { mutableStateOf(true) }

    val priceRuleList by viewModel.priceRules.collectAsState()
    val discountCodeList by viewModel.discountCodes.collectAsState()

    if(fetchDone) {
        repeat(3){
            for (item in priceRuleList) {
                item.id?.let { viewModel.fetchDiscountCodes(it) }
                fetchDone = false
            }
        }
    }

    if(discountCodeList.isNotEmpty()){
        for(item in discountCodeList) {
            discountCodeIds.add(item.code)
        }
    }




    //val discountCode by viewModel.discountCodes.collectAsState()

    // Log list sizes for debugging





    LazyRow(state = scrollState) {
        // Static ads first
        item {
            AdCard(painterResource(id = R.drawable.ad1))
        }
        item {
            AdCard(painterResource(id = R.drawable.ad2))
        }
        item{
            if(discountCodeIds.isNotEmpty()){
                for(item in discountCodeIds) {
                    CouponsCard(item)
                }
            }
        }
//        items(filteredDiscountCodes.size) { index ->
//            Log.i("couponsTest", "LazyRow: discount code at index $index : ${filteredDiscountCodes[index]}")
//            CouponsCard(filteredDiscountCodes[index])
//        }
    }

//    // Fetch discount codes for each price rule
//    for (item in priceRuleList) {
//        Log.i("couponsTest", "LazyRow: item :${item.id}")
//        item.id?.let { viewModel.fetchDiscountCodes(it) }
//    }

    LaunchedEffect(Unit) {
        viewModel.fetchPriceRules()
        delay(2000)
        val needToScrollItems = 20 - scrollState.layoutInfo.visibleItemsInfo.size + 1
        val scrollWidth = needToScrollItems * context.resources.displayMetrics.widthPixels
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
fun CouponsCard(code: String?) {
    val discount = code?.takeLast(2)
    val painter = when(discount) {
        "10" -> painterResource(R.drawable.coupon10)
        "30" -> painterResource(R.drawable.coupon30)
        "50" -> painterResource(R.drawable.coupon50)
        else -> painterResource(R.drawable.ad1)
    }

    val clipboardManager = LocalClipboardManager.current
    val context = LocalContext.current

    Card(
        modifier = Modifier
            .padding(24.dp)
            .fillMaxWidth()
            .height(200.dp),
        shape = RoundedCornerShape(12.dp),
        elevation = CardDefaults.cardElevation(12.dp),
        onClick = {
            code?.let {
                clipboardManager.setText(AnnotatedString(it))
                Toast.makeText(context, "Code: $code copied to clipboard", Toast.LENGTH_SHORT).show()
            }
        }
    ) {
        Image(
            painter = painter,
            contentDescription = "Coupon",
        )
    }
}

//@Preview(showBackground = true, showSystemUi = true)
@Composable
fun CategoriesSection(categories: List<CustomCollection>) {
    //categories: List<CustomCollection>
    Log.d(TAG, "3. ${categories}")
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
            itemsIndexed(categories) { index,category->
                CategoryItem(category)}
        }
    }
}


//@Preview(showBackground = true, showSystemUi = true)
@Composable
fun CategoryItem(category: CustomCollection) {
    Log.d(TAG, "4. ${category}")
    Column(modifier = Modifier
        .padding(15.dp)
        .clip(RoundedCornerShape(8.dp))
        .border(BorderStroke(5.dp, Color(0xFFE0E0E0)))
        .background(color = Color.White)
        .size(100.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center) {
        Image(
            painter = rememberAsyncImagePainter(category.image?.src),
            contentDescription = "category item",
            modifier = Modifier.size(40.dp)
        )
        Text(text = category.title,
            style = TextStyle(fontSize = 14.sp, fontWeight = FontWeight.Medium),
            modifier = Modifier.padding(top = 4.dp))
    }
}
//@Preview(showBackground = true, showSystemUi = true)
@Composable
fun LatestProductsSection(products: List<Product>) {
    //
    Column(modifier = Modifier.padding(16.dp)) {
        Row (
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ){
            Text("All Products", style = TextStyle(fontSize = 18.sp, fontWeight = FontWeight.Bold)
                ,modifier = Modifier.padding(26.dp))
            Text("SEE ALL", style = TextStyle(fontSize = 14.sp, fontWeight = FontWeight.Medium),
                color = Color.Cyan
            )
        }

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
                itemsIndexed(products) { _,product ->
                    ProductCard(product = product)
                }
            }
        }
    }
}



@Composable
fun ProductCard(product: Product) {
    Box(
        modifier = Modifier
            .width(150.dp)
            .clip(RoundedCornerShape(12.dp))
            .background(Color(0xFFF7F7F7))
            .padding(14.dp)
    ) {
        // Background image of the product
        product.images.firstOrNull()?.let { image ->
            Image(
                painter = rememberAsyncImagePainter(image.src),
                contentDescription = "Product Image",
                modifier = Modifier
                    .fillMaxWidth()
                    .height(100.dp),
                contentScale = ContentScale.Crop
            )
        }

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
            Text(product.title, style = TextStyle(fontSize = 14.sp, fontWeight = FontWeight.Bold))
            Text(product.vendor, color = Color.Green)
            val price = product.variants.first().price
            Text("Price: $${price}")
        }
    }
}


@Preview(showBackground = true, showSystemUi = true)
@Composable
fun ProductCard() {
    Box(
        modifier = Modifier
            .width(150.dp)
            .clip(RoundedCornerShape(12.dp))
            .background(Color(0xFFE0E0E0))
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
}
//@Preview(showBackground = true, showSystemUi = true)
@Composable
fun BottomNavigationBar(navController: NavController) {
    val navBackStackEntry = navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry.value?.destination?.route

    BottomNavigation(
        backgroundColor = Color.White,
        contentColor = Color.Black,
        modifier = Modifier.navigationBarsPadding()
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
            selected = currentRoute == Screen.HomeScreen.route,
            onClick = {
                if (currentRoute != Screen.HomeScreen.route) {
                    navController.popBackStack(Screen.HomeScreen.route, inclusive = false)
                    navController.navigate(Screen.HomeScreen.route) {
                        launchSingleTop = true
                    }
                }
            }
        )
        BottomNavigationItem(
            icon = {
                Image(
                    painter = painterResource(id = R.drawable.category_2),
                    contentDescription = "Categories",
                    modifier = Modifier.size(24.dp)
                )
            },
            label = { Text("Categories", style = TextStyle(fontSize = 11.5.sp)) },
            selected =false /*currentRoute == Screen.CategoriesScreen.route*/,
            onClick = {
                /*if (currentRoute != Screen.CategoriesScreen.route) {
                    navController.popBackStack(Screen.CategoriesScreen.route, inclusive = false)
                    navController.navigate(Screen.CategoriesScreen.route) {
                        launchSingleTop = true
                    }
                }*/
            }
        )
        BottomNavigationItem(
            icon = {
                Image(
                    painter = painterResource(id = R.drawable.shopping_cart),
                    contentDescription = "My Cart",
                    modifier = Modifier.size(24.dp)
                )
            },
            label = { Text("My Cart", style = TextStyle(fontSize = 12.sp)) },
            selected = currentRoute == Screen.ShoppingScreen.route,
            onClick = {
                if (currentRoute != Screen.ShoppingScreen.route) {
                    navController.popBackStack(Screen.ShoppingScreen.route, inclusive = false)
                    navController.navigate(Screen.ShoppingScreen.route) {
                        launchSingleTop = true
                    }
                }
            }
        )
        BottomNavigationItem(
            icon = {
                Image(
                    painter = painterResource(id = R.drawable.heart),
                    contentDescription = "Wishlist",
                    modifier = Modifier.size(24.dp)
                )
            },
            label = { Text("Wishlist", style = TextStyle(fontSize = 12.sp)) },
            selected =false /*currentRoute == Screen.WishlistScreen.route*/,
            onClick = {
                /*if (currentRoute != Screen.WishlistScreen.route) {
                    navController.popBackStack(Screen.WishlistScreen.route, inclusive = false)
                    navController.navigate(Screen.WishlistScreen.route) {
                        launchSingleTop = true
                    }
                }*/
            }
        )
        BottomNavigationItem(
            icon = {
                Image(
                    painter = painterResource(id = R.drawable.profile),
                    contentDescription = "Profile",
                    modifier = Modifier.size(24.dp)
                )
            },
            label = { Text("Profile", style = TextStyle(fontSize = 12.sp)) },
            selected =false /*currentRoute == Screen.ProfileScreen.route*/,
            onClick = {
                /*if (currentRoute != Screen.ProfileScreen.route) {
                    navController.popBackStack(Screen.ProfileScreen.route, inclusive = false)
                    navController.navigate(Screen.ProfileScreen.route) {
                        launchSingleTop = true
                    }
                }*/
            }
        )
    }
}
