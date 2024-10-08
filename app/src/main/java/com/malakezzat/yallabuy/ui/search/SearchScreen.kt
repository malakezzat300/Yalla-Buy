package com.malakezzat.yallabuy.ui.search

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
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
import androidx.compose.material.icons.filled.List
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderColors
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldColors
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.malakezzat.yallabuy.model.Product
import com.malakezzat.yallabuy.ui.Screen

//@Preview(showSystemUi = true)
@Composable
fun SearchScreen(viewModel: SearchViewModel,
                 navController: NavController
) {
    val searchQuery by viewModel.searchQuery.collectAsState()
    val filteredProducts by viewModel.filteredProducts.collectAsState()
    var query by remember { mutableStateOf("") }
    var sliderPosition by remember { mutableStateOf(100.0f) }


    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(18.dp)
    ) {
        Spacer(modifier = Modifier.height(50.dp))
        // Search bar
        OutlinedTextField(
            value = query,
            onValueChange = {input->
                query=input
                    viewModel.onSearchQueryChanged(input,sliderPosition)
                            },
            placeholder = { Text("Search") },
            leadingIcon = { Icon(Icons.Default.Search, contentDescription = "Search Icon") },
            shape = RoundedCornerShape(10.dp),
            modifier = Modifier
                .fillMaxWidth()
                .background(color = Color.White)
                .padding(10.dp),
            colors =  OutlinedTextFieldDefaults.colors(unfocusedBorderColor = Color.Green)
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Price slider

        Text(text = "Max Price: ${sliderPosition.toInt()} EGP")
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
                thumbColor = Color.Green,
                activeTrackColor = Color.Green,
                inactiveTrackColor = Color.Gray
            )
        )

        Spacer(modifier = Modifier.height(16.dp))
        // Recent Searches List
        if(searchQuery.isEmpty()){

        }else{
            LazyVerticalGrid(
                columns = GridCells.Fixed(2),
                contentPadding = PaddingValues(8.dp)
            ) {
                items(filteredProducts) { searchItem ->
                    RecentSearchItem(searchItem,navController)
                }
            }
        }
         
    }
}
@Composable
fun RecentSearchItem(product: Product,navController: NavController) {

    Column(
        modifier = Modifier
            .padding(8.dp)
            .background(Color.White, shape = RoundedCornerShape(8.dp))
            .clickable { /* Handle click */ }
            .padding(16.dp)
    ) {
        // Placeholder for product image
        Box(modifier = Modifier
            .fillMaxWidth()
            .height(100.dp)
            .background(Color.White)
            .clickable { navController.navigate("${Screen.ProductInfScreen.route}/${product.id}") }
        ) {
            if(product.image != null){
                AsyncImage(
                    model = product.image.src,
                    contentDescription = product.title,
                    modifier = Modifier
                        .size(128.dp)
                        .clip(RoundedCornerShape(8.dp))
                        .background(Color.LightGray),
                    contentScale = ContentScale.Crop)
            }
          
        }

        Spacer(modifier = Modifier.height(8.dp))

        // Product name and price
        Text(text = product?.title?:"title", style = MaterialTheme.typography.bodyMedium)
        Text(text = product?.variants?.get(0)?.price?:"200", style = MaterialTheme.typography.bodyMedium)
    }
}
