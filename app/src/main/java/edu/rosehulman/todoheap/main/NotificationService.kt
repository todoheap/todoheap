package edu.rosehulman.todoheap.main

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.Service
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Handler
import android.os.IBinder
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import edu.rosehulman.todoheap.Constants
import edu.rosehulman.todoheap.Constants.TAG
import edu.rosehulman.todoheap.R
import java.util.*


class NotificationService : Service() {
    var timer: Timer? = null
    var timerTask: TimerTask? = null
    override fun onBind(intent: Intent?): IBinder? {
        return null
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        super.onStartCommand(intent, flags, startId)

        startTimer()
        //sendNotification()
        return START_STICKY
    }


    override fun onCreate() {
        Log.e(TAG, "onCreate")
//        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.O) startMyOwnForeground() else startForeground(
//            1,
//            Notification()
//        )

        startForeground((System.currentTimeMillis()%1000).toInt(), Notification.Builder(this, Constants.CHANNEL_ID).build())

    }

    override fun onDestroy() {
        Log.e(TAG, "onDestroy")
        stoptimertask()
        super.onDestroy()
//        val broadcastIntent = Intent()
//        broadcastIntent.action = "restartservice"
//        broadcastIntent.setClass(this, Restarter::class.java)
//        this.sendBroadcast(broadcastIntent)
    }

    //we are going to use a handler to be able to run in our TimerTask
    val handler: Handler = Handler()
//
//
    fun startTimer() {
        //set a new Timer
        timer = Timer()

        //initialize the TimerTask's job
        initializeTimerTask()

        //schedule the timer, after the first 5000ms the TimerTask will run every 10000ms
        timer?.schedule(timerTask, 5000, 5 * 1000) //
        //timer.schedule(timerTask, 5000,1000); //
    }

    private fun initializeTimerTask() {
        timerTask = object : TimerTask() {
            override fun run() {
                sendNotification()
            }
        }
    }

    fun stoptimertask() {
        //stop the timer, if it's not already null
        if (timer != null) {
            timer?.cancel()
            timer = null
        }
    }

    fun sendNotification(){
        val builder = NotificationCompat.Builder(
            this@NotificationService,
            Constants.CHANNEL_ID
        )
            .setSmallIcon(R.drawable.ic_account)
            .setContentTitle("title")
            .setContentText("test")
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setAutoCancel(true)
        //startForeground(1, builder.build())

        val name = getString(R.string.channel_name)
        val descriptionText = getString(R.string.channel_description)
        val importance = NotificationManager.IMPORTANCE_DEFAULT
        val channel = NotificationChannel(Constants.CHANNEL_ID, name, importance).apply {
            description = descriptionText
        }
        // Register the channel with the system
        val notificationManager: NotificationManager =
            getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.createNotificationChannel(channel)
        with(NotificationManagerCompat.from(this@NotificationService)) {
            notify((System.currentTimeMillis()%1000).toInt(), builder.build())
        }
    }
}