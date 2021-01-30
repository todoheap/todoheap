package edu.rosehulman.todoheap.controller

import android.content.Context
import android.view.View
import android.view.inputmethod.InputMethodManager
import androidx.core.content.ContextCompat

abstract class ControllerBase(
    private val context: Context
) {
    fun hideKeyboardOnLoseFocus(view: View, hasFocus: Boolean) {
        hideKeyboardOnFocusEqual(view, hasFocus, false)
    }

    fun hideKeyboardOnGainFocus(view: View, hasFocus: Boolean) {
        hideKeyboardOnFocusEqual(view, hasFocus, true)
    }

    private fun hideKeyboardOnFocusEqual(view: View, hasFocus: Boolean, hideOnHasFocus: Boolean) {
        if(hasFocus==hideOnHasFocus) {
           hideKeyboard(view)
        }
    }

    fun hideKeyboard(view: View) {
        val inputMethodManager = ContextCompat.getSystemService(context, InputMethodManager::class.java)
        inputMethodManager?.hideSoftInputFromWindow(view.windowToken, 0)
    }
}