package com.malakezzat.yallabuy.ui.auth.view

import android.annotation.SuppressLint
import android.util.Log
import android.widget.Toast
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
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.google.firebase.auth.FirebaseAuth
import com.malakezzat.yallabuy.R
import com.malakezzat.yallabuy.data.firebase.FirebaseAuthun
import com.malakezzat.yallabuy.ui.Screen
import com.malakezzat.yallabuy.ui.auth.viewmodel.login.LogInViewModel
import com.malakezzat.yallabuy.ui.theme.AppColors

@SuppressLint("SuspiciousIndentation")
@Composable
fun LogInScreen(viewModel: LogInViewModel,
                 navController: NavController
) {
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var passwordVisibility by remember { mutableStateOf(false) }
    var isLoading by remember { mutableStateOf(false) }

    var auth = FirebaseAuthun()
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(10.dp)
            .background(Color.White),
       //horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        val context = LocalContext.current
        Column( horizontalAlignment = Alignment.Start,

            ) {

            Text(text = "Welcome Back!",
                fontSize = 35.sp,
                color = Color.Black,
                modifier = Modifier
                    .padding(8.dp)
            )

//            Spacer(modifier = Modifier.height(8.dp))
        }
        Spacer(modifier = Modifier.height(16.dp))
        // Email Input
        Text(text = "Email", fontSize = 18.sp, modifier = Modifier.padding(8.dp))
        OutlinedTextField(
            value = email,
            onValueChange = {input -> email = input },
            shape = RoundedCornerShape(30.dp),
            modifier = Modifier
                .fillMaxWidth()
                .height(75.dp)
                .background(color = Color.White)
                .padding(10.dp),
            colors =  OutlinedTextFieldDefaults.colors(unfocusedBorderColor = AppColors.MintGreen)
        )

        Text(text = "Password", fontSize = 18.sp, modifier = Modifier.padding(8.dp))
        // Password Input
        OutlinedTextField(
            value = password,
            onValueChange = { input -> password = input },
            shape = RoundedCornerShape(30.dp),
            modifier = Modifier
                .fillMaxWidth()
                .height(75.dp)
                .background(color = Color.White)
                .padding(10.dp),
            visualTransformation = if (passwordVisibility) VisualTransformation.None else PasswordVisualTransformation(),
            trailingIcon = {
                val image = if (passwordVisibility) {
                    //  Icons.Filled.Visibility

                } else {
                    // Icons.Filled.VisibilityOff
                }

                IconButton(onClick = { passwordVisibility = !passwordVisibility }) {
                    //Icon(imageVector = image, contentDescription = null)
                }
            },
            keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Password),
            colors =  OutlinedTextFieldDefaults.colors(unfocusedBorderColor = AppColors.MintGreen)


        )
        Spacer(modifier = Modifier.height(8.dp))
        // Create Account Button
        Button(
            onClick = {
                isLoading = true
                //  viewModel.signInWithEmailAndPassword(email,password,fullName)
                if(email.isEmpty() || password.isEmpty()){
                    isLoading = false
                    Toast.makeText(context,"complete empty fields please", Toast.LENGTH_LONG)
                }else{
                    isLoading=true
                        auth.logInWithEmailAndPassword(email,password, onSuccess = {
                                val user = FirebaseAuth.getInstance().currentUser
                                if (user != null) {
                                    // Reload the user to ensure the latest data
                                    user.reload().addOnCompleteListener { task ->
                                        if (task.isSuccessful) {
                                            if (user.isEmailVerified) {
                                                // Email is verified
                                                println("Email is verified")
                                                navController.navigate(Screen.HomeScreen.route)
                                            } else {
                                                isLoading=false
                                                Toast.makeText(context,"your email is not verified", Toast.LENGTH_LONG).show()
                                            }
                                        } else {
                                            // Handle reload error
                                            isLoading=false
                                            Toast.makeText(context,"login faild", Toast.LENGTH_LONG).show()
                                            println("Failed to reload user: ${task.exception?.message}")
                                        }
                                    }
                                } else {
                                    // Handle user not logged in
                                    println("User not logged in")
                                }


                            Log.i("TAG", "SignupScreen: password and confirm password are not the same")
                        }, onError = {
                            isLoading=false
                            Toast.makeText(context,"login faild", Toast.LENGTH_LONG).show()
                        })


                }

            },
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
            if (isLoading) {
                CircularProgressIndicator(color = Color.White, modifier = Modifier.size(24.dp))
            } else {
                Text(text = "Login", fontSize = 20.sp)
            }

        }
        Row(verticalAlignment = Alignment.CenterVertically) {
            Spacer(modifier = Modifier.width(10.dp))
            Text(text = "Don’t have an account?")
            TextButton(onClick = {navController.navigate(Screen.SignUpScreen.route) }) {
                Text(text = " Signup", color = AppColors.MintGreen)
            }
        }

        Spacer(modifier = Modifier.height(8.dp))
    }
}

