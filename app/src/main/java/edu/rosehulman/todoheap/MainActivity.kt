package edu.rosehulman.todoheap

import android.os.Bundle
import android.util.Log
import com.google.android.material.bottomnavigation.BottomNavigationView
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.firebase.firestore.FirebaseFirestore
import edu.rosehulman.todoheap.data.FreeEvent
import edu.rosehulman.todoheap.data.ScheduledEvent

class MainActivity : AppCompatActivity() {

    private val freeEvents = ArrayList<FreeEvent>()
    private val scheduledEvents = ArrayList<ScheduledEvent>()

    private val eventsRef = FirebaseFirestore.getInstance().collection("events")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val navView: BottomNavigationView = findViewById(R.id.nav_view)

        val navController = findNavController(R.id.nav_host_fragment)
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        val appBarConfiguration = AppBarConfiguration(setOf(
                R.id.navigation_tasks, R.id.navigation_calendar, R.id.navigation_account, R.id.navigation_settings))
        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)

        findViewById<FloatingActionButton>(R.id.fab).setOnClickListener {
            //This code launches the fragment to add an event
            //Problem: The FAB appears on all fragments for now
            //TODO: Change FAB to not appear on the notifications and settings. Make a viewpager to do this, fab.show() and fab.hide()
            //TODO: Add fragments to allow for variable task creation
            Log.d("EventDebug", "Makes an Event")

            //TODO: change to add an event that's more complex
            val event = FreeEvent("Senior Project", true, 0, 0, 0, 4)
            freeEvents.add(event)
            eventsRef.add(event)
//            Log.d("EventDebug", "Events:")
//            for (e in freeEvents) {
//                Log.d("EventDebug", e.toString())
//            }


        }
    }
}