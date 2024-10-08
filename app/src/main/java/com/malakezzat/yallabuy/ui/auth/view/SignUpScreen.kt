package com.malakezzat.yallabuy.ui

import android.util.Log
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.DialogProperties
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.malakezzat.yallabuy.R
import com.malakezzat.yallabuy.data.firebase.FirebaseAuthun
import com.malakezzat.yallabuy.data.remote.ApiState
import com.malakezzat.yallabuy.model.Customer
import com.malakezzat.yallabuy.model.CustomerRequest
import com.malakezzat.yallabuy.model.CustomerSearchRespnse
import com.malakezzat.yallabuy.model.Customerr
import com.malakezzat.yallabuy.model.Customers
import com.malakezzat.yallabuy.ui.auth.viewmodel.SignUpViewModel
import com.malakezzat.yallabuy.ui.theme.AppColors

@Composable
fun SignupScreen(viewModel: SignUpViewModel, navController: NavController) {
    var fullName by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") }
    var passwordVisibility by remember { mutableStateOf(false) }
    var isLoading by remember { mutableStateOf(false) }
    var showDialog by remember { mutableStateOf(false) }

    // Firebase Auth and Google Sign-In setup
    val context = LocalContext.current
    val auth = FirebaseAuthun()
    val googleSignInClient = GoogleSignIn.getClient(context, auth.getGoogleSignInOptions(context))
    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.StartActivityForResult()
    ) { result ->
        // Handle Google Sign-In result
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
            .padding(16.dp),
        verticalArrangement = Arrangement.Center
    ) {
        // Header
        Text(
            text = "Create an account",
            fontSize = 28.sp,
            color = Color.Black,
            modifier = Modifier.padding(8.dp)
        )

        Spacer(modifier = Modifier.height(16.dp))
        // Full Name Input
        Text(text = "Full Name", fontSize = 16.sp, modifier = Modifier.padding(start = 8.dp))
        OutlinedTextField(
            value = fullName,
            onValueChange = { fullName = it },
            shape = RoundedCornerShape(30.dp),
            modifier = Modifier
                .fillMaxWidth()
                .height(75.dp)
                .padding(10.dp),
            colors = OutlinedTextFieldDefaults.colors(unfocusedBorderColor = AppColors.MintGreen)
        )
        Spacer(modifier = Modifier.height(8.dp))
        // Email Input
        Text(text = "Email", fontSize = 16.sp, modifier = Modifier.padding(start = 8.dp))
        OutlinedTextField(
            value = email,
            onValueChange = { email = it },
            shape = RoundedCornerShape(30.dp),
            modifier = Modifier
                .fillMaxWidth()
                .height(75.dp)
                .padding(10.dp),
            keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Email),
            colors = OutlinedTextFieldDefaults.colors(unfocusedBorderColor = AppColors.MintGreen)
        )

        Spacer(modifier = Modifier.height(8.dp))

        // Password Input
        Text(text = "Password", fontSize = 16.sp, modifier = Modifier.padding(start = 8.dp))
        OutlinedTextField(
            value = password,
            onValueChange = { password = it },
            shape = RoundedCornerShape(30.dp),
            modifier = Modifier
                .fillMaxWidth()
                .height(75.dp)
                .padding(10.dp),
            visualTransformation = if (passwordVisibility) VisualTransformation.None else PasswordVisualTransformation(),
            trailingIcon = {
                IconButton(onClick = { passwordVisibility = !passwordVisibility }) {
                    // Add visibility icons here
                }
            },
            keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Password),
            colors = OutlinedTextFieldDefaults.colors(unfocusedBorderColor = AppColors.MintGreen)
        )

        Spacer(modifier = Modifier.height(8.dp))

        // Confirm Password Input
        Text(text = "Confirm Password", fontSize = 16.sp, modifier = Modifier.padding(start = 8.dp))
        OutlinedTextField(
            value = confirmPassword,
            onValueChange = { confirmPassword = it },
            shape = RoundedCornerShape(30.dp),
            modifier = Modifier
                .fillMaxWidth()
                .height(75.dp)
                .padding(10.dp),
            visualTransformation = if (passwordVisibility) VisualTransformation.None else PasswordVisualTransformation(),
            trailingIcon = {
                IconButton(onClick = { passwordVisibility = !passwordVisibility }) {
                    // Add visibility icons here
                }
            },
            keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Password),
            colors = OutlinedTextFieldDefaults.colors(unfocusedBorderColor = AppColors.MintGreen)
        )

        Spacer(modifier = Modifier.height(14.dp))

        // Create Account Button
        Button(
            onClick = { /* Logic for account creation */ },
            shape = RoundedCornerShape(30.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = Color.Black,
                contentColor = Color.White
            ),
            modifier = Modifier
                .fillMaxWidth()
                .height(60.dp)
                .padding(10.dp)
        ) {
            if (isLoading) {
                CircularProgressIndicator(color = Color.White, modifier = Modifier.size(24.dp))
            } else {
                Text(text = "Create Account",
                    fontSize = 20.sp)
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        // Signup with Google text

        Text(
            text = "-OR Signup with Google-",
            fontSize = 16.sp,
            modifier = Modifier.align(Alignment.CenterHorizontally)
        )

        Spacer(modifier = Modifier.height(10.dp))

        // Google Icon
        TextButton(
            onClick = {
                val signInIntent = googleSignInClient.signInIntent
                launcher.launch(signInIntent)
            },
            modifier = Modifier.fillMaxWidth()
                .size(45.dp)
                .align(Alignment.CenterHorizontally)

        ) {
        Icon(
            painter = painterResource(id = R.drawable.google),
            contentDescription = null,
            tint = Color.Unspecified,
            //modifier = Modifier.size(35.dp).align(Alignment.CenterHorizontally)
        )}

        Spacer(modifier = Modifier.height(8.dp))

        // Row for login prompt
        Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.align(Alignment.CenterHorizontally)) {
            Spacer(modifier = Modifier.width(10.dp))
            Text(text = "Already have an account?")
            TextButton(onClick = { navController.navigate(Screen.LogInScreen.route) }) {
                Text(text = " Login", color = AppColors.MintGreen)
            }
        }

        Spacer(modifier = Modifier.height(14.dp))

        // AlertDialog for success message
        if (showDialog) {
            AlertDialog(
                onDismissRequest = { showDialog = false },
                confirmButton = {
                    TextButton(onClick = { showDialog = false; navController.navigate(Screen.LogInScreen.route) }) {
                        Text("OK", fontSize = 18.sp, fontWeight = FontWeight.Bold, color = AppColors.MintGreen)
                    }
                },
                title = { Text(text = "Success", fontSize = 24.sp, fontWeight = FontWeight.Bold) },
                text = { Text("A verification email has been sent to your email. Please verify and login.", fontSize = 15.sp) },
                icon = {
                    Icon(
                        imageVector = Icons.Filled.CheckCircle,
                        contentDescription = "Success Icon",
                        tint = Color.Cyan,
                        modifier = Modifier.size(40.dp)
                    )
                },
                properties = DialogProperties(dismissOnBackPress = true),
                shape = RectangleShape,
                containerColor = Color.White
            )
        }
    }
}


