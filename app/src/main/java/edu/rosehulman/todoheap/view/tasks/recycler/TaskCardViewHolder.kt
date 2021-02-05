package edu.rosehulman.todoheap.view.tasks.recycler

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.google.android.gms.tasks.Task
import edu.rosehulman.todoheap.controller.Controller
import edu.rosehulman.todoheap.controller.TaskCardController
import edu.rosehulman.todoheap.databinding.CardTaskBinding
import edu.rosehulman.todoheap.view.tasks.viewmodel.TaskCardViewModel

class TaskCardViewHolder(private val binding: CardTaskBinding) : RecyclerView.ViewHolder(binding.root) {
    fun bind(viewModel: TaskCardViewModel, controller: Controller) {
        binding.viewModel = viewModel
        binding.controller = TaskCardController(controller) {adapterPosition}
    }
}