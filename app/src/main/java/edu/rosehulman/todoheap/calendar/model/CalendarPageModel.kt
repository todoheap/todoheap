package edu.rosehulman.todoheap.calendar.model

import android.util.Log
import com.google.firebase.Timestamp
import com.google.firebase.firestore.DocumentChange
import com.google.firebase.firestore.ListenerRegistration
import com.google.firebase.firestore.Query
import edu.rosehulman.todoheap.Constants
import edu.rosehulman.todoheap.calendar.view.CalendarCardViewModel
import edu.rosehulman.todoheap.data.Database
import edu.rosehulman.todoheap.common.model.FreeEvent
import edu.rosehulman.todoheap.common.model.ScheduledEvent
import edu.rosehulman.todoheap.common.view.RecyclerViewModelProvider
import edu.rosehulman.todoheap.tasks.view.recycler.CalendarCardAdapter
import edu.rosehulman.todoheap.tasks.view.TaskCardViewModel
import java.util.*
import kotlin.collections.ArrayList

class CalendarPageModel: RecyclerViewModelProvider<CalendarCardViewModel> {
    var recyclerAdapter: CalendarCardAdapter? = null
    private lateinit var selectedDayTimestamp: Timestamp
    private lateinit var nextDayOfSelectedDayTimestamp: Timestamp
    init {
        val now = Calendar.getInstance()
        val year = now.get(Calendar.YEAR)
        val month = now.get(Calendar.MONTH)
        val day = now.get(Calendar.DAY_OF_MONTH)
        selectDay(year,month, day)
    }

    fun selectDay(year: Int, month: Int, day: Int){
        selectedDayTimestamp = Timestamp(Calendar.Builder().setDate(year,month,day).setTimeOfDay(0, 0, 0).build().time)
        nextDayOfSelectedDayTimestamp = Timestamp(Calendar.Builder().setDate(year,month,day+1).setTimeOfDay(0, 0, 0).build().time)
        init()
    }

    private val eventList = ArrayList<ScheduledEvent>()
    private var listenerRegistration: ListenerRegistration?=null


    fun init(){
        listenerRegistration?.remove()
        eventList.clear()
        recyclerAdapter?.notifyDataSetChanged()
        initDBListener()
    }

    private fun initDBListener(){
        listenerRegistration?.remove()
        Database.scheduledEventsCollection
            .whereGreaterThanOrEqualTo("startTime",selectedDayTimestamp)
            .whereLessThanOrEqualTo("startTime",nextDayOfSelectedDayTimestamp)
            .orderBy("startTime",Query.Direction.ASCENDING)
            .addSnapshotListener { value, error ->
                if(error!=null) {
                    Log.e(Constants.TAG, "Error in CalendarPageModel: $error")
                    return@addSnapshotListener
                }
                val snapshot = value ?: return@addSnapshotListener
                for(docChange in snapshot.documentChanges) {
                    val event = FreeEvent.fromSnapshot(docChange.document)
                    when(docChange.type){
                        DocumentChange.Type.ADDED -> {
                            //val index = insertEventIntoSortedList(event)
                            //recyclerAdapter?.notifyItemInserted(index)
                        }
                        DocumentChange.Type.REMOVED -> {
                            eventList.indexOfFirst { it.id == event.id }.let {
                                eventList.removeAt(it)
                                recyclerAdapter?.notifyItemRemoved(it)
                            }
                        }
                        DocumentChange.Type.MODIFIED -> {
                            eventList.indexOfFirst { it.id == event.id }.let {
                                eventList.removeAt(it)
                                //val newIndex = insertEventIntoSortedList(event)
                                //if(newIndex!=it){
                                //    recyclerAdapter?.notifyItemRemoved(it)
                                //    recyclerAdapter?.notifyItemInserted(newIndex)
                               // }else{
                               //     recyclerAdapter?.notifyItemChanged(it)
                               // }
    //
    //                            taskList[it] = event
    //                            recyclerAdapter?.notifyItemChanged(it)

                            }
                        }
                    }
                }
        }
    }

    override fun getViewModel(position: Int): CalendarCardViewModel {
        //TODO: Set proper sub text
        return CalendarCardViewModel(0,"","")
    }

    operator fun get(position: Int) = eventList[position]

    override val size: Int
        get() = eventList.size

//    private fun compareEvents(e1: FreeEvent, e2: FreeEvent): Int {
//        //algorithm is for now simple rank by procrastination
//        return e1.procrastination - e2.procrastination;
//    }
//
//    private fun insertEventIntoSortedList(e: FreeEvent): Int=
//        eventList.indexOfFirst { compareEvents(it,e)>0 }.let {
//            if(it>=0){
//                eventList.add(it, e)
//                it
//            }else{
//                eventList.add(e)
//                eventList.size-1
//            }
//
//        }



}