package com.malakezzat.yallabuy.ui

import android.content.Context
import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Label
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Shapes
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldColors
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.FirebaseAuthUserCollisionException
import com.google.firebase.auth.FirebaseAuthWeakPasswordException
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.auth.UserProfileChangeRequest
import com.malakezzat.yallabuy.R
import com.malakezzat.yallabuy.data.firebase.FirebaseAuthun
import com.malakezzat.yallabuy.ui.auth.viewmodel.SignUpViewModel
import com.malakezzat.yallabuy.ui.home.viewmodel.HomeScreenViewModel

@Composable
fun CreateAccountScreen(context : Context) {
    var userName by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var pass by remember { mutableStateOf("") }
    var isLoading by remember { mutableStateOf(false) }
    var errorMessage by remember { mutableStateOf<String?>(null) }
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Create Account",
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.Bold
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = "Start learning with create your account!",
            style = MaterialTheme.typography.bodyMedium,
            color = Color.Gray
        )
        Spacer(modifier = Modifier.height(24.dp))

        OutlinedTextField(
            value = userName,
            onValueChange = {input -> userName = input},
            label = { Text("Create your username") },
            leadingIcon = { Icon(Icons.Default.Person, contentDescription = null) },
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(
            value = email,
            onValueChange = {input -> email = input},
            label = { Text("Enter your email or phone number") },
            leadingIcon = { Icon(Icons.Default.Email, contentDescription = null) },
            modifier = Modifier.fillMaxWidth()

        )
        Spacer(modifier = Modifier.height(16.dp))

        // Password TextField
        OutlinedTextField(
            value = pass,
            onValueChange = {input -> pass = input},
            label = { Text("Create your password") },
            leadingIcon = { Icon(Icons.Default.Lock, contentDescription = null) },
           // trailingIcon = { Icon(Icons.Default.Lock, contentDescription = null) },
            modifier = Modifier.fillMaxWidth(),
            visualTransformation = PasswordVisualTransformation()
        )
        Spacer(modifier = Modifier.height(24.dp))

        Button(
            onClick = {
                isLoading = true

            },
            modifier = Modifier
                .fillMaxWidth()
                .height(50.dp),
            shape = RoundedCornerShape(8.dp),
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF5C4CE3))
        ) {
            if (isLoading) {
                CircularProgressIndicator(color = Color.White, modifier = Modifier.size(24.dp))
            } else {
                Text(text = "Create Account", color = Color.White)
            }
        }
        errorMessage?.let {
            Spacer(modifier = Modifier.height(16.dp))
            Text(text = it, color = Color.Red)
        }
        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = "Or using other method",
            color = Color.Gray,
            style = MaterialTheme.typography.bodyMedium
        )
        Spacer(modifier = Modifier.height(16.dp))

        OutlinedButton(
            onClick = { /* Handle Google Sign Up */ },
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(8.dp)
        ) {
            Icon(
                painter = painterResource(id = R.drawable.google), // replace with your Google icon resource
                contentDescription = null,
                tint = Color.Unspecified,
                modifier = Modifier.size(25.dp)
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(text = "Sign Up with Google")
        }

        Spacer(modifier = Modifier.height(8.dp))

        OutlinedButton(
            onClick = { /* Handle Facebook Sign Up */ },
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(8.dp)
        ) {
            Icon(
                painter = painterResource(id = R.drawable.facebook), // replace with your Facebook icon resource
                contentDescription = null,
                tint = Color.Unspecified,
                modifier = Modifier.size(25.dp)

            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(text = "Sign Up with Facebook")
        }
    }
}


