package edu.rosehulman.todoheap.controller.account

import android.content.Intent
import android.util.Log
import com.firebase.ui.auth.AuthUI
import com.google.firebase.auth.FirebaseAuth
import edu.rosehulman.todoheap.Constants
import edu.rosehulman.todoheap.R
import edu.rosehulman.todoheap.activities.MainActivity
import edu.rosehulman.todoheap.data.AuthManager
import edu.rosehulman.todoheap.data.Database


class AccountController(
    private val activity: MainActivity
) {

    private val authListener = FirebaseAuth.AuthStateListener {
        val user = it.currentUser
        if (user==null){
            launchLoginUI()
        }else{
            Log.d(Constants.TAG, "login ok $user");
            activity.loginOK()
        }
    }

    fun setupListener(){
        Database.auth.addAuthStateListener(authListener)
    }
    fun removeListener(){
        Database.auth.removeAuthStateListener(authListener)
    }

    fun onLogOut() {
        Database.signOut()
    }

    private fun launchLoginUI() {
        val providers = arrayListOf(
            AuthUI.IdpConfig.EmailBuilder().build(),
            AuthUI.IdpConfig.GoogleBuilder().build()
        )
        val loginIntent = AuthUI.getInstance()
            .createSignInIntentBuilder()
            .setAvailableProviders(providers)
            .setLogo(R.drawable.common_google_signin_btn_icon_light)
            .setTheme(R.style.Theme_TodoHeap)
            .build()
        //loginIntent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
        activity.startActivityForResult(loginIntent, 1)
       // activity.start
    }
}