package edu.rosehulman.todoheap.main

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Build




class Restarter : BroadcastReceiver() {
    override fun onReceive(context: Context?, intent: Intent?) {
        //TODO("Not yet implemented")
        context!!.startForegroundService(Intent(context, NotificationService::class.java))
    }

}