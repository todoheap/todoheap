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
import edu.rosehulman.todoheap.data.TimestampUtil
import edu.rosehulman.todoheap.tasks.view.recycler.CalendarCardAdapter
import edu.rosehulman.todoheap.tasks.view.TaskCardViewModel
import java.util.*
import kotlin.collections.ArrayList

class CalendarPageModel: RecyclerViewModelProvider<CalendarCardViewModel> {
    var recyclerAdapter: CalendarCardAdapter? = null
    private lateinit var selectedDayTimestamp: Timestamp
    private lateinit var nextDayOfSelectedDayTimestamp: Timestamp
    private val eventList = ArrayList<ScheduledEvent>()
    private var listenerRegistration: ListenerRegistration?=null
    init {
        val now = Calendar.getInstance()
        val year = now.get(Calendar.YEAR)
        val month = now.get(Calendar.MONTH)
        val day = now.get(Calendar.DAY_OF_MONTH)
        selectDay(year,month, day)
    }

    fun selectDay(year: Int, month: Int, day: Int){
        selectedDayTimestamp = TimestampUtil.toTimestamp(year,month, day);//Timestamp(Calendar.Builder().setDate(year,month,day).setTimeOfDay(0, 0, 0).build().time)
        nextDayOfSelectedDayTimestamp = TimestampUtil.toTimestamp(year,month, day+1);
        init()
    }

    fun init(){
        listenerRegistration?.remove()
        eventList.clear()
        recyclerAdapter?.notifyDataSetChanged()
        initDBListener()
    }

    private fun initDBListener(){
        listenerRegistration?.remove()
        listenerRegistration = Database.scheduledEventsCollection
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
                    val event = ScheduledEvent.fromSnapshot(docChange.document)
                    Log.d(Constants.TAG,"notified ${event.id}")
                    when(docChange.type){
                        DocumentChange.Type.ADDED -> {
                            Log.d(Constants.TAG,"inserting ${event.id}")
                            val index = insertEventIntoSortedList(event)
                            recyclerAdapter?.notifyItemInserted(index)
                        }
                        DocumentChange.Type.REMOVED -> {
                            Log.d(Constants.TAG,"removing ${event.id}")
                            eventList.indexOfFirst { it.id == event.id }.let {
                                eventList.removeAt(it)
                                recyclerAdapter?.notifyItemRemoved(it)
                            }
                        }
                        DocumentChange.Type.MODIFIED -> {
                            eventList.indexOfFirst { it.id == event.id }.let {
                                eventList.removeAt(it)
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

    override fun getViewModel(position: Int): CalendarCardViewModel {
        //TODO: Set proper sub text
        val event = eventList[position]
        val hour = TimestampUtil.getHour(event.startTime)
        val minute = TimestampUtil.getMinute(event.startTime)
        var showTime = true
        if(position>0){
            val previousHour = TimestampUtil.getHour(eventList[position-1].startTime)
            if(hour==previousHour) showTime = false
        }
        return CalendarCardViewModel(showTime, hour, minute,eventList[position].name,eventList[position].startTime.toString())
    }

    operator fun get(position: Int) = eventList[position]

    override val size: Int
        get() = eventList.size

    private fun compareEvents(e1: ScheduledEvent, e2: ScheduledEvent): Long {
        return e1.startTime.seconds - e2.startTime.seconds;
    }

    private fun insertEventIntoSortedList(e: ScheduledEvent): Int=
        eventList.indexOfFirst { compareEvents(it,e)>0 }.let {
            if(it>=0){
                Log.d(Constants.TAG,"scheduled event inserted $it")
                eventList.add(it, e)
                it
            }else{
                Log.d(Constants.TAG,"scheduled event inserted ${eventList.size}")
                eventList.add(e)
                eventList.size-1
            }

        }



}