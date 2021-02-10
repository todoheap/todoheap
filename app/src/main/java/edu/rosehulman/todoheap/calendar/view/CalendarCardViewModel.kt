package edu.rosehulman.todoheap.calendar.view

data class CalendarCardViewModel(
        val hour: Int?,
        val title: String,
        val subText: String,
) {
    val pm get() = hour != null && hour>=12
}