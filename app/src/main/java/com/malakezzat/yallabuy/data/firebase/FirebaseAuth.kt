package com.malakezzat.yallabuy.data.firebase

import android.content.Context
import android.util.Log
import android.widget.Toast
import androidx.navigation.NavController
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.FirebaseAuthInvalidUserException
import com.google.firebase.auth.FirebaseAuthUserCollisionException
import com.google.firebase.auth.FirebaseAuthWeakPasswordException
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.auth.UserProfileChangeRequest
import com.malakezzat.yallabuy.R
import com.malakezzat.yallabuy.ui.Screen

class FirebaseAuthun {
    private val mAuth = FirebaseAuth.getInstance()
    //private val userEmail = FirebaseAuth.getInstance().currentUser?.email
//    fun signInWithEmailAndPassword(email: String, password: String, name: String) : Boolean {
//        var isSuccess = false
//        mAuth.createUserWithEmailAndPassword(email, password)
//            .addOnCompleteListener { task ->
//                if (task.isSuccessful) {
//                    val user = mAuth.currentUser
//                    val profileUpdates = UserProfileChangeRequest.Builder()
//                        .setDisplayName(name)
//                        .build()
//                    user?.updateProfile(profileUpdates)
//                        ?.addOnCompleteListener { profileUpdateTask ->
//                            if (profileUpdateTask.isSuccessful) {
//                                user.sendEmailVerification()
//                                    .addOnCompleteListener { verificationTask ->
//                                        if (verificationTask.isSuccessful) {
//                                            isSuccess=true
//                                            Log.i("UserIsSuccessfullyCreated", "username: $name")
//
//                                        }
//                                    }
//                            } else {
//                                Log.e(
//                                    "CreateUser",
//                                    "Failedtoupdateuserprofile: ${profileUpdateTask.exception?.message}"
//                                )
//                            }
//                        }
//
//                } else {
//                    try {
//                        throw task.exception!!
//                    } catch (e: FirebaseAuthWeakPasswordException) {
//                        Log.e("CreateUser", "Weak password.")
//                    } catch (e: FirebaseAuthInvalidCredentialsException) {
//                        Log.e("CreateUser", "Invalid email.")
//                    } catch (e: FirebaseAuthUserCollisionException) {
//                        Log.e("CreateUser", "User already exists.")
//                    } catch (e: Exception) {
//                        Log.e("CreateUser", "Error: ${e.message}")
//                    }
//                }
//            }
//        return isSuccess
//    }

