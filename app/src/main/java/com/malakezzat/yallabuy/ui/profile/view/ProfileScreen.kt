package com.malakezzat.yallabuy.ui.profile.view
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.ClickableText
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.malakezzat.yallabuy.R
import com.malakezzat.yallabuy.ui.profile.viewmodel.ProfileScreenViewModel

@Composable
fun ProfileScreen(viewModel: ProfileScreenViewModel, navController: NavController) {
    Scaffold{ paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.White) // White background for the rest of the screen
                .padding(paddingValues)
        ) {
            // User Info Section with the colored background
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color(0xFF00C4B4)) // Your specified color for upper part
                    .padding(16.dp),
            ) {
                UserInfoSection()
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Personal Information Section
            SectionWithPadding(title = "Personal Information") {
                ProfileItem(icon = R.drawable.ic_shipping_address, title = "Settings")
                //ProfileItem(icon = R.drawable.ic_payment_method, title = "Payment Method")
                ProfileItem(icon = R.drawable.ic_order_history, title = "Order History")
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Support & Information Section
            SectionWithPadding(title = "Support & Information") {
                ProfileItem(icon = R.drawable.ic_privacy_policy, title = "Privacy Policy")
                ProfileItem(icon = R.drawable.ic_terms_conditions, title = "Terms & Conditions")
                ProfileItem(icon = R.drawable.ic_faq, title = "FAQs")
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Account Management Section
            SectionWithPadding(title = "Account Management") {
                ProfileItem(icon = R.drawable.ic_change_password, title = "Change Password")
                //DarkThemeToggle() // Dark Theme Toggle inside the Account Management section
            }

            Spacer(modifier = Modifier.weight(1f))
        }
    }
}
@Composable
fun SectionWithPadding(title: String, content: @Composable ColumnScope.() -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp) // Padding for each section
    ) {
        SectionHeader(title)
        content()
    }
}
@Composable
fun UserInfoSection() {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .fillMaxWidth()
    ) {
        // Profile Picture
        Image(
            painter = painterResource(id = R.drawable.ic_user_placeholder),
            contentDescription = "User Profile Picture",
            modifier = Modifier
                .size(64.dp)
                .clip(CircleShape)
        )

        Spacer(modifier = Modifier.width(16.dp))

        // User Info (Name and Email)
        Column(
            modifier = Modifier.weight(1f) // Takes remaining space
        ) {
            Text(
                text = "Ahmed Raza", // User name from viewModel
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White
            )
            Text(
                text = "ahmedraza@gmail.com", // User email from viewModel
                fontSize = 16.sp,
                color = Color.White
            )
        }

        IconButton(
            onClick = {
                // Handle log out action
            }
        ) {
            Icon(
                painter = painterResource(id = R.drawable.ic_logout), // Replace with your logout icon
                contentDescription = "Log Out",
                tint = Color.White,
                modifier = Modifier.size(40.dp)
            )
        }
    }
}


@Composable
fun SectionHeader(title: String) {
    Text(
        text = title,
        fontSize = 18.sp,
        fontWeight = FontWeight.SemiBold,
        modifier = Modifier.padding(vertical = 8.dp)
    )
}

@Composable
fun ProfileItem(icon: Int, title: String) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 12.dp)
    ) {
        Icon(
            painter = painterResource(id = icon),
            contentDescription = title,
            tint = Color.Gray,
            modifier = Modifier.size(24.dp)
        )
        Spacer(modifier = Modifier.width(16.dp))
        ClickableText(
            text = AnnotatedString(title),
            onClick = { /* Navigate to the corresponding screen */ },
            style = LocalTextStyle.current.copy(fontSize = 16.sp)
        )
    }
}

//@Composable
//fun DarkThemeToggle() {
//    Row(
//        verticalAlignment = Alignment.CenterVertically,
//        modifier = Modifier.fillMaxWidth().padding(vertical = 12.dp)
//    ) {
//        Text(
//            text = "Dark Theme",
//            fontSize = 16.sp,
//            modifier = Modifier.weight(1f)
//        )
//        Switch(
//            checked = false, // This should be tied to a state in viewModel
//            onCheckedChange = { /* Handle dark theme toggle */ }
//        )
//    }
//}
