package edu.rosehulman.todoheap.input

import com.google.firebase.Timestamp
import edu.rosehulman.todoheap.model.FreeEvent
import java.lang.Double
import java.util.*

data class FreeEventInputViewModel(
        var eventName: String = "",
        var location: String = "",
        var year: Int,
        var month: Int,
        var dayOfMonth: Int,
        var hour: Int,
        var minute: Int,
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
}