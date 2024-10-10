package com.malakezzat.yallabuy.ui.profile.view

import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Scaffold
import androidx.compose.material.Snackbar
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.modifier.modifierLocalConsumer
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.navigation.NavController
import com.google.firebase.auth.FirebaseAuth
import com.malakezzat.yallabuy.ui.CustomTopBar
import com.malakezzat.yallabuy.ui.Screen
import com.malakezzat.yallabuy.ui.customAlert
import com.malakezzat.yallabuy.ui.theme.AppColors


@Composable
fun ChangePasswordScreen(navController: NavController){
    var pass by remember { mutableStateOf("") }
    var ConfirmPass by remember { mutableStateOf("") }
    var isLoading by remember { mutableStateOf(false) }
    var isChanged by remember { mutableStateOf(false) }
    val context = LocalContext.current

    androidx.compose.material3.Scaffold(
        topBar = { CustomTopBar(navController, "Change Password", AppColors.Teal) },
        containerColor = Color.White,
        content = { paddingValues ->
            Column (modifier = Modifier
                .padding(paddingValues)
                .padding(top = 20.dp)
            ){

                Text("New Password", fontSize = 18.sp, modifier = Modifier.padding(8.dp))
                OutlinedTextField(
                    value = pass,
                    onValueChange = {input -> pass = input },
                    shape = RoundedCornerShape(30.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(75.dp)
                        .background(color = Color.White)
                        .padding(10.dp),
                    colors =  OutlinedTextFieldDefaults.colors(unfocusedBorderColor = AppColors.MintGreen),
                    visualTransformation =  PasswordVisualTransformation()
                )
                Text("Confirm Password", fontSize = 18.sp, modifier = Modifier.padding(8.dp))
                OutlinedTextField(
                    value = ConfirmPass,
                    onValueChange = {input -> ConfirmPass = input },
                    shape = RoundedCornerShape(30.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(75.dp)
                        .background(color = Color.White)
                        .padding(10.dp),
                    colors =  OutlinedTextFieldDefaults.colors(unfocusedBorderColor = AppColors.MintGreen),
                    visualTransformation =  PasswordVisualTransformation()
                )
                Spacer(modifier = Modifier.height(8.dp))
                // Create Account Button
                Button(
                    onClick = {
                        isLoading = true
                        //  viewModel.signInWithEmailAndPassword(email,password,fullName)
                        if(pass.isEmpty() || ConfirmPass.isEmpty()){
                            Log.i("TAG", "ChangePasswordScreen: empty")
                            Toast.makeText(context,"complete empty fields please", Toast.LENGTH_LONG).show()

                            isLoading = false
                        }else{
                            if(pass == ConfirmPass){
                                isLoading=true
                                val user = FirebaseAuth.getInstance().currentUser
                                if (user != null) {
                                    user.updatePassword(pass)
                                        .addOnCompleteListener { task ->
                                            if (task.isSuccessful) {
                                                Log.d("PasswordChange", "Password updated successfully.")
                                                isLoading=false
                                                isChanged=true
                                                //Toast.makeText(LocalContext.current, "Password changed successfully.", Toast.LENGTH_SHORT).show()
                                            } else {
                                                Log.e("PasswordChange", "Error: ${task.exception?.message}")
                                                // Toast.makeText(this, "Failed to change password.", Toast.LENGTH_SHORT).show()
                                            }
                                        }
                                }
                            }else{
                                isLoading=false
                                Toast.makeText(context,"password and confirm password are not the same", Toast.LENGTH_LONG).show()
                            }

                        }

                    },
                    shape = RoundedCornerShape(30.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = AppColors.Teal,
                        contentColor = Color.White,
                        disabledContainerColor = Color.Gray
                    ),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(10.dp)
                        .height(60.dp)

                ) {
                    if (isLoading) {
                        CircularProgressIndicator(color = Color.White, modifier = Modifier.size(24.dp))
                    } else {
                        Text(text = "Change Password", fontSize = 20.sp)
                    }

                }
                if(isChanged){
                    CustomDialog(navController)
                }

            }

        })
        }

@Composable
fun CustomDialog(navController: NavController) {
    var showDialog by remember { mutableStateOf(true) }
    AlertDialog(
        containerColor = Color.White,
        onDismissRequest = {  showDialog = false },
        title = { Text("Change Password") },
        text = { Text("Your password has been changed successfully") },
        confirmButton = {
            TextButton(onClick = {
                showDialog = false
                navController.navigate(Screen.HomeScreen.route)
            }) {
                Text("Ok", color = Color.Red)
            }
        },


    )
}
