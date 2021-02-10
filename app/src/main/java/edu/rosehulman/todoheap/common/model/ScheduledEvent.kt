package edu.rosehulman.todoheap.common.model

import com.google.firebase.firestore.Exclude
import kotlinx.parcelize.IgnoredOnParcel
import java.util.*
import kotlin.collections.ArrayList

data class ScheduledEvent(
    val name: String,
    val startTime: Date,
    val endTime: Date,
    val isRepeat: Boolean,
    val daysRepeating: ArrayList<Boolean>,
    val location: String? = null){
    @IgnoredOnParcel
    @get: Exclude
    var id: String? = null
}