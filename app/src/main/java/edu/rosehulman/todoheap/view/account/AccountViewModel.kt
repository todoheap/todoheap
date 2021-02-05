package edu.rosehulman.todoheap.view.account

import android.graphics.drawable.Drawable

data class AccountViewModel(
    val name: String = "",
    var profilePicture: Drawable,
) {
}