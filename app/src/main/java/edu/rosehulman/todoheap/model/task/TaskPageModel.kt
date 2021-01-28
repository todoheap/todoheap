package edu.rosehulman.todoheap.model.task

import android.util.Log
import edu.rosehulman.todoheap.Constants
import edu.rosehulman.todoheap.data.Database
import edu.rosehulman.todoheap.model.FreeEvent
import edu.rosehulman.todoheap.view.RecyclerViewModelProvider
import edu.rosehulman.todoheap.view.tasks.viewmodel.TaskCardViewModel

class TaskPageModel: RecyclerViewModelProvider<TaskCardViewModel> {
    private val taskList = ArrayList<FreeEvent>()

    init {
        Database.eventsCollection.addSnapshotListener { value, error ->
            if(error!=null) {
                Log.e(Constants.TAG, "Error in TaskPageModel: $error")
                return@addSnapshotListener
            }
            //TODO: listen to collection changes
        }
    }

    override fun get(position: Int): TaskCardViewModel {
        //TODO: Set proper sub text
        return TaskCardViewModel(taskList[position].name, taskList[position].toString())
    }

    override val size: Int
        get() = taskList.size
}