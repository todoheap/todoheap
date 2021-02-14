package edu.rosehulman.todoheap.tasks.model

import android.util.Log
import com.google.firebase.Timestamp
import com.google.firebase.firestore.DocumentChange
import com.google.firebase.firestore.ListenerRegistration
import edu.rosehulman.todoheap.Constants
import edu.rosehulman.todoheap.data.Database
import edu.rosehulman.todoheap.common.model.FreeEvent
import edu.rosehulman.todoheap.common.model.ScheduledEvent
import edu.rosehulman.todoheap.common.view.RecyclerViewModelProvider
import edu.rosehulman.todoheap.data.TimestampUtil
import edu.rosehulman.todoheap.tasks.view.recycler.CalendarCardAdapter
import edu.rosehulman.todoheap.tasks.view.TaskCardViewModel
import edu.rosehulman.todoheap.tasks.view.recycler.TaskCardAdapter

class TaskPageModel: RecyclerViewModelProvider<TaskCardViewModel> {
    var recyclerAdapter: TaskCardAdapter? = null
    private val taskList = ArrayList<FreeEvent>()
    private var freeEventsListenerRegistration: ListenerRegistration?=null

    private var freeHours = 0.0

    fun init(){
        freeEventsListenerRegistration?.remove()
        taskList.clear()
        recyclerAdapter?.notifyDataSetChanged()
        reloadParameters {
            initDBListener()
        }
    }

    private fun initDBListener(){
        freeEventsListenerRegistration?.remove()
        freeEventsListenerRegistration = Database.freeEventsCollection?.addSnapshotListener { value, error ->
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
        val isE1Doable = e1.isDoable(freeHours)
        val isE2Doable = e2.isDoable(freeHours)
        val now = Timestamp.now()
        if(isE1Doable && isE2Doable){
            //both are doable, check based on due, priority, procastination & enjoyability
            //past dues are always scheduled first
            val isE1PastDue = e1.isPastDue(now)
            val isE2PastDue = e2.isPastDue(now)
            if(isE1PastDue != isE2PastDue){
                //one is past due, schedule past due first
                return if(isE1PastDue) -1 else 1
            }else{
                //both are not due or past due
                // if one has deadline, schedule it first
                val e1HasDeadline = e1.deadline!=null
                val e2HasDeadline = e2.deadline!=null
                if(e1HasDeadline!=e2HasDeadline){
                    return if(e1HasDeadline)-1 else 1
                }
                // both have deadline, and are not due or past due
                // base on time until due priority, procrast and enjoyability

                //priority value lower = more important
                //procrastination value lower = less procrastination (schedule sooner)
                // read weights from settings, weights are from 1 to -1, 0 means neutral
                val dueTimeWeight = 0.5
                val priorityWeight = 0.5
                val procrastinationWeight = 0.5
                val enjoyabilityWeight = 0.5
                val workloadWeight = 0.5

                val e1Value = e1.value(now,dueTimeWeight,priorityWeight,enjoyabilityWeight,procrastinationWeight,workloadWeight)
                val e2Value = e2.value(now,dueTimeWeight,priorityWeight,enjoyabilityWeight,procrastinationWeight,workloadWeight)
                return if (e1Value - e2Value<0) -1 else 1
            }
        }else{
            if(isE1Doable){
                return -1
            }else if(isE2Doable){
                return 1
            }
            return 0
        }

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

    private fun reloadParameters(callback: ()->Unit) {
        val now = Timestamp.now()
        var freeSeconds = 0L;
        Database.scheduledEventsCollection?.whereGreaterThanOrEqualTo("endTime",now)?.get()?.addOnSuccessListener {
            val value = it ?: return@addOnSuccessListener
            for(document in value.documents){
                val scheduledEvent = ScheduledEvent.fromSnapshot(document) ?: continue
                if(TimestampUtil.compare(scheduledEvent.startTime, now)<0){
                    //start before, check if event time is after now
                    val startTimeOnly = TimestampUtil.convertToSameTimeOfDay(scheduledEvent.startTime, now)
                    if(TimestampUtil.compare(startTimeOnly, now)>=0){
                        //event starts after now, check if it repeats on today
                        val dayOfWeekIndex = TimestampUtil.calendarDayOfWeekToIndex(TimestampUtil.getDayOfWeek(now))
                        if(scheduledEvent.isRepeatingOn(dayOfWeekIndex)){
                            val currentFreeSeconds = startTimeOnly.seconds - now.seconds
                            freeSeconds = Math.min(freeSeconds, currentFreeSeconds)
                        }
                    }
                }else{
                    //starts after now
                    val currentFreeSeconds = scheduledEvent.startTime.seconds - now.seconds
                    freeSeconds = Math.min(freeSeconds, currentFreeSeconds)
                }
            }
            freeHours = freeSeconds / 3600.0
            callback()
        }
    }



}