package edu.rosehulman.todoheap.data

import com.google.firebase.Timestamp
import java.util.*

object TimestampUtil {
    @JvmStatic
    fun toTimestamp(calendar: Calendar) = Timestamp(calendar.time)
    @JvmStatic
    fun toTimestamp(year: Int, month: Int, day: Int) = TimestampUtil.toTimestamp(year,month,day,0,0,0)
    @JvmStatic
    fun toTimestamp(year: Int, month: Int, day: Int, hour: Int, minute: Int, second: Int) =  Timestamp(Calendar.Builder().setDate(year,month,day).setTimeOfDay(hour, minute, second).build().time)
    @JvmStatic
    fun getYear(timestamp: Timestamp) = toCalendar(timestamp).get(Calendar.YEAR)
    @JvmStatic
    fun getMonth(timestamp: Timestamp) = toCalendar(timestamp).get(Calendar.MONTH)
    @JvmStatic
    fun getDay(timestamp: Timestamp) = toCalendar(timestamp).get(Calendar.DAY_OF_MONTH)
    @JvmStatic
    fun getHour(timestamp: Timestamp) = toCalendar(timestamp).get(Calendar.HOUR_OF_DAY)
    @JvmStatic
    fun getMinute(timestamp: Timestamp) = toCalendar(timestamp).get(Calendar.MINUTE)

    fun toCalendar(timestamp: Timestamp) = Calendar.Builder().setInstant(timestamp.seconds*1000).build()



}