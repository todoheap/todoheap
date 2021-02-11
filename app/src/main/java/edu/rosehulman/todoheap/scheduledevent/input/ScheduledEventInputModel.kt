package edu.rosehulman.todoheap.scheduledevent.input

data class ScheduledEventInputModel(
    var eventName: String = "",
    var location: String = "",
    var startYear: Int = 1970,
    var startMonth: Int = 0,
    var startDay: Int = 1,
    var startHour: Int = 0,
    var startMinute: Int = 0,
    var endYear: Int = 1970,
    var endMonth: Int = 0,
    var endDay: Int = 1,
    var endHour: Int = 0,
    var endMinute: Int = 0,
    var repeatMonday: Boolean = false,
    var repeatTuesday: Boolean = false,
    var repeatWednesday: Boolean = false,
    var repeatThursday: Boolean = false,
    var repeatFriday: Boolean = false,
    var repeatSaturday: Boolean = false,
    var repeatSunday: Boolean = false,
) {
    val startPm get() = startHour>=12
    val startHourIn12 get() = if (startHour==0 || startHour==12) 12 else (startHour % 12)
    val endPm get() = endHour>=12
    val endHourIn12 get() = if (endHour==0 || endHour==12) 12 else (endHour % 12)
}