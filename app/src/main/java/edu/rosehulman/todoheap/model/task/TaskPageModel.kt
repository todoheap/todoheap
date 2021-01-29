package edu.rosehulman.todoheap.model.task

import android.util.Log
import com.google.firebase.firestore.DocumentChange
import edu.rosehulman.todoheap.Constants
import edu.rosehulman.todoheap.data.Database
import edu.rosehulman.todoheap.model.FreeEvent
import edu.rosehulman.todoheap.view.RecyclerViewModelProvider
import edu.rosehulman.todoheap.view.tasks.recycler.TaskCardAdapter
import edu.rosehulman.todoheap.view.tasks.viewmodel.TaskCardViewModel

class TaskPageModel: RecyclerViewModelProvider<TaskCardViewModel> {
    var recyclerAdapter: TaskCardAdapter? = null
    //TODO: Keep the list sorted??
    private val taskList = ArrayList<FreeEvent>()

    init {
        Database.eventsCollection.addSnapshotListener { value, error ->
            if(error!=null) {
                Log.e(Constants.TAG, "Error in TaskPageModel: $error")
                return@addSnapshotListener
            }
            val snapshot = value ?: return@addSnapshotListener
            for(docChange in snapshot.documentChanges) {
                val event = FreeEvent.fromSnapshot(docChange.document)
                when(docChange.type){
                    DocumentChange.Type.ADDED -> {
                        taskList.add(event)
                        recyclerAdapter?.notifyItemInserted(taskList.size - 1)
                    }
                    DocumentChange.Type.REMOVED -> {
                        taskList.indexOfFirst { it.id == event.id }.let {
                            taskList.removeAt(it)
                            recyclerAdapter?.notifyItemRemoved(it)
                        }
                    }
                    DocumentChange.Type.MODIFIED -> {
                        taskList.indexOfFirst { it.id == event.id }.let {
                            taskList[it] = event
                            recyclerAdapter?.notifyItemChanged(it)
                        }
                    }
                }
            }
        }
    }

    override fun get(position: Int): TaskCardViewModel {
        //TODO: Set proper sub text
        return TaskCardViewModel(taskList[position].name, taskList[position].toString())
    }

    override val size: Int
        get() = taskList.size
}