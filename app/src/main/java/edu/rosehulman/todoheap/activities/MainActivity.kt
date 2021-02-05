package edu.rosehulman.todoheap.activities

import android.os.Bundle
import android.util.Log
import com.google.android.material.bottomnavigation.BottomNavigationView
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import edu.rosehulman.todoheap.R
import edu.rosehulman.todoheap.controller.Controller
import edu.rosehulman.todoheap.databinding.ActivityMainBinding
import edu.rosehulman.todoheap.model.App
import edu.rosehulman.todoheap.model.task.TaskPageModel
import edu.rosehulman.todoheap.view.account.AccountFragment

class MainActivity : AppCompatActivity() {


    private lateinit var binding: ActivityMainBinding
    lateinit var app: App
    lateinit var controller: Controller

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        initApp()
        initController()
        val navView: BottomNavigationView = binding.bottomNav

        val navController = findNavController(R.id.nav_host_fragment)

        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        val appBarConfiguration = AppBarConfiguration(setOf(
            R.id.navigation_tasks,
            R.id.navigation_calendar,
            R.id.navigation_account,
            R.id.navigation_settings
        ))
        setupActionBarWithNavController(navController, appBarConfiguration)

        navController.addOnDestinationChangedListener {controller, destination, bundle ->
            when (destination.label) {
                "Account" -> AccountFragment()
            }
        }

        navView.setupWithNavController(navController)
        navController.navigate(R.id.navigation_tasks)

    }

    private fun initApp() {
        val taskPageModel = TaskPageModel()
        app = App(taskPageModel)
    }

    private fun initController() {
        controller = Controller(this)
        binding.controller = controller
    }

}