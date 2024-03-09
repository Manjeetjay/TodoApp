package com.android.teachnooktodo.tasks

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext



class TaskRepository(private val taskDao: TaskDao) {

    suspend fun insertTask(task: Task) {
        withContext(Dispatchers.IO) {
            taskDao.insertTask(task)
        }
    }

    suspend fun getAllTasks(): List<Task> {
        return withContext(Dispatchers.IO) {
            taskDao.getAllTasks()
        }
    }

    suspend fun updateTask(task: Task) {
        withContext(Dispatchers.IO) {
            taskDao.updateTask(task)
        }
    }


    suspend fun deleteTask(task: Task) {
        withContext(Dispatchers.IO) {
            taskDao.deleteTask(task)
        }
    }

    suspend fun searchTasks(query: String): List<Task> {
        return taskDao.searchTasks("%$query%")
    }
}


