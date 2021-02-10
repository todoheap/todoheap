package edu.rosehulman.todoheap.tasks.view.recycler

import androidx.recyclerview.widget.RecyclerView
import edu.rosehulman.todoheap.main.Controller
import edu.rosehulman.todoheap.tasks.controller.TaskCardController
import edu.rosehulman.todoheap.databinding.CardTaskBinding
import edu.rosehulman.todoheap.tasks.view.TaskCardViewModel

class TaskCardViewHolder(private val binding: CardTaskBinding) : RecyclerView.ViewHolder(binding.root) {
    fun bind(viewModel: TaskCardViewModel, controller: Controller) {
        binding.viewModel = viewModel
        binding.controller = TaskCardController(controller) {adapterPosition}
    }
}