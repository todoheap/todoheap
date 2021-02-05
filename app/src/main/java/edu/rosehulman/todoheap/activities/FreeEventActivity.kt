package edu.rosehulman.todoheap.activities

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import edu.rosehulman.todoheap.Constants
import edu.rosehulman.todoheap.R
import edu.rosehulman.todoheap.controller.FreeEventController
import edu.rosehulman.todoheap.databinding.ActivityFreeEventBinding
import edu.rosehulman.todoheap.model.FreeEvent
import edu.rosehulman.todoheap.viewmodel.FreeEventInputViewModel
import java.util.*

class FreeEventActivity: AppCompatActivity() {

    lateinit var binding: ActivityFreeEventBinding
    lateinit var controller: FreeEventController
    lateinit var model: FreeEventInputViewModel

    private var enjoyability = 0
    private var procrastination = 0
    //private var deadline: Date = Calendar.getInstance().time

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_free_event)
        controller = FreeEventController(this)
        binding.controller = controller

        val now = Calendar.getInstance();
        model = FreeEventInputViewModel(
            year = now.get(Calendar.YEAR),
            dayOfMonth = now.get(Calendar.DAY_OF_MONTH),
            month = now.get(Calendar.MONTH),
            hour = now.get(Calendar.HOUR_OF_DAY),
            minute = now.get(Calendar.MINUTE))
        binding.model = model
        initFields()

        setSupportActionBar(binding.toolbar)
        supportActionBar?.setHomeAsUpIndicator(R.drawable.ic_baseline_close_24)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }

    private fun initFields() {
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

        binding.enjoyableSpinner.onItemSelectedListener = object: AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                enjoyability = position
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
                //Do nothing
            }

        }

        binding.procrastinationSpinner.onItemSelectedListener = object: AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                procrastination = position
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
                //Do nothing
            }

        }

//        findViewById<DatePicker>(R.id.date_picker).setOnDateChangedListener { _, year, monthOfYear, dayOfMonth ->
//            deadline = Date(year, monthOfYear, dayOfMonth)
//            Timestamp(deadline)
//        }

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
        val nameEditText = findViewById<EditText>(R.id.editTextEventName)
        val locationEditText = findViewById<EditText>(R.id.editTextLocation)
        //val hasDeadline = !findViewById<CheckBox>(R.id.deadline_checkbox).isChecked
        val workloadEditText = findViewById<EditText>(R.id.workload_edit_text)
        val isMultipleSessonsTrue = findViewById<RadioButton>(R.id.is_one_sitting_true).isSelected
        val isMultipleSessionsFalse = findViewById<RadioButton>(R.id.is_one_sitting_false).isSelected
        val newEvent = FreeEvent(
            nameEditText.text.toString(),
            isMultipleSessonsTrue && !isMultipleSessionsFalse,
            0, procrastination, enjoyability,
            java.lang.Double.parseDouble(workloadEditText.text.toString()),
            locationEditText.text.toString(),
            if (model.noDeadline) null else model.deadlineTimestamp
        )

        Log.d("EventDebug", "New Event Added: $newEvent")

        val id = intent.getStringExtra(Constants.KEY_FREE_EVENT_ID)
        //Database.freeEventsCollection.add(newEvent)

        val returnIntent = Intent().putExtra(Constants.KEY_FREE_EVENT, newEvent)
        if(id!=null) returnIntent.putExtra(Constants.KEY_FREE_EVENT_ID,id)

        setResult(Constants.RESULT_GOOD, returnIntent)
        finish()

    }

}