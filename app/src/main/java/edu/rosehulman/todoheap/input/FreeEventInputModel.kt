package edu.rosehulman.todoheap.input

import com.google.firebase.Timestamp
import edu.rosehulman.todoheap.R
import edu.rosehulman.todoheap.model.FreeEvent
import java.lang.Double
import java.util.*

data class FreeEventInputModel(

    var eventName: String = "",
    var location: String = "",
    var year: Int = 1970,
    var month: Int = 0,
    var dayOfMonth: Int = 1,
    var hour: Int = 0,
    var minute: Int = 0,
    var noDeadline: Boolean = false,
    var workLoad: String = "",
    var oneSitting: Boolean = false,
    var priority: Int = 1,
    var procrastination: Int = 1,
    var enjoyability: Int = 1
) {


    val deadlineTimestamp get () = Timestamp(Calendar.Builder().setDate(year,month,dayOfMonth).setTimeOfDay(hour, minute, 0).build().time)
    val pm get() = hour>=12
    val hourIn12 get() = if (hour==0 || hour==12) 12 else (hour % 12)

    fun setDeadlineToCurrentTime(){
        val now = Calendar.getInstance()
        year = now.get(Calendar.YEAR)
        dayOfMonth = now.get(Calendar.DAY_OF_MONTH)
        month = now.get(Calendar.MONTH)
        hour = now.get(Calendar.HOUR_OF_DAY)
        minute = now.get(Calendar.MINUTE)
    }

    fun copyFromEvent(event: FreeEvent){
        eventName = event.name
        location = event.location?:""
        val deadline = event.deadline
        if(deadline!=null){
            Calendar.Builder().setInstant(deadline.toDate()).build().let {
                year = it.get(Calendar.YEAR)
                dayOfMonth = it.get(Calendar.DAY_OF_MONTH)
                month = it.get(Calendar.MONTH)
                hour = it.get(Calendar.HOUR_OF_DAY)
                minute = it.get(Calendar.MINUTE)
            }
        }

        noDeadline = event.deadline==null
        workLoad = event.workload.toString()
        oneSitting = event.isOneSitting
        priority = event.priority
        procrastination = event.procrastination
        enjoyability = event.enjoyability
    }

    fun toEvent() = FreeEvent(
        name = eventName,
        isOneSitting = oneSitting,
        priority = priority,
        procrastination = procrastination,
        enjoyability = enjoyability,
        workload = Double.parseDouble(workLoad),
        location = location,
        deadline = if (noDeadline) null else deadlineTimestamp
    )

    fun validateInput(): Int?{
        if(eventName=="") return R.string.error_event_name_empty
        if(workLoad=="") return R.string.error_work_load_empty
        return null
    }
}