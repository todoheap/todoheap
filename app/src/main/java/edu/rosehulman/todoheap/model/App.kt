package edu.rosehulman.todoheap.model

import edu.rosehulman.todoheap.model.task.TaskPageModel

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