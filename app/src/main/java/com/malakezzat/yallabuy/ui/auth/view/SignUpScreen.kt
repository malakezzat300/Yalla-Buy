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

@Composable
fun SignupScreen(viewModel: SignUpViewModel,
                 navController: NavController) {
    var fullName by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") }
    var passwordVisibility by remember { mutableStateOf(false) }
    var isLoading by remember { mutableStateOf(false) }
    val customerData by viewModel.customerDataByEmail.collectAsStateWithLifecycle()
    var auth = FirebaseAuthun()
    val context = LocalContext.current
    var showDialog by remember { mutableStateOf(false) }
    val googleSignInClient = GoogleSignIn.getClient(context, auth.getGoogleSignInOptions(context))
    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.StartActivityForResult()
    ) { result ->
        val task = GoogleSignIn.getSignedInAccountFromIntent(result.data)
        auth.handleSignInResult(task, context, navController)
    }
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
                fontSize = 35.sp,
                color = Color.Black,
                modifier = Modifier.padding(8.dp)
            )
            Row(verticalAlignment = Alignment.CenterVertically) {
                Spacer(modifier = Modifier.width(10.dp))
                Text(text = "Already have an account?")
                TextButton(onClick = {navController.navigate(Screen.LogInScreen.route) }) {
                    Text(text = " Login", color = Color.Green)
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
            modifier = Modifier
                .fillMaxWidth()
                .background(color = Color.White)
                .padding(10.dp),
            colors =  OutlinedTextFieldDefaults.colors(unfocusedBorderColor = Color.Green)
        )


        Spacer(modifier = Modifier.height(8.dp))
        Text(text = "Email", fontSize = 18.sp, modifier = Modifier.padding(8.dp))
        // Email Input
        OutlinedTextField(
            value = email,
            onValueChange = { input -> email=input },
            shape = RoundedCornerShape(10.dp),
            modifier = Modifier
                .fillMaxWidth()
                .background(color = Color.White)
                .padding(10.dp),
            keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Email),
            colors =  OutlinedTextFieldDefaults.colors(unfocusedBorderColor = Color.Green)

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
            colors =  OutlinedTextFieldDefaults.colors(unfocusedBorderColor = Color.Green)


        )

        Spacer(modifier = Modifier.height(8.dp))
        Text(text = "Confirm Password", fontSize = 18.sp, modifier = Modifier.padding(8.dp))
        // Password Input
        OutlinedTextField(
            value = confirmPassword,
            onValueChange = { input -> confirmPassword = input },
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
            colors =  OutlinedTextFieldDefaults.colors(unfocusedBorderColor = Color.Green)


        )

        // Create Account Button
        Button(
            onClick = {
                viewModel.getCustomerById(7716613128374)
                when(customerData){
                    is ApiState.Error -> {
                        Log.i("TAG", "SignupScreen: customer by id failed")
                    }
                    ApiState.Loading -> {

                    }
                    is ApiState.Success -> {
                        val brands = (customerData as ApiState.Success<CustomerSearchRespnse>).data

                        Log.i("TAG", "SignupScreen: ${ brands.customers.get(0).id}")
                    }
                }
                isLoading=true
                if(email.isEmpty() || password.isEmpty() || confirmPassword.isEmpty() || email.isEmpty()){
                    Toast.makeText(context,"complete empty fields please",Toast.LENGTH_LONG).show()
                    isLoading=false
                    //showDialog=true
                }else{
                    if(password == confirmPassword){
                        isLoading = true
                        auth.signInWithEmailAndPassword(email,password,fullName, onSuccess = {
                            showDialog = true
                            /*create customer on API*/
                            val customer = Customerr(
                                first_name = fullName,
                                last_name = "",
                                email = email,
                                phone = ""
                            )

                            val customerRequest = CustomerRequest(customer)
                            viewModel.createCustomer(customerRequest)


                        }, onError = {m->
                            Toast.makeText(context,m,Toast.LENGTH_LONG).show()
                            isLoading=false
                        })

                    }else{
                        isLoading=false
                        Toast.makeText(context,"password and confirm password are not the same",Toast.LENGTH_LONG).show()
                        Log.i("TAG", "SignupScreen: password and confirm password are not the same")
                    }
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
            if (isLoading) {
                CircularProgressIndicator(color = Color.White, modifier = Modifier.size(24.dp))
            } else {
                Text(text = "Create Account",fontSize = 20.sp)
            }

        }

        Spacer(modifier = Modifier.height(8.dp))

        // Signup with Google
        TextButton(
            onClick = {
                val signInIntent = googleSignInClient.signInIntent
                launcher.launch(signInIntent)
                      },
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

        if (showDialog) {
            isLoading=false
            AlertDialog(
                onDismissRequest = { showDialog = false },
                confirmButton = {
                    TextButton(onClick = {
                        showDialog = false
                        navController.navigate(Screen.LogInScreen.route)
                    }) {
                        Text("OK", fontSize = 18.sp, fontWeight = FontWeight.Bold, color = Color.Green)
                    }
                },
                title = {
                    Text(text = "Success", fontSize = 24.sp, fontWeight = FontWeight.Bold)
                },
                text = {
                    Text("A verification email has been sent to your email. \nplease verify your email and login.", fontSize = 15.sp)
                },
                icon = {
                    Icon(
                        imageVector = Icons.Filled.CheckCircle,
                        contentDescription = "Success Icon",
                        tint = Color.Cyan,
                        modifier = Modifier.size(40.dp) // Adjust icon size
                    )
                },

                properties = DialogProperties(dismissOnBackPress = true) , shape = RectangleShape, containerColor = Color.White
            )
        }

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
                fontSize = 35.sp,
                color = Color.Black,
                modifier = Modifier.padding(8.dp)
            )
            Row(verticalAlignment = Alignment.CenterVertically) {
                Spacer(modifier = Modifier.width(10.dp))
                Text(text = "Already have an account?")
                TextButton(onClick = {/*navController.navigate(Screen.LogInScreen.route) */}) {
                    Text(text = " Login", color = Color.Green)
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
            modifier = Modifier
                .fillMaxWidth()
                .background(color = Color.White)
                .padding(10.dp),
            colors =  OutlinedTextFieldDefaults.colors(unfocusedBorderColor = Color.Green)
        )


        Spacer(modifier = Modifier.height(8.dp))
        Text(text = "Email", fontSize = 18.sp, modifier = Modifier.padding(8.dp))
        // Email Input
        OutlinedTextField(
            value = email,
            onValueChange = { input -> email=input },
            shape = RoundedCornerShape(10.dp),
            modifier = Modifier
                .fillMaxWidth()
                .background(color = Color.White)
                .padding(10.dp),
            keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Email),
            colors =  OutlinedTextFieldDefaults.colors(unfocusedBorderColor = Color.Green)

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
            colors =  OutlinedTextFieldDefaults.colors(unfocusedBorderColor = Color.Green)


        )

        Spacer(modifier = Modifier.height(8.dp))
        Text(text = "Confirm Password", fontSize = 18.sp, modifier = Modifier.padding(8.dp))
        // Password Input
        OutlinedTextField(
            value = ConfirmPassword,
            onValueChange = { input -> ConfirmPassword = input },
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
            colors =  OutlinedTextFieldDefaults.colors(unfocusedBorderColor = Color.Green)


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
            modifier = Modifier
                .fillMaxWidth()
                .padding(10.dp)
                .height(60.dp)

        ) {
            Text(text = "Create Account", fontSize = 20.sp)
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