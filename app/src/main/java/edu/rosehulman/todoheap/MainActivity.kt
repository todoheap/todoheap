package edu.rosehulman.todoheap

import android.os.Bundle
import android.util.Log
import com.google.android.material.bottomnavigation.BottomNavigationView
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import edu.rosehulman.todoheap.data.Database
import edu.rosehulman.todoheap.databinding.ActivityMainBinding
import edu.rosehulman.todoheap.model.FreeEvent
import edu.rosehulman.todoheap.model.ScheduledEvent

class MainActivity : AppCompatActivity() {

    private val scheduledEvents = ArrayList<ScheduledEvent>()

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)

        val navView: BottomNavigationView = binding.bottomNav

        val navController = findNavController(R.id.nav_host_fragment)
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        val appBarConfiguration = AppBarConfiguration(setOf(
                R.id.navigation_tasks, R.id.navigation_calendar, R.id.navigation_account, R.id.navigation_settings))
        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)

        binding.fab.setOnClickListener {
            //This code launches the fragment to add an event
            //Problem: The FAB appears on all fragments for now
            //TODO: Change FAB to not appear on the notifications and settings. Make a viewpager to do this, fab.show() and fab.hide()
            //TODO: Add fragments to allow for variable task creation
            Log.d("EventDebug", "Makes an Event")

            //TODO: change to add an event that's more complex
            val event = FreeEvent("Senior Project", true, 0, 0, 0, 4.0)
            Database.eventsCollection.add(event)
//            Log.d("EventDebug", "Events:")
//            for (e in freeEvents) {
//                Log.d("EventDebug", e.toString())
//            }


        }
    }

}