@Preview(showBackground = true, showSystemUi = true)
@Composable
fun SignupScreenPreview() {
    // State variables for user input
    var fullName by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") }
    var passwordVisibility by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
            .padding(16.dp),
        verticalArrangement = Arrangement.Center
    ) {
        // Header for the Signup screen
        Column(horizontalAlignment = Alignment.Start) {
            Text(
                text = "Create an account",
                fontSize = 28.sp,
                color = Color.Black,
                modifier = Modifier.padding(8.dp)
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Full Name Input
        Text(text = "Full Name", fontSize = 16.sp, modifier = Modifier.padding(start = 8.dp))
        OutlinedTextField(
            value = fullName,
            onValueChange = { input -> fullName = input },
            shape = RoundedCornerShape(30.dp),
            modifier = Modifier
                .fillMaxWidth() // Use fillMaxWidth for full width
                .height(60.dp) // Set a specific height for the TextField
                .padding(10.dp),
            colors = OutlinedTextFieldDefaults.colors(unfocusedBorderColor = AppColors.MintGreen)
        )

        Spacer(modifier = Modifier.height(8.dp))

        // Email Input
        Text(text = "Email", fontSize = 16.sp, modifier = Modifier.padding(start = 8.dp))
        OutlinedTextField(
            value = email,
            onValueChange = { input -> email = input },
            shape = RoundedCornerShape(30.dp),
            modifier = Modifier
                .fillMaxWidth() // Use fillMaxWidth for full width
                .height(60.dp) // Set a specific height for the TextField
                .padding(10.dp),
            keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Email),
            colors = OutlinedTextFieldDefaults.colors(unfocusedBorderColor = AppColors.MintGreen)
        )

        Spacer(modifier = Modifier.height(8.dp))

        // Password Input
        Text(text = "Password", fontSize = 16.sp, modifier = Modifier.padding(start = 8.dp))
        OutlinedTextField(
            value = password,
            onValueChange = { input -> password = input },
            shape = RoundedCornerShape(30.dp),
            modifier = Modifier
                .fillMaxWidth() // Use fillMaxWidth for full width
                .height(60.dp) // Set a specific height for the TextField
                .padding(10.dp),
            visualTransformation = if (passwordVisibility) VisualTransformation.None else PasswordVisualTransformation(),
            trailingIcon = {
                // Icon for toggling password visibility
                val image = if (passwordVisibility) {
                    // Icons.Filled.Visibility
                } else {
                    // Icons.Filled.VisibilityOff
                }
                IconButton(onClick = { passwordVisibility = !passwordVisibility }) {
                    // Icon(imageVector = image, contentDescription = null)
                }
            },
            keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Password),
            colors = OutlinedTextFieldDefaults.colors(unfocusedBorderColor = AppColors.MintGreen)
        )

        Spacer(modifier = Modifier.height(8.dp))

        // Confirm Password Input
        Text(text = "Confirm Password", fontSize = 16.sp, modifier = Modifier.padding(start = 8.dp))
        OutlinedTextField(
            value = confirmPassword,
            onValueChange = { input -> confirmPassword = input },
            shape = RoundedCornerShape(30.dp),
            modifier = Modifier
                .fillMaxWidth() // Use fillMaxWidth for full width
                .height(60.dp) // Set a specific height for the TextField
                .padding(10.dp),
            visualTransformation = if (passwordVisibility) VisualTransformation.None else PasswordVisualTransformation(),
            trailingIcon = {
                // Icon for toggling password visibility
                val image = if (passwordVisibility) {
                    // Icons.Filled.Visibility
                } else {
                    // Icons.Filled.VisibilityOff
                }
                IconButton(onClick = { passwordVisibility = !passwordVisibility }) {
                    // Icon(imageVector = image, contentDescription = null)
                }
            },
            keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Password),
            colors = OutlinedTextFieldDefaults.colors(unfocusedBorderColor = AppColors.MintGreen)
        )

        Spacer(modifier = Modifier.height(14.dp))

        // Create Account Button
        Button(
            onClick = { /* Handle account creation */ },
            shape = RoundedCornerShape(30.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = Color.Black,        // Default background color
                contentColor = Color.White,         // Text color
                disabledContainerColor = Color.Gray // Background color when disabled
            ),
            modifier = Modifier
                .fillMaxWidth()
                .padding(10.dp)
                .height(60.dp)
        ) {
            Text(text = "Create Account", fontSize = 20.sp)
        }

        Spacer(modifier = Modifier.height(8.dp))

        // Signup with Google text
        Text(
            text = "-OR Signup with Google-",
            fontSize = 16.sp,
            modifier = Modifier
                .align(Alignment.CenterHorizontally) // Center the text
                .padding(vertical = 8.dp)
        )
        Spacer(modifier = Modifier.height(10.dp))
        // Google Icon
        Icon(
            painter = painterResource(id = R.drawable.google),
            contentDescription = null,
            tint = Color.Unspecified,
            modifier = Modifier
                .size(35.dp)
                .align(Alignment.CenterHorizontally) // Center the icon
        )
        Spacer(modifier = Modifier.height(8.dp))
        // Row for login prompt
        Row(verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .align(Alignment.CenterHorizontally)) {
            Spacer(modifier = Modifier.width(10.dp))
            Text(text = "Already have an account?")
            TextButton(onClick = { /* Navigate to Login screen */ }) {
                Text(text = " Login", color = AppColors.MintGreen)
            }
        }

        Spacer(modifier = Modifier.height(14.dp))
    }
}

@Composable
fun customAlert(){
        var showDialog by remember { mutableStateOf(false) }


        // Dialog implementation
        if (showDialog) {
            AlertDialog(
                onDismissRequest = { showDialog = false }, // Action on dismiss
                confirmButton = {
                    TextButton(onClick = { showDialog = false }) {
                        Text("OK")
                    }
                },
                dismissButton = {
                    TextButton(onClick = { showDialog = false }) {
                        Text("Cancel")
                    }
                },
                title = {
                    Text(text = "Dialog Title")
                },
                text = {
                    Text("This is a simple dialog in Jetpack Compose.")
                }
            )
        }
}