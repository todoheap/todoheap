package edu.rosehulman.todoheap.controller

import android.content.Context
import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat.startActivity
import androidx.databinding.DataBindingUtil
import edu.rosehulman.todoheap.R
import edu.rosehulman.todoheap.activities.AddFreeActivity
import edu.rosehulman.todoheap.data.Database
import edu.rosehulman.todoheap.databinding.RadioEventTypeBinding
import edu.rosehulman.todoheap.model.FreeEvent

class Controller(val context: Context) {

    private lateinit var dialog: AlertDialog

    fun onClickFab() {

        Log.d("EventDebug", "Makes an Event")
        val builder = AlertDialog.Builder(context)

        //Inflate the view and bind the data
        val bind = DataBindingUtil.inflate<RadioEventTypeBinding>(LayoutInflater.from(context), R.layout.radio_event_type, null, false)
        bind.controller = this
        builder.setView(bind.root)
        dialog = builder.create()
        dialog.show()
    }

    fun onClickFree() {
        Log.d("EventDebug", "Free Clicked")
        val addFreeIntent = Intent(context, AddFreeActivity::class.java)
        context.startActivity(addFreeIntent)
        dialog.dismiss()
    }

    fun onClickScheduled() {
        Log.d("EventDebug", "Scheduled Clicked")
        dialog.dismiss()
    }

}