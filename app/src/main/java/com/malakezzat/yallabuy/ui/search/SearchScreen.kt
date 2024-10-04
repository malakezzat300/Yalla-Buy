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
import androidx.compose.material3.Slider
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.malakezzat.yallabuy.model.Product

//@Preview(showSystemUi = true)
@Composable
fun SearchScreen(viewModel: SearchViewModel,
                 navController: NavController
) {
    val searchQuery by viewModel.searchQuery.collectAsState()
    val filteredProducts by viewModel.filteredProducts.collectAsState()
    var query by remember { mutableStateOf("") }


    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(18.dp)
    ) {
        Spacer(modifier = Modifier.height(50.dp))
        // Search bar
        TextField(
            value = query,
            onValueChange = {input->
                query=input
                    viewModel.onSearchQueryChanged(input)
                            },
            modifier = Modifier.fillMaxWidth(),
            placeholder = { Text("Search") },
            leadingIcon = { Icon(Icons.Default.Search, contentDescription = "Search Icon") },
            trailingIcon = { Icon(Icons.Default.List, contentDescription = "Filter Icon") }
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Price slider
        var sliderPosition by remember { mutableStateOf(0.0f) }
        Text(text = "Max Price: ${sliderPosition.toInt()} EGP")
        Slider(
            value = sliderPosition,
            onValueChange = { sliderPosition = it },
            valueRange = 0f..200f,
            modifier = Modifier.fillMaxWidth()
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
                    RecentSearchItem(searchItem)
                }
            }
        }
         
    }
}
@Composable
fun RecentSearchItem(product: Product?) {

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
            .background(Color.Gray)) {

        }

        Spacer(modifier = Modifier.height(8.dp))

        // Product name and price
        Text(text = product?.title?:"title", style = MaterialTheme.typography.bodyMedium)
        Text(text = product?.variants?.get(0)?.price?:"200", style = MaterialTheme.typography.bodyMedium)
    }
}
