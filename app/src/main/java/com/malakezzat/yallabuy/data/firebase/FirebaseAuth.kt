package com.malakezzat.yallabuy.data.firebase

import android.util.Log
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.FirebaseAuthUserCollisionException
import com.google.firebase.auth.FirebaseAuthWeakPasswordException
import com.google.firebase.auth.UserProfileChangeRequest

class FirebaseAuthun {
    private val mAuth = FirebaseAuth.getInstance()
    fun signInWithEmailAndPassword(email: String, password: String, name : String) {
//    val signInRequest = BeginSignInRequest.builder()
//        .setGoogleIdTokenRequestOptions(
//            BeginSignInRequest.GoogleIdTokenRequestOptions.builder()
//                .setSupported(true)
//                // Your server's client ID, not your Android client ID.
//                .setServerClientId(context.getString(R.string.web_id))
//                // Only show accounts previously used to sign in.
//                .setFilterByAuthorizedAccounts(true)
//                .build())

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

    fun logInWithEmailAndPassword(){}

    fun signInWithGoogle(){

    }

    fun logOut(){

    }

    fun signInAnonymously(){}
}