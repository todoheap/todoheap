package edu.rosehulman.todoheap.activities

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import edu.rosehulman.todoheap.Constants
import edu.rosehulman.todoheap.R
import edu.rosehulman.todoheap.controller.FreeEventController
import edu.rosehulman.todoheap.databinding.ActivityFreeEventBinding
import edu.rosehulman.todoheap.model.FreeEvent
import edu.rosehulman.todoheap.input.FreeEventInputViewModel
import java.util.*

class FreeEventActivity: AppCompatActivity() {

    lateinit var binding: ActivityFreeEventBinding
    lateinit var controller: FreeEventController
    lateinit var model: FreeEventInputViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_free_event)
        controller = FreeEventController(this)
        binding.controller = controller

        initModel()
        initFields()

        setSupportActionBar(binding.toolbar)
        supportActionBar?.setHomeAsUpIndicator(R.drawable.ic_baseline_close_24)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }

    private fun initModel(){
        val now = Calendar.getInstance();
        model = FreeEventInputViewModel(
            year = now.get(Calendar.YEAR),
            dayOfMonth = now.get(Calendar.DAY_OF_MONTH),
            month = now.get(Calendar.MONTH),
            hour = now.get(Calendar.HOUR_OF_DAY),
            minute = now.get(Calendar.MINUTE))
        binding.model = model
    }


    private fun initFields() {
        initSpinner(binding.enjoyableSpinner, R.array.enjoyability_options)
        initSpinner(binding.procrastinationSpinner, R.array.procrastination_options)

    }

    private fun initSpinner(spinner: Spinner, resId: Int){
        ArrayAdapter.createFromResource(
            this,
            resId,
            android.R.layout.simple_spinner_item
        ).also { adapter ->
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            spinner.adapter = adapter
        }
    }


    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_add_free_event, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when(item.itemId){
            android.R.id.home -> {
                finish()
                true
            }
            R.id.action_save -> {
                save()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    fun save() {
        val newEvent = model.toEvent()

        Log.d("EventDebug", "New Event Added: $newEvent")

        val id = intent.getStringExtra(Constants.KEY_FREE_EVENT_ID)

        val returnIntent = Intent().putExtra(Constants.KEY_FREE_EVENT, newEvent)
        if(id!=null) returnIntent.putExtra(Constants.KEY_FREE_EVENT_ID,id)

        setResult(Constants.RESULT_GOOD, returnIntent)
        finish()

    }

}