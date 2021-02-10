package edu.rosehulman.todoheap.common.model

import java.util.*
import kotlin.collections.ArrayList

data class ScheduledEvent(
    val name: String,
    val startTime: Date,
    val endTime: Date,
    val isRepeat: Boolean,
    val daysRepeating: ArrayList<Boolean>,
    val location: String? = null)