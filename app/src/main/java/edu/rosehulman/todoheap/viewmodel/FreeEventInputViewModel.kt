package edu.rosehulman.todoheap.viewmodel

import com.google.firebase.Timestamp
import com.google.type.DateTime
import edu.rosehulman.todoheap.R
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
        var oneSitting: Int = R.id.is_one_sitting_false,
        var procrastination: Int = 0,
        var enjoyability: Int = 0
) {
        val deadlineTimestamp get () = Timestamp(Calendar.Builder().setDate(year,month,dayOfMonth).setTimeOfDay(hour, minute, 0).build().time)
        val pm get() = hour>=12
        val hourIn12 get() = (hour % 12).let{if (it==0) 12 else it}
}