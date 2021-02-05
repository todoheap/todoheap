package edu.rosehulman.todoheap.view

interface RecyclerViewModelProvider<T> {
    operator fun get(position: Int): T

    val size: Int
}