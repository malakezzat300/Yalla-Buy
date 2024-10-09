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
fun TermsScreen(navController: NavController) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Terms and Conditions", fontSize = 24.sp, fontWeight = FontWeight.Bold) },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(painter = painterResource(id = R.drawable.back_arrow), contentDescription = "Back" , modifier = Modifier.size(20.dp))
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

                PrivacySection(
                    title = "Terms and Conditions",
                    content = "Welcome to QuickMart! These Terms and Conditions (\"Terms\") govern your use of our e-commerce app. By accessing or using QuickMart, you agree to be bound by these Terms. Please read them carefully before proceeding."
                )

                PrivacySection(
                    title = "1. Account Registration:",
                    content = "- You must create an account to use certain features of QuickMart.\n" +
                            "- You are responsible for providing accurate and up-to-date information during the registration process.\n" +
                            "- You must safeguard your account credentials and notify us immediately of any unauthorized access or use of your account."
                )

                PrivacySection(
                    title = "2. Product Information and Pricing:",
                    content = "- QuickMart strives to provide accurate product descriptions, images, and pricing information.\n" +
                            "- We reserve the right to modify product details and prices without prior notice.\n" +
                            "- In the event of an error, we may cancel or refuse orders placed for incorrectly priced products."
                )

                PrivacySection(
                    title = "3. Order Placement and Fulfillment:",
                    content = "- By placing an order on QuickMart, you agree to purchase the selected products at the stated price.\n" +
                            "- We reserve the right to accept or reject any order, and we may cancel orders due to product unavailability, pricing errors, or suspected fraudulent activity.\n" +
                            "- Once an order is confirmed, we will make reasonable efforts to fulfill and deliver it in a timely manner."
                )

                PrivacySection(
                    title = "4. Payment:",
                    content = "- QuickMart supports various payment methods, including credit/debit cards and online payment platforms.\n" +
                            "- By providing payment information, you represent and warrant that you are authorized to use the chosen payment method.\n" +
                            "- All payments are subject to verification and approval by relevant financial institutions."
                )

                PrivacySection(
                    title = "5. Shipping and Delivery:",
                    content = "- QuickMart will make reasonable efforts to ensure timely delivery of products.\n" +
                            "- Shipping times may vary based on factors beyond our control, such as location, weather conditions, or carrier delays.\n" +
                            "- Risk of loss or damage to products passes to you upon delivery."
                )

                PrivacySection(
                    title = "6. Returns and Refunds:",
                    content = "- QuickMart's return and refund policies are outlined separately and govern the process for returning products and seeking refunds.\n" +
                            "- Certain products may be non-returnable or subject to specific conditions."
                )

                PrivacySection(
                    title = "7. Intellectual Property:",
                    content = "- QuickMart and its content, including logos, trademarks, text, images, and software, are protected by intellectual property rights.\n" +
                            "- You may not use, reproduce, modify, distribute, or display any part of QuickMart without our prior written consent."
                )

                PrivacySection(
                    title = "8. User Conduct:",
                    content = "- You agree to use QuickMart in compliance with applicable laws and regulations.\n" +
                            "- You will not engage in any activity that disrupts or interferes with the functioning of QuickMart or infringes upon the rights of others.\n" +
                            "- Any unauthorized use or attempt to access restricted areas or user accounts is strictly prohibited."
                )

                PrivacySection(
                    title = "9. Limitation of Liability:",
                    content = "- QuickMart and its affiliates shall not be liable for any direct, indirect, incidental, consequential, or punitive damages arising from the use or inability to use our app or any products purchased through it.\n" +
                            "- We do not guarantee the accuracy, completeness, or reliability of information provided on QuickMart."
                )

                PrivacySection(
                    title = "10. Governing Law:",
                    content = "- These Terms shall be governed by and construed in accordance with the laws of [Jurisdiction].\n" +
                            "- Any disputes arising out of or relating to these Terms shall be resolved in the courts of [Jurisdiction]."
                )

                Spacer(modifier = Modifier.height(16.dp))

                Text(
                    text = "If you have any questions or concerns about our Privacy Policy, please contact our customer support. By using QuickMart, you acknowledge that you have read and understood this Privacy Policy and agree to its terms and conditions.",
                    fontSize = 14.sp
                )
            }
        }
    )
}
