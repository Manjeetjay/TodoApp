package com.android.teachnooktodo.tasks

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import androidx.room.Upsert

@Dao
interface TaskDao {
    @Query("SELECT * FROM task_table")
    fun getAllTasks(): List<Task>

    @Query("SELECT * FROM task_table WHERE id = :taskId")
    fun getTask(taskId: Int): Task?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertTask(task: Task)

    @Update
    fun updateTask(task: Task)

    @Query("DELETE FROM task_table WHERE id = :taskId")
    fun deleteTask(taskId: Int)

    @Query("SELECT * FROM task_table WHERE title LIKE :query OR description LIKE :query")
    fun searchTasks(query: String): List<Task>

    @Query("SELECT * FROM task_table WHERE category = :category AND priority = :priority AND isCompleted = :isCompleted")
    fun filterTasks(category: String?, priority: TaskPriority?, isCompleted: Boolean?): List<Task>

    @Delete
    fun deleteTask(task: Task)

    @Query("SELECT id FROM task_table WHERE title = :title LIMIT 1")
    fun getTaskIdByTitle(title: String): Int?



}



