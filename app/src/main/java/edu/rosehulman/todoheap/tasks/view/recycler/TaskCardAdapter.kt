package edu.rosehulman.todoheap.tasks.view.recycler

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import edu.rosehulman.todoheap.R
import edu.rosehulman.todoheap.main.MainActivity
import edu.rosehulman.todoheap.databinding.CardTaskBinding
import edu.rosehulman.todoheap.common.view.RecyclerViewModelProvider
import edu.rosehulman.todoheap.tasks.view.TaskCardViewModel

class TaskCardAdapter(
        private val viewModelProvider: RecyclerViewModelProvider<TaskCardViewModel>,
        private val activity: MainActivity,
): RecyclerView.Adapter<CalendarCardViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CalendarCardViewHolder {
        val binding = DataBindingUtil.inflate<CardTaskBinding>(
                LayoutInflater.from(activity),
                R.layout.card_task,
                parent,
                false
        )
        return CalendarCardViewHolder(binding)
    }

    override fun onBindViewHolder(holder: CalendarCardViewHolder, position: Int) {
        holder.bind(viewModelProvider.getViewModel(position),activity.controller)
    }

    override fun getItemCount() = viewModelProvider.size
}