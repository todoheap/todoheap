package edu.rosehulman.todoheap.viewmodel

import com.google.type.DateTime
import edu.rosehulman.todoheap.R
import java.util.*

data class FreeEventInputViewModel(
        var eventName: String = "",
        var location: String = "",
        var deadline: Date = Calendar.getInstance().time,
        var noDeadLine: Boolean = false,
        var workLoad: String = "",
        var oneSitting: Int = R.id.is_one_sitting_false,
        var procrastination: Int = 0,
        var enjoyability: Int = 0
) {
}