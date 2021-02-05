package edu.rosehulman.todoheap.activities

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.os.SystemClock
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import edu.rosehulman.todoheap.R
import edu.rosehulman.todoheap.controller.AddFreeEventController
import edu.rosehulman.todoheap.data.Database
import edu.rosehulman.todoheap.databinding.AddFreeEventBinding
import edu.rosehulman.todoheap.model.FreeEvent
import edu.rosehulman.todoheap.viewmodel.FreeEventInputViewModel
import io.grpc.Deadline
import java.util.*

class AddFreeActivity: AppCompatActivity() {

    lateinit var binding: AddFreeEventBinding
    lateinit var controller: AddFreeEventController
    lateinit var model: FreeEventInputViewModel

    //private lateinit var enjoyabilitySpinner: Spinner
    //private lateinit var procrastinationSpinner: Spinner

    private var enjoyability = 0
    private var procrastination = 0
    private var deadline: Date = Calendar.getInstance().time

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.add_free_event)
        controller = AddFreeEventController(this)
        binding.controller = controller
        model = FreeEventInputViewModel()

        initFields()

        setSupportActionBar(binding.toolbar)
        supportActionBar?.setHomeAsUpIndicator(R.drawable.ic_baseline_close_24)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }

    private fun initFields() {
       // enjoyabilitySpinner = findViewById(R.id.enjoyable_spinner)
       // procrastinationSpinner = findViewById(R.id.procrastination_spinner)
        // Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter.createFromResource(
            this,
            R.array.enjoyability_options,
            android.R.layout.simple_spinner_item
        ).also { adapter ->
            // Specify the layout to use when the list of choices appears
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            // Apply the adapter to the spinner
            binding.enjoyableSpinner.adapter = adapter
        }

        // Do the same here for the other spinner
        ArrayAdapter.createFromResource(
            this,
            R.array.procrastination_options,
            android.R.layout.simple_spinner_item
        ).also { adapter ->
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            binding.procrastinationSpinner.adapter = adapter
        }

        findViewById<DatePicker>(R.id.date_picker).setOnDateChangedListener { _, year, monthOfYear, dayOfMonth ->
            deadline = Date(year, monthOfYear, dayOfMonth)
        }

    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.menu_add_free_event, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when(item.itemId){
            android.R.id.home -> {
                cancel()
                true
            }
            R.id.action_save -> {
                save()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    fun cancel() {
        Log.d("EventDebug", "Going Back to Main")
        finish()
    }

    fun save() {
        Log.d("EventDebug", "adding new event")

        val nameEditText = findViewById<EditText>(R.id.editTextEventName)
        Log.d("EventDebug", "Got past name")
        val locationEditText = findViewById<EditText>(R.id.editTextLocation)
        Log.d("EventDebug", "Got past location")
        val hasDeadline = !findViewById<CheckBox>(R.id.deadline_checkbox).isChecked
        Log.d("EventDebug", "Got past deadline")
        val workloadEditText = findViewById<EditText>(R.id.workload_edit_text)
        Log.d("EventDebug", "Got past workload")
        val isMultipleSessonsTrue = findViewById<RadioButton>(R.id.is_one_sitting_true).isSelected
        Log.d("EventDebug", "Got past multi1")
        val isMultipleSessionsFalse = findViewById<RadioButton>(R.id.is_one_sitting_false).isSelected
        Log.d("EventDebug", "Got past multi2")

        val newEvent = FreeEvent(
            nameEditText.text.toString(),
            isMultipleSessonsTrue && !isMultipleSessionsFalse,
            0, procrastination, enjoyability,
            java.lang.Double.parseDouble(workloadEditText.text.toString()),
            locationEditText.text.toString(),
            (if (hasDeadline) deadline else null)
        )

        Log.d("EventDebug", "New Event Added: $newEvent")

        Database.freeEventsCollection.add(newEvent)

        Log.d("EventDebug", "New Event Added: $newEvent")
        finish()

    }

}