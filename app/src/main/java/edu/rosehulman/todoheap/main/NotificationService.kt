package edu.rosehulman.todoheap.main

import android.app.Service
import android.content.Intent
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
        return START_STICKY
    }


    override fun onCreate() {
        Log.e(TAG, "onCreate")
    }

    override fun onDestroy() {
        Log.e(TAG, "onDestroy")
        stoptimertask()
        super.onDestroy()
    }

    //we are going to use a handler to be able to run in our TimerTask
    val handler: Handler = Handler()


    fun startTimer() {
        //set a new Timer
        timer = Timer()

        //initialize the TimerTask's job
        initializeTimerTask()

        //schedule the timer, after the first 5000ms the TimerTask will run every 10000ms
        timer?.schedule(timerTask, 5000, 5 * 1000) //
        //timer.schedule(timerTask, 5000,1000); //
    }

    fun stoptimertask() {
        //stop the timer, if it's not already null
        if (timer != null) {
            timer?.cancel()
            timer = null
        }
    }

    fun initializeTimerTask() {
        timerTask = object : TimerTask() {
            override fun run() {

                //use a handler to run a toast that shows the current timestamp
                handler.post(Runnable { //TODO CALL NOTIFICATION FUNC
                    val builder = NotificationCompat.Builder(this@NotificationService, Constants.CHANNEL_ID)
                        .setSmallIcon(R.drawable.ic_account)
                        .setContentTitle("title")
                        .setContentText("test")
                        .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                        .setAutoCancel(true)

                    with(NotificationManagerCompat.from(this@NotificationService)) {
                        notify(1, builder.build())
                    }
                })
            }
        }
    }
}