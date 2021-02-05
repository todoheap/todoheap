package edu.rosehulman.todoheap.model.task

import android.util.Log
import com.google.firebase.firestore.DocumentChange
import com.google.firebase.firestore.ListenerRegistration
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
    private var freeEventsListenerRegistration: ListenerRegistration?=null


    fun init(){
        //Log.d(Constants.TAG, "Hello World\n \n \n \n \n \n \n sp ac e" )
        freeEventsListenerRegistration?.remove()
        taskList.clear()
        recyclerAdapter?.notifyDataSetChanged()
        initDBListener()
    }

    private fun initDBListener(){
        freeEventsListenerRegistration?.remove()
        Database.freeEventsCollection.addSnapshotListener { value, error ->
            if(error!=null) {
                Log.e(Constants.TAG, "Error in TaskPageModel: $error")
                return@addSnapshotListener
            }
            val snapshot = value ?: return@addSnapshotListener
            for(docChange in snapshot.documentChanges) {
                val event = FreeEvent.fromSnapshot(docChange.document)
                when(docChange.type){
                    DocumentChange.Type.ADDED -> {
                        val index = insertEventIntoSortedList(event)
                        recyclerAdapter?.notifyItemInserted(index)
                    }
                    DocumentChange.Type.REMOVED -> {
                        taskList.indexOfFirst { it.id == event.id }.let {
                            taskList.removeAt(it)
                            recyclerAdapter?.notifyItemRemoved(it)
                        }
                    }
                    DocumentChange.Type.MODIFIED -> {
                        taskList.indexOfFirst { it.id == event.id }.let {
                            taskList.removeAt(it)
                            val newIndex = insertEventIntoSortedList(event)
                            if(newIndex!=it){
                                recyclerAdapter?.notifyItemRemoved(it)
                                recyclerAdapter?.notifyItemInserted(newIndex)
                            }else{
                                recyclerAdapter?.notifyItemChanged(it)
                            }
//
//                            taskList[it] = event
//                            recyclerAdapter?.notifyItemChanged(it)

                        }
                    }
                }
            }
        }
    }

    override fun getViewModel(position: Int): TaskCardViewModel {
        //TODO: Set proper sub text
        return TaskCardViewModel(taskList[position].name, taskList[position].procrastination.toString())
    }

    operator fun get(position: Int) = taskList[position]

    override val size: Int
        get() = taskList.size

    private fun compareEvents(e1: FreeEvent, e2: FreeEvent): Int {
        //algorithm is for now simple rank by procrastination
        return e1.procrastination - e2.procrastination;
    }

    private fun insertEventIntoSortedList(e: FreeEvent): Int=
        taskList.indexOfFirst { compareEvents(it,e)>0 }.let {
            if(it>=0){
                taskList.add(it, e)
                it
            }else{
                taskList.add(e)
                taskList.size-1
            }

        }



}