package com.malakezzat.yallabuy.ui.profile.view

import android.annotation.SuppressLint
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
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
fun PrivacyPolicyScreen(navController: NavController) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Privacy Policy", fontSize = 24.sp, fontWeight = FontWeight.Bold) },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(painter = painterResource(id = R.drawable.back_arrow), contentDescription = "Back" , modifier = Modifier.size(20.dp))
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    titleContentColor = Color(0xFF00C4B4), // Text color
                    navigationIconContentColor =Color(0xFF00C4B4) // Icon color
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
                    title = "Our Policy",
                    content = "At QuickMart, we are committed to protecting the privacy and security of our users' personal information. This Privacy Policy outlines how we collect, use, disclose, and safeguard the information obtained through our e-commerce app. By using QuickMart, you consent to the practices described in this policy."
                )

                PrivacySection(
                    title = "1. Information Collection",
                    content = "- Personal Information: We may collect personal information such as name, address, email, and phone number when you create an account, make a purchase, or interact with our services.\n" +
                            "- Transaction Details: We collect information related to your purchases, including order history, payment method, and shipping details.\n" +
                            "- Usage Data: We may collect data on how you interact with our app, such as browsing activity, search queries, and preferences."
                )

                PrivacySection(
                    title = "2. Information Use",
                    content = "- Provide Services: We use the collected information to process orders, deliver products, and provide customer support.\n" +
                            "- Personalization: We may use your information to personalize your shopping experience, recommend products, and display targeted advertisements.\n" +
                            "- Communication: We may use your contact information to send important updates, promotional offers, and newsletters. You can opt-out of these communications at any time."
                )

                PrivacySection(
                    title = "3. Information Sharing",
                    content = "- Third-Party Service Providers: We may share your information with trusted third-party service providers who assist us in operating our app, fulfilling orders, and improving our services.\n" +
                            "- Legal Compliance: We may disclose personal information if required by law or in response to a valid legal request from authorities."
                )

                PrivacySection(
                    title = "4. Data Security",
                    content = "- We implement appropriate security measures to protect your information from unauthorized access, alteration, disclosure, or destruction.\n" +
                            "- However, please note that no data transmission over the internet or electronic storage is 100% secure. We cannot guarantee absolute security of your information."
                )

                PrivacySection(
                    title = "5. User Rights",
                    content = "- Access and Update: You have the right to access, correct, or update your personal information stored in our app.\n" +
                            "- Data Retention: We retain your personal information as long as necessary to provide our services and comply with legal obligations."
                )

                PrivacySection(
                    title = "6. Children's Privacy",
                    content = "- QuickMart is not intended for children under the age of 13. We do not knowingly collect or solicit personal information from children."
                )

                PrivacySection(
                    title = "7. Updates to the Privacy Policy",
                    content = "- We reserve the right to update this Privacy Policy from time to time. Any changes will be posted on our app, and the revised policy will be effective upon posting."
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

@Composable
fun PrivacySection(title: String, content: String) {
    Text(
        text = title,
        fontSize = 20.sp,
        fontWeight = FontWeight.Bold,
        modifier = Modifier.padding(vertical = 8.dp)
    )
    Text(
        text = content,
        fontSize = 16.sp,
        modifier = Modifier.padding(bottom = 16.dp)
    )
}
