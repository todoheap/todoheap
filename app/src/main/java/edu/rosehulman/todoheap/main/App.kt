package edu.rosehulman.todoheap.main

import edu.rosehulman.todoheap.tasks.model.TaskPageModel

/**
 * Main model for the app
 */
class App(
    val taskPageModel: TaskPageModel
) {
    fun init(){
                taskPageModel.init();
        }
}