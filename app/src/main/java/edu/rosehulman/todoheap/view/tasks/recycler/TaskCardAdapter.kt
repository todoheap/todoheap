package edu.rosehulman.todoheap.view.tasks.recycler

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import edu.rosehulman.todoheap.R
import edu.rosehulman.todoheap.databinding.CardTaskBinding
import edu.rosehulman.todoheap.view.RecyclerViewModelProvider
import edu.rosehulman.todoheap.view.tasks.viewmodel.TaskCardViewModel

class TaskCardAdapter(
        private val viewModelProvider: RecyclerViewModelProvider<TaskCardViewModel>,
        private val context: Context,
): RecyclerView.Adapter<TaskCardViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TaskCardViewHolder {
        val binding = DataBindingUtil.inflate<CardTaskBinding>(
                LayoutInflater.from(context),
                R.layout.card_task,
                parent,
                false
        )
        return TaskCardViewHolder(binding)
    }

    override fun onBindViewHolder(holder: TaskCardViewHolder, position: Int) {
        holder.bind(viewModelProvider[position])
    }

    override fun getItemCount() = viewModelProvider.size
}