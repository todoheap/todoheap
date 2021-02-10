package edu.rosehulman.todoheap.tasks.controller

import edu.rosehulman.todoheap.main.Controller

class TaskCardController(val controller: Controller, val position: () -> Int) {

    fun onClick() {
        controller.editFreeEvent(position())
    }

}