@Composable
fun SignupScreen(viewModel: SignUpViewModel,
                 navController: NavController) {
    var fullName by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") }
    var passwordVisibility by remember { mutableStateOf(false) }
    var auth = FirebaseAuthun()
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
            Text(text = "Signup",
                fontSize = 30.sp,
                color = Color.Black,
                modifier = Modifier.padding(8.dp)
            )
            Row(verticalAlignment = Alignment.CenterVertically) {
                Spacer(modifier = Modifier.width(10.dp))
                Text(text = "Already have an account?")
                TextButton(onClick = {navController.navigate(Screen.LogInScreen.route) }) {
                    Text(text = " Login", color = Color.Cyan)
                }
            }
//            Spacer(modifier = Modifier.height(8.dp))
        }


        Spacer(modifier = Modifier.height(16.dp))
        Text(text = "Full Name", fontSize = 18.sp, modifier = Modifier.padding(8.dp))

        OutlinedTextField(
            value = fullName,
            onValueChange = {input -> fullName = input },
            //label = { Text(text = "Full Name") },
            shape = RoundedCornerShape(10.dp),
            modifier = Modifier.fillMaxWidth()
                .background(color = Color.White)
                .padding(10.dp),
            colors =  OutlinedTextFieldDefaults.colors(unfocusedBorderColor = Color.Cyan)
        )


        Spacer(modifier = Modifier.height(8.dp))
        Text(text = "Email", fontSize = 18.sp, modifier = Modifier.padding(8.dp))
        // Email Input
        OutlinedTextField(
            value = email,
            onValueChange = { input -> email=input },
            shape = RoundedCornerShape(10.dp),
            modifier = Modifier.fillMaxWidth()
                .background(color = Color.White)
                .padding(10.dp),
            keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Email),
            colors =  OutlinedTextFieldDefaults.colors(unfocusedBorderColor = Color.Cyan)

        )

        Spacer(modifier = Modifier.height(8.dp))
        Text(text = "Password", fontSize = 18.sp, modifier = Modifier.padding(8.dp))
        // Password Input
        OutlinedTextField(
            value = password,
            onValueChange = { input -> password = input },
            shape = RoundedCornerShape(10.dp),
            modifier = Modifier.fillMaxWidth()
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
            colors =  OutlinedTextFieldDefaults.colors(unfocusedBorderColor = Color.Cyan)


        )

        Spacer(modifier = Modifier.height(8.dp))
        Text(text = "Confirm Password", fontSize = 18.sp, modifier = Modifier.padding(8.dp))
        // Password Input
        OutlinedTextField(
            value = confirmPassword,
            onValueChange = { input -> confirmPassword = input },
            shape = RoundedCornerShape(10.dp),
            modifier = Modifier.fillMaxWidth()
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
            colors =  OutlinedTextFieldDefaults.colors(unfocusedBorderColor = Color.Cyan)


        )

        // Create Account Button
        Button(
            onClick = {
              //  viewModel.signInWithEmailAndPassword(email,password,fullName)
                if(email.isEmpty() || password.isEmpty() || confirmPassword.isEmpty() || email.isEmpty()){
                    Toast.makeText(context,"complete empty fields please",Toast.LENGTH_LONG)
                }else{
                    if(password == confirmPassword){
                        auth.signInWithEmailAndPassword(email,password,fullName)
                    }else{
                        Toast.makeText(context,"password and confirm password are not the same",Toast.LENGTH_LONG)
                    }
                }
                
            },
            shape = RoundedCornerShape(16.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = Color.Black,        // Default background color
                contentColor = Color.White,         // Text color
                disabledContainerColor = Color.Gray // Background color when disabled
            ),
            modifier = Modifier.fillMaxWidth()
                .padding(10.dp)
                .height(60.dp)

        ) {
            Text(text = "Create Account")
        }

        Spacer(modifier = Modifier.height(8.dp))

        // Signup with Google
        TextButton(
            onClick = { /* Handle Google Signup */ },
            modifier = Modifier.fillMaxWidth()

        ) {
            Text(text = "Signup with Google")
            Icon(
                painter = painterResource(id = R.drawable.google),
                contentDescription = null,
                tint = Color.Unspecified,
                modifier = Modifier.size(25.dp)

            )
        }

        Spacer(modifier = Modifier.height(16.dp))



    }
}

