package com.android.teachnooktodo.fragments

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.CheckBox
import android.widget.EditText
import android.widget.Spinner
import androidx.fragment.app.Fragment
import com.android.teachnooktodo.R
import com.android.teachnooktodo.TodoAdapter
import com.android.teachnooktodo.tasks.Task
import com.android.teachnooktodo.tasks.TaskDatabase
import com.android.teachnooktodo.tasks.TaskPriority
import com.google.android.material.textfield.TextInputLayout
import kotlin.random.Random

class TodoAddFragment : Fragment() {

    private lateinit var db: TaskDatabase

    @SuppressLint("MissingInflatedId")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_todo_add, container, false)

        db = TaskDatabase.getInstance(requireContext())

        val title = view.findViewById<TextInputLayout>(R.id.tv_add_title)
        val desc = view.findViewById<TextInputLayout>(R.id.tv_add_description)
        val dueDate = view.findViewById<EditText>(R.id.add_date_picker)
        val isCompleted = view.findViewById<CheckBox>(R.id.add_checkbox_is_completed)
        val categorySpinner = view.findViewById<Spinner>(R.id.add_category_spinner)
        val prioritySpinner = view.findViewById<Spinner>(R.id.add_priority_spinner)
        val addBtn = view.findViewById<Button>(R.id.add_task)


        val categories = getCategoryList()
        val categoryAdapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, categories)
        categoryAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        categorySpinner.adapter = categoryAdapter

        val priorities = TaskPriority.entries.map { it.name }
        val priorityAdapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, priorities)
        priorityAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        prioritySpinner.adapter = priorityAdapter


        addBtn.setOnClickListener {
            val titleText = title.editText?.text.toString()
            val descText = desc.editText?.text.toString()
            val dueDateText = dueDate.editableText?.toString()
            val isCompletedChecked = isCompleted.isChecked
            val selectedCategory = categorySpinner.selectedItem.toString()
            val selectedPriority = prioritySpinner.selectedItem.toString()
            val randomTaskId = Random.nextInt(1, 101)

            val newTask = Task(
                id = randomTaskId,
                title = titleText,
                description = descText,
                dueDate = dueDateText,
                isCompleted = isCompletedChecked,
                category = selectedCategory,
                priority = TaskPriority.valueOf(selectedPriority ?: TaskPriority.NONE.name)
            )

            Thread {
                db.taskDao().insertTask(newTask)
                activity?.runOnUiThread {
                    requireActivity().supportFragmentManager.popBackStack()
                }
            }.start()
        }

        return view
    }

    private fun getCategoryList(): List<String> {
        return listOf("Work", "Personal", "Study", "Other")
    }
}