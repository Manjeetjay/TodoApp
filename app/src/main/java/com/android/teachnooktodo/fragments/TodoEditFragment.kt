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
import com.android.teachnooktodo.tasks.Task
import com.android.teachnooktodo.tasks.TaskDatabase
import com.android.teachnooktodo.tasks.TaskPriority
import com.google.android.material.textfield.TextInputLayout


class TodoEditFragment : Fragment() {

    private lateinit var db: TaskDatabase

    @SuppressLint("MissingInflatedId")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_todo_edit, container, false)

        val taskId = arguments?.getInt("taskId") ?: return view

        db = TaskDatabase.getInstance(requireContext())

        val title = view.findViewById<TextInputLayout>(R.id.tv_edit_title)
        val desc = view.findViewById<TextInputLayout>(R.id.tv_edit_description)
        val dueDate = view.findViewById<EditText>(R.id.edit_date_picker)
        val isCompleted = view.findViewById<CheckBox>(R.id.edit_checkbox_is_completed)
        val categorySpinner = view.findViewById<Spinner>(R.id.edit_category_spinner)
        val prioritySpinner = view.findViewById<Spinner>(R.id.edit_priority_spinner)
        val editBtn = view.findViewById<Button>(R.id.edit_task)

        // Set up category spinner
        val categories = getCategoryList() // Implement this function to get categories from the database
        val categoryAdapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, categories)
        categoryAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        categorySpinner.adapter = categoryAdapter

        // Set up priority spinner
        val priorities = TaskPriority.entries.map { it.name }
        val priorityAdapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, priorities)
        priorityAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        prioritySpinner.adapter = priorityAdapter



        Thread {
            val task = db.taskDao().getTask(taskId)
            activity?.runOnUiThread {
                title.editText?.setText(task?.title)
                desc.editText?.setText(task?.description)
                dueDate.setText(task?.dueDate)
                isCompleted.isChecked = task?.isCompleted == true

                // Select category in the spinner
                val categoryPosition = categories.indexOf(task?.category)
                categorySpinner.setSelection(if (categoryPosition != -1) categoryPosition else 0)

                // Select priority in the spinner
                val priorityPosition = priorities.indexOf(task?.priority?.name)
                prioritySpinner.setSelection(if (priorityPosition != -1) priorityPosition else 0)
            }

            editBtn.setOnClickListener {
                val titleText = title.editText?.text.toString()
                val descText = desc.editText?.text.toString()
                val dueDateText = dueDate.text?.toString()
                val isCompletedChecked = isCompleted.isChecked
                val selectedCategory = categorySpinner.selectedItem?.toString()
                val selectedPriority = prioritySpinner.selectedItem?.toString()

                val newTask = Task(
                    id = taskId,
                    title = titleText,
                    description = descText,
                    dueDate = dueDateText,
                    isCompleted = isCompletedChecked,
                    category = selectedCategory,
                    priority = TaskPriority.valueOf(selectedPriority ?: TaskPriority.MEDIUM.name)
                )



                Thread {
                    db.taskDao().updateTask(newTask)
                    activity?.runOnUiThread {
                        requireActivity().supportFragmentManager.popBackStack()
                    }
                }.start()
            }
        }.start()

        return view
    }

    private fun getCategoryList(): List<String> {
        return listOf("Work", "Personal", "Study", "Other")
    }
}
