package edu.rosehulman.todoheap.view.tasks.recycler

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import edu.rosehulman.todoheap.databinding.CardTaskBinding
import edu.rosehulman.todoheap.view.tasks.viewmodel.TaskCardViewModel

class TaskCardViewHolder(private val binding: CardTaskBinding) : RecyclerView.ViewHolder(binding.root) {
    fun bind(viewModel: TaskCardViewModel) {
        binding.viewModel = viewModel
    }
}