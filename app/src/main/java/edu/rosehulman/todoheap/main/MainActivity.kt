package edu.rosehulman.todoheap.main

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.bottomnavigation.BottomNavigationView
import edu.rosehulman.todoheap.Constants
import edu.rosehulman.todoheap.R
import edu.rosehulman.todoheap.account.controller.AccountController
import edu.rosehulman.todoheap.account.model.SettingsModel
import edu.rosehulman.todoheap.calendar.model.CalendarPageModel
import edu.rosehulman.todoheap.common.model.FreeEvent
import edu.rosehulman.todoheap.data.Database
import edu.rosehulman.todoheap.databinding.ActivityMainBinding
import edu.rosehulman.todoheap.tasks.model.TaskPageModel

class MainActivity : AppCompatActivity() {


    private lateinit var binding: ActivityMainBinding
    lateinit var app: App
    lateinit var controller: Controller
    lateinit var accountController: AccountController
    lateinit var navController: NavController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        initApp()
        initController()
        val navView: BottomNavigationView = binding.bottomNav

        navController = findNavController(R.id.nav_host_fragment)

        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        val appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.navigation_tasks,
                R.id.navigation_calendar,
                R.id.navigation_account
            )
        )
        setupActionBarWithNavController(navController, appBarConfiguration)


        navView.setupWithNavController(navController)
        navController.navigate(R.id.navigation_tasks)
        navController.addOnDestinationChangedListener { controller, destination, arguments ->
            if(destination.id==R.id.navigation_account){
                binding.fab.hide()
            }else{
                binding.fab.show()
            }

        }
    }

    override fun onStart() {
        super.onStart()
        accountController.setupListener()
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
//
//        }
        stopService(Intent(this, NotificationService::class.java))
    }

    override fun onStop() {
        super.onStop()
        accountController.removeListener()
        startService(Intent(this, NotificationService::class.java))
    }

//    override fun onDestroy() {
//        //startService(Intent(this, NotificationService::class.java))
////        val broadcastIntent = Intent()
////        broadcastIntent.action = "restartservice"
////        broadcastIntent.setClass(this, Restarter::class.java)
////        this.sendBroadcast(broadcastIntent)
//        super.onDestroy()
//    }

    private fun initApp() {
        val taskPageModel = TaskPageModel()
        val calendarPageModel = CalendarPageModel()
        val settingsModel = SettingsModel()
        app = App(taskPageModel, calendarPageModel, settingsModel)
    }

    private fun initController() {
        controller = Controller(this)
        binding.controller = controller
        accountController = AccountController(this)
    }

    fun loginOK(){
        Log.d(Constants.TAG, "INIT")
        app.init()
        navController.navigate(R.id.navigation_tasks)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        Log.d(Constants.TAG, "Result")
        val event = data?.getParcelableExtra<FreeEvent>(Constants.KEY_FREE_EVENT)
        if (event != null) {
            when (requestCode) {
                Constants.RC_ADD_FREE_EVENT -> Database.freeEventsCollection?.add(event)
                Constants.RC_EDIT_FREE_EVENT -> {
                    val id = data?.getStringExtra(Constants.KEY_FREE_EVENT_ID)
                    event.id = id
                    Database.freeEventsCollection?.document(id!!)?.set(event)
                }
            }
        }
    }

}