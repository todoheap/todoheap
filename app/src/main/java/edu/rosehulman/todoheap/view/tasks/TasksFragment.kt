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
            it.layoutManager = LinearLayoutManager(requireContext())
            //TODO: Replace provider with getting from the model
            val provider = object: RecyclerViewModelProvider<TaskCardViewModel>{
                override fun get(position: Int) = TaskCardViewModel("","")
                override val size: Int = 0
            }
            it.adapter = TaskCardAdapter(provider, requireContext())
            it.setHasFixedSize(true)
        }

        return binding.root
    }
}