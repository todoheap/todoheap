package edu.rosehulman.todoheap.scheduledevent.controller

import edu.rosehulman.todoheap.common.ControllerBase
import edu.rosehulman.todoheap.scheduledevent.ScheduledEventActivity

class ScheduledEventController(
        val activity: ScheduledEventActivity
):ControllerBase(activity) {
    fun selectStartDate(){}
    fun selectStartTime(){}
    fun selectEndDate(){}
    fun selectEndTime(){}
}