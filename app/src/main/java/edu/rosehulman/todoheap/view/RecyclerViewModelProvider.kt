package edu.rosehulman.todoheap.view

interface RecyclerViewModelProvider<T> {
    fun getViewModel(position: Int): T

    val size: Int
}