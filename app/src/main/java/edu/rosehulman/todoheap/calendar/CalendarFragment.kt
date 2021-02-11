package edu.rosehulman.todoheap.calendar

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import edu.rosehulman.todoheap.R
import edu.rosehulman.todoheap.databinding.FragmentCalendarBinding
import edu.rosehulman.todoheap.main.MainActivity
import edu.rosehulman.todoheap.tasks.controller.TaskCardSwipeCallback
import edu.rosehulman.todoheap.tasks.view.recycler.CalendarCardAdapter

class CalendarFragment : Fragment() {
    private lateinit var activity: MainActivity
    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        val binding = DataBindingUtil.inflate<FragmentCalendarBinding>(inflater, R.layout.fragment_calendar, container, false)

        activity = requireContext() as MainActivity
        //setup recycler
        binding.recyclerCalendar.let{
            it.layoutManager = LinearLayoutManager(activity)
            val calendarCardAdapter = CalendarCardAdapter(activity.app.calendarPageModel, activity)
            activity.app.calendarPageModel.recyclerAdapter = calendarCardAdapter
            it.adapter = calendarCardAdapter
            it.setHasFixedSize(true)
        }

       // ItemTouchHelper(TaskCardSwipeCallback(activity.controller)).attachToRecyclerView(binding.recyclerCalendar)
        return binding.root
    }
}