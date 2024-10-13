package com.malakezzat.yallabuy.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.malakezzat.yallabuy.R
import com.malakezzat.yallabuy.ui.theme.AppColors

@Composable
fun CustomTopBar(navController: NavController, title :String,backgroundColor: Color = AppColors.Teal,
                 onClick: () -> Unit = { navController.navigate(Screen.HomeScreen.route) }) {
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
            IconButton(onClick = onClick) {
                Image(
                    painter = painterResource(id = R.drawable.back_arrow),
                    contentDescription = "Search Icon",
                    modifier = Modifier.size(24.dp)
                )
            }
            Text(
                text = title,
                style = TextStyle(
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Normal
                ), color = backgroundColor,
                modifier = Modifier.padding(start = 4.dp)
            )
        }
    }
}