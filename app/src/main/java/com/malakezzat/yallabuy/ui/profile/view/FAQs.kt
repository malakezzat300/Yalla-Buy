package com.malakezzat.yallabuy.ui.profile.view

import android.annotation.SuppressLint
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.malakezzat.yallabuy.R

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FAQScreen(navController: NavController) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("FAQs", fontSize = 24.sp, fontWeight = FontWeight.Bold) },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(
                            painter = painterResource(id = R.drawable.back_arrow),
                            contentDescription = "Back",
                            modifier = Modifier.size(20.dp)
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    titleContentColor = Color(0xFF00C4B4), // Text color
                    navigationIconContentColor = Color(0xFF00C4B4) // Icon color
                )
            )
        },
        content = { paddingValues ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
                    .padding(start = 16.dp)
                    .verticalScroll(rememberScrollState()) // Adding scroll functionality
            ) {

                Spacer(modifier = Modifier.height(16.dp))

                FAQSection(
                    question = "Can I cancel my order?",
                    answer = "Yes, only if the order is not dispatched yet. You can contact our customer service department to get your order canceled."
                )

                FAQSection(
                    question = "Will I receive the same product I see in the photo?",
                    answer = "Actual product color may vary from the images shown. Every monitor or mobile display has a different capability to display colors, and every individual may see these colors differently. In addition, lighting conditions at the time the photo was taken can also affect an image's color."
                )

                FAQSection(
                    question = "How can I recover the forgotten password?",
                    answer = "If you have forgotten your password, you can recover it from the 'Login - Forgotten your password?' section. You will receive an e-mail with a link to enter and confirm your new password."
                )

                FAQSection(
                    question = "Is my personal information confidential?",
                    answer = "Your personal information is confidential. We do not rent, sell, barter, or trade email addresses. When you place an order with us, we collect your name, address, telephone number, credit card information, and your email address. We use this information to fulfill your order and to communicate with you about your order. All your information is kept confidential and will not be disclosed to anybody unless ordered by government authorities."
                )

                FAQSection(
                    question = "What payment methods can I use to make purchases?",
                    answer = "We offer the following payment methods: PayPal, VISA, MasterCard, and Voucher code, if applicable."
                )

                Spacer(modifier = Modifier.height(16.dp))

                Text(
                    text = "For any query, you can visit our website for Help Center at Quickmart.com",
                    fontSize = 14.sp
                )
            }
        }
    )
}

@Composable
fun FAQSection(question: String, answer: String) {
    Column(modifier = Modifier.padding(bottom = 16.dp)) {
        Text(text = question, fontWeight = FontWeight.Bold, fontSize = 18.sp)
        Text(text = answer, fontSize = 16.sp, modifier = Modifier.padding(top = 4.dp))
        Spacer(modifier = Modifier.height(12.dp))
    }
}