@Preview
@Composable
fun LogInScreenPreview(
) {
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var passwordVisibility by remember { mutableStateOf(false) }
    val context = LocalContext.current
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(10.dp)
            .background(Color.White),
//        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Column( horizontalAlignment = Alignment.Start,

            ) {
            Text(text = "Login",
                fontSize = 35.sp,
                color = Color.Black,
                modifier = Modifier.padding(8.dp)
            )
            Row(verticalAlignment = Alignment.CenterVertically) {
                Spacer(modifier = Modifier.width(10.dp))
                Text(text = "Don’t have an account?")
                TextButton(onClick = {/*navController.navigate(Screen.SignUpScreen.route) */}) {
                    Text(text = " Signup", color = AppColors.MintGreen)
                }
            }
//            Spacer(modifier = Modifier.height(8.dp))
        }


        Spacer(modifier = Modifier.height(16.dp))
        Text(text = "Email", fontSize = 18.sp, modifier = Modifier.padding(8.dp))

        OutlinedTextField(
            value = email,
            onValueChange = {input -> email = input },
            //label = { Text(text = "Full Name") },
            shape = RoundedCornerShape(10.dp),
            modifier = Modifier
                .fillMaxWidth()
                .background(color = Color.White)
                .padding(10.dp),
            colors =  OutlinedTextFieldDefaults.colors(unfocusedBorderColor = colorResource(R.color.cyan))
        )


        Spacer(modifier = Modifier.height(8.dp))
        Text(text = "Password", fontSize = 18.sp, modifier = Modifier.padding(8.dp))
        // Password Input
        OutlinedTextField(
            value = password,
            onValueChange = { input -> password = input },
            shape = RoundedCornerShape(10.dp),
            modifier = Modifier
                .fillMaxWidth()
                .background(color = Color.White)
                .padding(10.dp),
            visualTransformation = if (passwordVisibility) VisualTransformation.None else PasswordVisualTransformation(),
            trailingIcon = {
                val image = if (passwordVisibility) {
                    //  Icons.Filled.Visibility
                } else {
                    // Icons.Filled.VisibilityOff
                }

                IconButton(onClick = { passwordVisibility = !passwordVisibility }) {
                    //Icon(imageVector = image, contentDescription = null)
                }
            },
            keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Password),
            colors =  OutlinedTextFieldDefaults.colors(unfocusedBorderColor = colorResource(R.color.cyan))


        )
        Spacer(modifier = Modifier.height(8.dp))
        // Create Account Button

        Button(
            onClick = {
                //  viewModel.signInWithEmailAndPassword(email,password,fullName)
                if(email.isEmpty() || password.isEmpty()){
                    Toast.makeText(context,"complete empty fields please", Toast.LENGTH_LONG)
                }else{

                  //  auth.signInWithEmailAndPassword(email,password,fullName)

                    Toast.makeText(context,"password and confirm password are not the same",
                        Toast.LENGTH_LONG)
                    Log.i("TAG", "SignupScreen: password and confirm password are not the same")

                }

            },
            shape = RoundedCornerShape(16.dp),
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
            Text(text = "Login", fontSize = 20.sp)
        }

        Spacer(modifier = Modifier.height(8.dp))
    }
}

