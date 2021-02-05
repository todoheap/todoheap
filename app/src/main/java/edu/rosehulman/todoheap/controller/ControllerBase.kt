package edu.rosehulman.todoheap.controller

import android.content.Context
import android.util.Log
import android.view.View
import android.view.inputmethod.InputMethodManager
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import edu.rosehulman.todoheap.Constants
import edu.rosehulman.todoheap.R
import edu.rosehulman.todoheap.databinding.DialogInputDateBinding
import edu.rosehulman.todoheap.databinding.DialogInputTimeBinding
import edu.rosehulman.todoheap.viewmodel.DateInputViewModel
import edu.rosehulman.todoheap.viewmodel.TimeInputViewModel

abstract class ControllerBase(
    private val activity: AppCompatActivity
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
        val inputMethodManager = ContextCompat.getSystemService(activity, InputMethodManager::class.java)
        inputMethodManager?.hideSoftInputFromWindow(view.windowToken, 0)
    }

    fun promptDate(initYear: Int, initMonth: Int, initDay: Int, callback: (year: Int, month: Int, dayOfMonth: Int)->Unit) {
        val binding = DataBindingUtil.inflate<DialogInputDateBinding>(activity.layoutInflater, R.layout.dialog_input_date, null, false)
        val model = DateInputViewModel(initYear, initMonth, initDay)
        binding.model = model
        AlertDialog.Builder(activity)
            .setTitle(R.string.title_dialog_date)
            .setView(binding.root)
            .setPositiveButton(android.R.string.ok){ _ , _->
                callback(model.year, model.month,model.dayOfMonth)
            }.setNegativeButton(android.R.string.cancel){ _ , _->
            }.create()
            .show()
    }

    fun promptTime(initHour: Int, initMinute: Int, callback: (hour: Int, minute: Int)->Unit) {
        val binding = DataBindingUtil.inflate<DialogInputTimeBinding>(activity.layoutInflater, R.layout.dialog_input_time, null, false)
        val model = TimeInputViewModel(initHour, initMinute)
        binding.model = model
        AlertDialog.Builder(activity)
            .setTitle(R.string.title_dialog_time)
            .setView(binding.root)
            .setPositiveButton(android.R.string.ok){ _ , _->
                Log.d(Constants.TAG, "Time model: $model")
                callback(model.hour, model.minute)
            }.setNegativeButton(android.R.string.cancel){ _ , _->
            }.create()
            .show()
    }
}