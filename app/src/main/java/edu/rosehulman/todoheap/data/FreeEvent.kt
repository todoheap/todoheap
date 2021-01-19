package edu.rosehulman.todoheap.data

import java.util.*

data class FreeEvent(val name: String,
                    val isOneSitting: Boolean,
                    val priority: Int,
                    val procrastination: Int,
                    val enjoyability: Int,
                     val workload: Int,
                    val location: String? = null,
                    val deadline: Date? = null)