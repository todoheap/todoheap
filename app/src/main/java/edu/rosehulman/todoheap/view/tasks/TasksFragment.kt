package edu.rosehulman.todoheap.view.tasks

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import edu.rosehulman.todoheap.MainActivity
import edu.rosehulman.todoheap.R
import edu.rosehulman.todoheap.databinding.FragmentTasksBinding
import edu.rosehulman.todoheap.view.RecyclerViewModelProvider
import edu.rosehulman.todoheap.view.tasks.recycler.TaskCardAdapter
import edu.rosehulman.todoheap.view.tasks.viewmodel.TaskCardViewModel

class TasksFragment : Fragment() {

    private lateinit var activity: MainActivity

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        val binding = DataBindingUtil.inflate<FragmentTasksBinding>(inflater,R.layout.fragment_tasks,container,false)
        activity = requireContext() as MainActivity
        //setup recycler
        binding.recyclerTasks.let{
            it.layoutManager = LinearLayoutManager(activity)
            val taskCardAdapter = TaskCardAdapter(activity.app.taskPageModel, activity)
            activity.app.taskPageModel.recyclerAdapter = taskCardAdapter
            it.adapter = taskCardAdapter
            it.setHasFixedSize(true)
        }

        return binding.root
    }
}