@Preview
@Composable
fun SignupScreenPreview() {
    var fullName by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var ConfirmPassword by remember { mutableStateOf("") }
    var passwordVisibility by remember { mutableStateOf(false) }

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
            Text(text = "Signup",
                fontSize = 30.sp,
                color = Color.Black,
                modifier = Modifier.padding(8.dp)
            )
            Row(verticalAlignment = Alignment.CenterVertically) {
                Spacer(modifier = Modifier.width(10.dp))
                Text(text = "Already have an account?")
                TextButton(onClick = {/*navController.navigate(Screen.LogInScreen.route) */}) {
                    Text(text = " Login", color = Color.Cyan)
                }
            }
//            Spacer(modifier = Modifier.height(8.dp))

        }


        Spacer(modifier = Modifier.height(16.dp))
        Text(text = "Full Name", fontSize = 18.sp, modifier = Modifier.padding(8.dp))

        OutlinedTextField(
            value = fullName,
            onValueChange = {input -> fullName = input },
            //label = { Text(text = "Full Name") },
            shape = RoundedCornerShape(10.dp),
            modifier = Modifier.fillMaxWidth()
                .background(color = Color.White)
                .padding(10.dp),
            colors =  OutlinedTextFieldDefaults.colors(unfocusedBorderColor = Color.Cyan)
        )


        Spacer(modifier = Modifier.height(8.dp))
        Text(text = "Email", fontSize = 18.sp, modifier = Modifier.padding(8.dp))
        // Email Input
        OutlinedTextField(
            value = email,
            onValueChange = { input -> email=input },
            shape = RoundedCornerShape(10.dp),
            modifier = Modifier.fillMaxWidth()
                .background(color = Color.White)
                .padding(10.dp),
            keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Email),
            colors =  OutlinedTextFieldDefaults.colors(unfocusedBorderColor = Color.Cyan)

        )

        Spacer(modifier = Modifier.height(8.dp))
        Text(text = "Password", fontSize = 18.sp, modifier = Modifier.padding(8.dp))
        // Password Input
        OutlinedTextField(
            value = password,
            onValueChange = { input -> password = input },
            shape = RoundedCornerShape(10.dp),
            modifier = Modifier.fillMaxWidth()
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
            colors =  OutlinedTextFieldDefaults.colors(unfocusedBorderColor = Color.Cyan)


        )

        Spacer(modifier = Modifier.height(8.dp))
        Text(text = "Confirm Password", fontSize = 18.sp, modifier = Modifier.padding(8.dp))
        // Password Input
        OutlinedTextField(
            value = ConfirmPassword,
            onValueChange = { input -> ConfirmPassword = input },
            shape = RoundedCornerShape(10.dp),
            modifier = Modifier.fillMaxWidth()
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
            colors =  OutlinedTextFieldDefaults.colors(unfocusedBorderColor = Color.Cyan)


        )

        // Create Account Button
        Button(
            onClick = {
            },
            shape = RoundedCornerShape(16.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = Color.Black,        // Default background color
                contentColor = Color.White,         // Text color
                disabledContainerColor = Color.Gray // Background color when disabled
            ),
            modifier = Modifier.fillMaxWidth()
                .padding(10.dp)
                .height(60.dp)

        ) {
            Text(text = "Create Account")
        }

        Spacer(modifier = Modifier.height(8.dp))

        // Signup with Google
        TextButton(
            onClick = { /* Handle Google Signup */ },
            modifier = Modifier.fillMaxWidth()

        ) {
            Text(text = "Signup with Google")
            Icon(
                painter = painterResource(id = R.drawable.google),
                contentDescription = null,
                tint = Color.Unspecified,
                modifier = Modifier.size(25.dp)

            )
        }

        Spacer(modifier = Modifier.height(16.dp))


    }
}