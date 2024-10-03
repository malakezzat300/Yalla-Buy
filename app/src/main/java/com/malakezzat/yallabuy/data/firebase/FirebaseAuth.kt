package com.malakezzat.yallabuy.data.firebase

import android.util.Log
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.FirebaseAuthInvalidUserException
import com.google.firebase.auth.FirebaseAuthUserCollisionException
import com.google.firebase.auth.FirebaseAuthWeakPasswordException
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.auth.UserProfileChangeRequest
import com.malakezzat.yallabuy.R

class FirebaseAuthun {
    private val mAuth = FirebaseAuth.getInstance()
    fun signInWithEmailAndPassword(email: String, password: String, name : String) {

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
                                            Log.i("User Is Successfully Created", "username: $name")
                                        }
                                    }
                            } else {
                                Log.e("CreateUser", "Failed to update user profile: ${profileUpdateTask.exception?.message}")
                            }
                        }

                } else {
                    try {
                        throw task.exception!!
                    } catch (e: FirebaseAuthWeakPasswordException) {
                        Log.e("CreateUser", "Weak password.")
                    } catch (e: FirebaseAuthInvalidCredentialsException) {
                        Log.e("CreateUser", "Invalid email.")
                    } catch (e: FirebaseAuthUserCollisionException) {
                        Log.e("CreateUser", "User already exists.")
                    } catch (e: Exception) {
                        Log.e("CreateUser", "Error: ${e.message}")
                    }
                }
            }
    }

    fun logInWithEmailAndPassword(email: String, password: String, onSuccess: () -> Unit, onError: (String) -> Unit) {
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

    fun signInWithGoogle(idToken: String, onSuccess: (FirebaseUser?) -> Unit, onError: (String) -> Unit) {
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

//            val signInRequest = BeginSignInRequest.builder()
//        .setGoogleIdTokenRequestOptions(
//            BeginSignInRequest.GoogleIdTokenRequestOptions.builder()
//                .setSupported(true)
//                // Your server's client ID, not your Android client ID.
//                .setServerClientId(context.getString(R.string.web_id))
//                // Only show accounts previously used to sign in.
//                .setFilterByAuthorizedAccounts(true)
//                .build())
    }

        fun logOut(){
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