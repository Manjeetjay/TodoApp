package com.android.teachnooktodo.tasks

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "task_table")
data class Task(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val title: String = "Title",
    val description: String = "Description",
    val dueDate: String?,
    var isCompleted: Boolean = false,
    val category: String?,
    val priority: TaskPriority = TaskPriority.MEDIUM
)


