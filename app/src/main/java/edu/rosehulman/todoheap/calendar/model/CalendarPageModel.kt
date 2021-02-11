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
import edu.rosehulman.todoheap.databinding.FragmentCalendarBinding
import edu.rosehulman.todoheap.tasks.view.recycler.CalendarCardAdapter
import edu.rosehulman.todoheap.tasks.view.TaskCardViewModel
import java.util.*
import kotlin.collections.ArrayList

class CalendarPageModel: RecyclerViewModelProvider<CalendarCardViewModel> {
    var recyclerAdapter: CalendarCardAdapter? = null
    var binding: FragmentCalendarBinding?=null
    private lateinit var selectedDayTimestamp: Timestamp
    val selectedYear  get() = TimestampUtil.getYear(selectedDayTimestamp)
    val selectedMonth  get() = TimestampUtil.getMonth(selectedDayTimestamp)
    val selectedDay  get() = TimestampUtil.getDay(selectedDayTimestamp)
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
        binding?.model = this
        init()
    }

    fun previousDay(){
        TimestampUtil.decomposeFields(selectedDayTimestamp) { year, month, day, _, _, _ ->
            selectDay(year,month,day-1)
        }
    }

    fun nextDay(){
        TimestampUtil.decomposeFields(selectedDayTimestamp) { year, month, day, _, _, _ ->
            selectDay(year,month,day+1)
        }
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
                    when(docChange.type){
                        DocumentChange.Type.ADDED -> {
                            val index = insertEventIntoSortedList(event)
                            recyclerAdapter?.notifyItemInserted(index)
                            notifyShowTimeChangeWhenInsertedAt(index)
                        }
                        DocumentChange.Type.REMOVED -> {
                            eventList.indexOfFirst { it.id == event.id }.let {
                                eventList.removeAt(it)
                                recyclerAdapter?.notifyItemRemoved(it)
                                notifyShowTimeChangeWhenRemovedAt(it)
                            }
                        }
                        DocumentChange.Type.MODIFIED -> {
                            eventList.indexOfFirst { it.id == event.id }.let {

                                eventList.removeAt(it)
                                val newIndex = insertEventIntoSortedList(event)
                                if(newIndex!=it){
                                    recyclerAdapter?.notifyItemRemoved(it)
                                    notifyShowTimeChangeWhenRemovedAt(it)
                                    recyclerAdapter?.notifyItemInserted(newIndex)
                                    notifyShowTimeChangeWhenInsertedAt(newIndex)
                                }else{
                                    recyclerAdapter?.notifyItemChanged(it)
                                    if(it<eventList.size-1){
                                        recyclerAdapter?.notifyItemChanged(it+1)
                                    }
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

    private fun notifyShowTimeChangeWhenInsertedAt(position: Int){
        if(position<eventList.size-1){
            val hour = TimestampUtil.getHour(eventList[position].startTime)
            val nextHour = TimestampUtil.getHour(eventList[position+1].startTime)
            if(hour==nextHour){
                recyclerAdapter?.notifyItemChanged(position+1)
            }
        }
    }

    private fun notifyShowTimeChangeWhenRemovedAt(position: Int){
        if(position==0){
            recyclerAdapter?.notifyItemChanged(0)
        }else if (position<eventList.size){
            val hour = TimestampUtil.getHour(eventList[position].startTime)
            val prevHour = TimestampUtil.getHour(eventList[position-1].startTime)
            if(hour!=prevHour){
                recyclerAdapter?.notifyItemChanged(position)
            }
        }
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
                eventList.add(it, e)
                it
            }else{
                eventList.add(e)
                eventList.size-1
            }

        }



}