package edu.rosehulman.todoheap.main

import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import androidx.appcompat.app.AlertDialog
import androidx.databinding.DataBindingUtil
import edu.rosehulman.todoheap.Constants
import edu.rosehulman.todoheap.R
import edu.rosehulman.todoheap.calendar.controller.CalendarController
import edu.rosehulman.todoheap.freeevent.FreeEventActivity
import edu.rosehulman.todoheap.databinding.RadioEventTypeBinding
import edu.rosehulman.todoheap.tasks.controller.TaskController

class Controller(private val activity: MainActivity) {

    private lateinit var dialog: AlertDialog
    val taskController: TaskController = TaskController(activity, activity.app.taskPageModel)
    val calendarController: CalendarController = CalendarController(activity, activity.app.calendarPageModel)

    fun onClickFab() {

        Log.d("EventDebug", "Makes an Event")
        val builder = AlertDialog.Builder(activity)

        //Inflate the view and bind the data
        val bind = DataBindingUtil.inflate<RadioEventTypeBinding>(LayoutInflater.from(activity), R.layout.radio_event_type, null, false)
        bind.controller = this
        builder.setView(bind.root)
        dialog = builder.create()
        dialog.show()
    }

    fun onClickFree() {
        Log.d("EventDebug", "Free Clicked")
        val intent = Intent(activity, FreeEventActivity::class.java)
            .putExtra(Constants.KEY_SET_TITLE,activity.resources.getString(R.string.title_add_free_event))
        activity.startActivityForResult(intent, Constants.RC_ADD_FREE_EVENT)
        dialog.dismiss()
    }

    fun onClickScheduled() {
        Log.d("EventDebug", "Scheduled Clicked")
        dialog.dismiss()
    }





}