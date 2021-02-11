package edu.rosehulman.todoheap.main

import edu.rosehulman.todoheap.calendar.model.CalendarPageModel
import edu.rosehulman.todoheap.tasks.model.TaskPageModel

/**
 * Main model for the app
 */
class App(
    val taskPageModel: TaskPageModel,
    val calendarPageModel: CalendarPageModel,
) {
    fun init(){
        taskPageModel.init()
        calendarPageModel.init()
    }
}