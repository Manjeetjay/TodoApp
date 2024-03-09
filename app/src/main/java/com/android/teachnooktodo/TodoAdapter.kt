package com.android.teachnooktodo

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.ImageButton
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.android.teachnooktodo.tasks.Task
import com.android.teachnooktodo.tasks.TaskDiffCallback

class TodoAdapter(
    private val todoItems: List<Task> = mutableListOf(),
    private val itemClickListener: TodoItemClickListener? = null
) : RecyclerView.Adapter<TodoAdapter.TodoViewHolder>() {

    interface TodoItemClickListener {
        fun onEditItemClick(task: Task)
        fun onDeleteItemClick(task: Task)
    }


    fun updateTasks(newTasks: List<Task>) {
        val diffCallback = TaskDiffCallback(todoItems, newTasks)
        val diffResult = DiffUtil.calculateDiff(diffCallback)

        (todoItems as MutableList).clear()
        (todoItems as MutableList).addAll(newTasks)

        diffResult.dispatchUpdatesTo(this)
    }

    private val randomColors = listOf(
        R.color.lowSaturationRed, R.color.lowSaturationGreen,
        R.color.lowSaturationBlue, R.color.lowSaturationYellow,
        R.color.lowSaturationCyan
    )


    inner class TodoViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val title: TextView = itemView.findViewById(R.id.todo_item_title)
        val desc: TextView = itemView.findViewById(R.id.todo_item_desc)
        val dueDate: TextView = itemView.findViewById(R.id.todo_item_due_date)
        val isCompleted: CheckBox = itemView.findViewById(R.id.todo_item_is_completed)
        val category: TextView = itemView.findViewById(R.id.todo_item_category)
        val priority: TextView = itemView.findViewById(R.id.todo_item_priority)
        val editBtn: ImageButton = itemView.findViewById(R.id.ib_edit)
        val deleteBtn: ImageButton = itemView.findViewById(R.id.ib_delete)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TodoViewHolder {
        return TodoViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.todo_item, parent, false)
        )
    }

    override fun getItemCount(): Int = todoItems.size


    override fun onBindViewHolder(holder: TodoViewHolder, position: Int) {


        val task = todoItems[position]
        holder.title.text = task.title
        holder.desc.text = task.description
        holder.dueDate.text = task.dueDate
        holder.category.text = task.category.toString()
        holder.priority.text = task.priority.toString()
        val isChecked = task.isCompleted

        val randomColor = randomColors[task.id % randomColors.size]
        holder.itemView.setBackgroundColor(ContextCompat.getColor(holder.itemView.context, if (isChecked) R.color.grey else randomColor))

        holder.isCompleted.setOnClickListener {
            holder.itemView.setBackgroundColor(ContextCompat.getColor(holder.itemView.context,R.color.grey ))
        }

        holder.editBtn.setOnClickListener {
            itemClickListener?.onEditItemClick(task)
        }

        holder.deleteBtn.setOnClickListener {
            itemClickListener?.onDeleteItemClick(task)
        }
    }
}











