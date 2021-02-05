package edu.rosehulman.todoheap.view.account

import android.app.Activity
import android.content.Context
import android.graphics.Bitmap
import android.os.Bundle
import android.util.Log
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import com.firebase.ui.auth.AuthUI
import com.google.firebase.auth.FirebaseAuth
import edu.rosehulman.todoheap.GetImageTask
import edu.rosehulman.todoheap.R
import edu.rosehulman.todoheap.databinding.FragmentAccountBinding

class AccountFragment: Fragment(), GetImageTask.ImageConsumer {
    lateinit var binding: FragmentAccountBinding

    private val auth = FirebaseAuth.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(context as Activity, R.layout.fragment_account)
        binding.fragment = this
    }

    fun onLoginButtonPressed() {
        if (getIsLoggedIn()) {
            auth.signOut()
        }
        else launchLoginUI()
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
            .build()

        startActivityForResult(loginIntent, 1)
    }

    private fun getIsLoggedIn() = auth.currentUser != null

    fun getButtonText() = if (getIsLoggedIn()) R.string.sign_out else R.string.sign_in

    override fun onImageLoaded(image: Bitmap?) {
        binding.profilePic.setImageBitmap(image)
        Log.d("ProfilePicDebug", "Image Bitmap Set $image")
    }

}