    fun signInWithEmailAndPassword(
        email: String,
        password: String,
        name: String,
        onSuccess: () -> Unit,
        onError: (String) -> Unit
    ) {
        mAuth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val user = mAuth.currentUser
                    val profileUpdates = UserProfileChangeRequest.Builder()
                        .setDisplayName(name)
                        .build()

                    user?.updateProfile(profileUpdates)
                        ?.addOnCompleteListener { profileUpdateTask ->
                            if (profileUpdateTask.isSuccessful) {
                                user.sendEmailVerification()
                                    .addOnCompleteListener { verificationTask ->
                                        if (verificationTask.isSuccessful) {
                                            Log.i("UserCreated", "User successfully created with name: $name")
                                            onSuccess()
                                        } else {
                                            onError("Failed to send verification email.")
                                        }
                                    }
                            } else {
                                onError("Failed to update user profile: ${profileUpdateTask.exception?.message}")
                            }
                        }
                } else {
                    try {
                        throw task.exception!!
                    } catch (e: FirebaseAuthWeakPasswordException) {
                        Log.e("SignIn", "Weak password.")
                        onError("Weak password.")
                    } catch (e: FirebaseAuthInvalidCredentialsException) {
                        Log.e("SignIn", "Invalid email.")
                        onError("Invalid email.")
                    } catch (e: FirebaseAuthUserCollisionException) {
                        Log.e("SignIn", "User already exists.")
                        onError("User already exists.")
                    } catch (e: Exception) {
                        Log.e("SignIn", "Error: ${e.message}")
                        onError("Error: ${e.message}")
                    }
                }
            }
    }
    fun logInWithEmailAndPassword(
        email: String,
        password: String,
        onSuccess: () -> Unit,
        onError: (String) -> Unit
    ) {
        mAuth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    Log.i("Login", "Login successful")
                    onSuccess()
                } else {
                    try {
                        throw task.exception!!
                    } catch (e: FirebaseAuthInvalidCredentialsException) {
                        Log.e("Login", "Invalid credentials.")
                        onError("Invalid credentials.")
                    } catch (e: FirebaseAuthInvalidUserException) {
                        Log.e("Login", "User not found.")
                        onError("User not found.")
                    } catch (e: Exception) {
                        Log.e("Login", "Login error: ${e.message}")
                        onError("Login error: ${e.message}")
                    }
                }
            }
    }

    fun signInWithGoogle(
        idToken: String,
        onSuccess: (FirebaseUser?) -> Unit,
        onError: (String) -> Unit
    ) {
        val credential = GoogleAuthProvider.getCredential(idToken, null)
        mAuth.signInWithCredential(credential)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val user = mAuth.currentUser
                    Log.i("GoogleSignIn", "Google sign-in successful, user: ${user?.displayName}")
                    onSuccess(user)
                } else {
                    Log.e("GoogleSignIn", "Google sign-in failed: ${task.exception?.message}")
                    onError("Google sign-in failed: ${task.exception?.message}")
                }
            }


    }

    fun logOut() {
        mAuth.signOut()
        Log.i("Logout", "User has been logged out.")
    }

    fun signInAnonymously(onSuccess: (FirebaseUser?) -> Unit, onError: (String) -> Unit) {
        mAuth.signInAnonymously()
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val user = mAuth.currentUser
                    Log.i("AnonymousSignIn", "Anonymous sign-in successful, user: ${user?.uid}")
                    onSuccess(user)
                } else {
                    Log.e("AnonymousSignIn", "Anonymous sign-in failed: ${task.exception?.message}")
                    onError("Anonymous sign-in failed: ${task.exception?.message}")
                }
            }
    }

    private fun sendEmailVerification(onSuccess: () -> Unit, onError: (String) -> Unit) {
        val user = mAuth.currentUser
        user?.sendEmailVerification()
            ?.addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    onSuccess()
                } else {
                    onError("Failed to send verification email: ${task.exception?.message}")
                }
            }
    }

    fun getGoogleSignInOptions(context : Context): GoogleSignInOptions {
        return GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(context.getString(R.string.web_client_id)) // Use your web client ID
            .requestEmail()
            .build()
    }

    fun handleSignInResult(
        task: Task<GoogleSignInAccount>,
        context: Context,
        navController: NavController
    ) {
        try {
            val account = task.getResult(ApiException::class.java)
            account?.let {
                val credential = GoogleAuthProvider.getCredential(it.idToken, null)
                FirebaseAuth.getInstance().signInWithCredential(credential)
                    .addOnCompleteListener { task ->
                        if (task.isSuccessful) {
                            Toast.makeText(context, "Signed in as ${it.displayName}", Toast.LENGTH_LONG).show()
                            navController.navigate(Screen.HomeScreen.route)
                        } else {
                            Toast.makeText(context, "Authentication Failed", Toast.LENGTH_LONG)
                                .show()
                        }
                    }
            }
        } catch (e: ApiException) {
            Toast.makeText(context, "Google sign-in failed: ${e.message}", Toast.LENGTH_LONG).show()
        }
    }
        private fun handleAuthException(exception: Exception?) {
            try {
                throw exception!!
            } catch (e: FirebaseAuthWeakPasswordException) {
                Log.e("AuthError", "Weak password.")
            } catch (e: FirebaseAuthInvalidCredentialsException) {
                Log.e("AuthError", "Invalid credentials.")
            } catch (e: FirebaseAuthUserCollisionException) {
                Log.e("AuthError", "User already exists.")
            } catch (e: Exception) {
                Log.e("AuthError", "Error: ${e.message}")
            }
        }
    }
