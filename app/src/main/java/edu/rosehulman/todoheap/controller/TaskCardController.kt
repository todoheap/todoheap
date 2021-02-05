package edu.rosehulman.todoheap.controller

class TaskCardController(val controller: Controller, val position: () -> Int) {

    fun onClick() {
        controller.editFreeEvent(position())
    }

}