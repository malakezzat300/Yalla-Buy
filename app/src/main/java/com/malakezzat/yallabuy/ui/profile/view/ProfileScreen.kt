package com.malakezzat.yallabuy.ui.profile.view
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.ClickableText
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import coil.compose.rememberAsyncImagePainter
import coil.request.ImageRequest
import com.google.firebase.auth.FirebaseAuth
import com.malakezzat.yallabuy.R
import com.malakezzat.yallabuy.data.firebase.FirebaseAuthun
import com.malakezzat.yallabuy.ui.Screen
import com.malakezzat.yallabuy.ui.profile.viewmodel.ProfileScreenViewModel
import com.malakezzat.yallabuy.ui.theme.AppColors

@Composable
fun ProfileScreen(viewModel: ProfileScreenViewModel, navController: NavController) {
    var showLogoutDialog by remember { mutableStateOf(false) }
    var showLoginDialog by remember { mutableStateOf(false) }
    var showResetPassDialog by remember { mutableStateOf(false) }
    var context = LocalContext.current
    Scaffold { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Color(0xFF00C4B4)) // Your specified color for upper part
                .padding(top = paddingValues.calculateTopPadding())
        ) {
            // User Info Section with the colored background
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color(0xFF00C4B4)) // Your specified color for upper part
                    .padding(top=20.dp ,start = 16.dp, bottom = 12.dp, end = 16.dp)
            ) {
                if(FirebaseAuth.getInstance().currentUser?.isAnonymous==true){
                    UserInfoSection(navController = navController, onLogoutClick = { showLoginDialog = true })
                }else{
                    UserInfoSection(navController = navController, onLogoutClick = { showLogoutDialog = true })                }
            }


            // Personal Information Section
            Surface (modifier = Modifier
                .fillMaxHeight()
                .clip(RoundedCornerShape(topStart = 30.dp, topEnd = 30.dp))
                .border(
                    1.dp,
                    shape = RoundedCornerShape(topStart = 30.dp, topEnd = 30.dp),
                    color = AppColors.White
                ))
            {
                Column(modifier = Modifier.padding(start = 8.dp, top = 20.dp)) {
                    SectionWithPadding(title = "Personal Information") {
                        ProfileItem(icon = R.drawable.ic_shipping_address, title = "Settings") {
                            navController.navigate(
                                Screen.SettingsScreen.route
                            )
                        }
                        //ProfileItem(icon = R.drawable.ic_payment_method, title = "Payment Method")
                        ProfileItem(icon = R.drawable.ic_order_history, title = "Order History") {
                            navController.navigate(
                                Screen.OrdersScreen.route
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    // Support & Information Section
                    SectionWithPadding(title = "Support & Information") {
                        ProfileItem(icon = R.drawable.ic_privacy_policy, title = "Privacy Policy") {
                            navController.navigate(
                                Screen.PrivacyPolicy.route
                            )
                        }
                        ProfileItem(icon = R.drawable.ic_terms_conditions, title = "Terms & Conditions") {
                            navController.navigate(
                                Screen.TermsConditions.route
                            )
                        }
                        ProfileItem(icon = R.drawable.ic_faq, title = "FAQs") {
                            navController.navigate(
                                Screen.Faqs.route
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    // Account Management Section
                    SectionWithPadding(title = "Account Management") {
                        ProfileItem(icon = R.drawable.ic_change_password, title = "Reset Password") {
                            showResetPassDialog=true
                        }
                        //DarkThemeToggle() // Dark Theme Toggle inside the Account Management section
                    }
                    if (showResetPassDialog) {
                        AlertDialog(
                            containerColor = Color.White,
                            onDismissRequest = { showResetPassDialog = false }, // Close dialog on dismiss
                            title = { Text(text = "Reset Password", color = AppColors.Teal) },
                            text = { Text("Are you sure you want to reset your password?") },
                            confirmButton = {
                                TextButton(
                                    onClick = {
                                        showResetPassDialog = false
                                        FirebaseAuthun().sendPasswordResetEmail(FirebaseAuth.getInstance().currentUser?.email.toString(), onSuccess = {
                                            Toast.makeText(context,"Password reset email sent successfully",Toast.LENGTH_LONG).show()
                                        }, onError = {})
                                        // Close the dialog after confirming
                                    },
                                    colors = ButtonDefaults.buttonColors(containerColor = AppColors.Teal)
                                ) {
                                    Text("Yes", color = Color.White)
                                }
                            },
                            dismissButton = {
                                TextButton(
                                    onClick = { showResetPassDialog = false },
                                    colors = ButtonDefaults.buttonColors(containerColor = AppColors.Teal) // Close dialog without action
                                ) {
                                    Text("No", color = Color.White)
                                }
                            }
                        )
                    }
                }
            }

            //Spacer(modifier = Modifier.weight(1f))
        }
    }
    // Log Out Confirmation Dialog
    if (showLogoutDialog) {
        AlertDialog(
            onDismissRequest = { showLogoutDialog = false }, // Close dialog on dismiss
            title = { Text(text = "Confirm Logout") },
            text = { Text("Are you sure you want to log out?") },
            confirmButton = {
                TextButton(
                    onClick = {
                        FirebaseAuth.getInstance().signOut()
                        navController.navigate(Screen.LogInScreen.route) {
                            popUpTo(navController.graph.startDestinationId) {
                                inclusive = true // Remove all previous screens from the back stack
                            }
                        }
                        showLogoutDialog = false // Close the dialog after confirming
                    }
                ) {
                    Text("Yes", color = Color.Red)
                }
            },
            dismissButton = {
                TextButton(
                    onClick = { showLogoutDialog = false } // Close dialog without action
                ) {
                    Text("No", color = Color(0xFF00C4B4))
                }
            }
        )
    }

    if (showLoginDialog) {
       navController.navigate(Screen.LogInScreen.route)
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
fun UserInfoSection(navController: NavController, onLogoutClick: () -> Unit) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .fillMaxWidth()
    ) {
        // Profile Picture
        Image(
            painter = rememberAsyncImagePainter(
                model = ImageRequest.Builder(LocalContext.current)
                    .data(FirebaseAuth.getInstance().currentUser?.photoUrl)
                    .crossfade(true)
                    .placeholder(R.drawable.person)
                    .error(R.drawable.person)
                    .build()
            ),
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
            if(FirebaseAuth.getInstance().currentUser?.isAnonymous == true){
                Text(
                    text = "Hello", // User name from viewModel
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )
            }else {
                Text(
                    text = FirebaseAuth.getInstance().currentUser?.displayName.toString(), // User name from viewModel
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )
                Text(
                    text = FirebaseAuth.getInstance().currentUser?.email.toString(), // User email from viewModel
                    fontSize = 16.sp,
                    color = Color.White
                )
            }
        }

        if(FirebaseAuth.getInstance().currentUser?.isAnonymous==true){
            IconButton(onClick = { navController.navigate(Screen.LogInScreen.route) } ) {
                Icon(
                    painter = painterResource(id = R.drawable.login),
                    contentDescription = "Log Out",
                    tint = Color.White,
                    modifier = Modifier.size(40.dp)
                ) }
        }else{
            IconButton(onClick = onLogoutClick ) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_logout),
                    contentDescription = "Log Out",
                    tint = Color.White,
                    modifier = Modifier.size(40.dp)
                ) }
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
fun ProfileItem(icon: Int, title: String, onClick: () -> Unit) {
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
            onClick = { onClick() },
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
