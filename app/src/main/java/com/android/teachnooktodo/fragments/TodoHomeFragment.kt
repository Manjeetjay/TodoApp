package com.android.teachnooktodo.fragments

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.teachnooktodo.R
import com.android.teachnooktodo.TodoAdapter
import com.android.teachnooktodo.tasks.Task
import com.android.teachnooktodo.tasks.TaskDatabase
import com.android.teachnooktodo.tasks.TaskRepository
import com.google.android.material.floatingactionbutton.FloatingActionButton
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class TodoHomeFragment : Fragment(), TodoAdapter.TodoItemClickListener {
    private lateinit var adapter: TodoAdapter
    private lateinit var taskRepository: TaskRepository

    @SuppressLint("UseSwitchCompatOrMaterialCode")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_todo_home, container, false)
        val recyclerView = view.findViewById<RecyclerView>(R.id.rv_home)
        recyclerView.layoutManager = LinearLayoutManager(context)
        val taskDatabase = TaskDatabase.getInstance(requireContext())
        taskRepository = TaskRepository(taskDatabase.taskDao())

        val searchView = view.findViewById<SearchView>(R.id.sv_todo_home)
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                GlobalScope.launch {
                    val filteredTasks = taskRepository.searchTasks(newText.orEmpty())
                    withContext(Dispatchers.Main) {
                        adapter.updateTasks(filteredTasks)
                    }
                }
                return true
            }
        })


        GlobalScope.launch {
            val tasks = taskRepository.getAllTasks()
            withContext(Dispatchers.Main) {
                adapter = TodoAdapter(tasks, this@TodoHomeFragment)
                recyclerView.adapter = adapter
            }
        }

        view?.findViewById<FloatingActionButton>(R.id.fab_add_task)?.setOnClickListener {
            switchToAddScreen()
        }
        return view
    }

    private fun switchToAddScreen() {
        val fragment = TodoAddFragment()
        val fragmentManager = requireActivity().supportFragmentManager
        val fragmentTransaction = fragmentManager.beginTransaction()
        fragmentTransaction.replace(R.id.fragment_container, fragment)
        fragmentTransaction.addToBackStack(null)
        fragmentTransaction.commit()
    }

    override fun onEditItemClick(task: Task) {
        val bundle = Bundle().apply {
            putInt("taskId", task.id)
        }
        val fragment = TodoEditFragment().apply {
            arguments = bundle
        }
        val fragmentManager = (requireActivity() as AppCompatActivity).supportFragmentManager
        val fragmentTransaction = fragmentManager.beginTransaction()
        fragmentTransaction.replace(R.id.fragment_container, fragment)
        fragmentTransaction.addToBackStack(null)
        fragmentTransaction.commit()
    }

    override fun onDeleteItemClick(task: Task) {
        GlobalScope.launch {
            taskRepository.deleteTask(task)
            // Refresh the UI after deletion
            val updatedTasks = taskRepository.getAllTasks()
            withContext(Dispatchers.Main) {
                adapter.updateTasks(updatedTasks)
            }
        }
    }
}
