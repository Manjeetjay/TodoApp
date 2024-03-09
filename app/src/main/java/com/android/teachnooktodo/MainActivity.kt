package com.android.teachnooktodo

import android.content.Context
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.room.Room
import com.android.teachnooktodo.fragments.SignInFragment
import com.android.teachnooktodo.fragments.SignUpFragment
import com.android.teachnooktodo.fragments.TodoAddFragment
import com.android.teachnooktodo.fragments.TodoEditFragment
import com.android.teachnooktodo.fragments.TodoHomeFragment
import com.android.teachnooktodo.tasks.TaskDatabase
import com.android.teachnooktodo.user.UserDatabase

class MainActivity : AppCompatActivity() {

    private lateinit var taskDb: TaskDatabase

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        taskDb = TaskDatabase.getInstance(applicationContext)

        if (isUserLoggedIn()) {
            showTaskHomeScreen()
        } else {
            showSignInScreen()
        }
    }

    private fun isUserLoggedIn(): Boolean {
        val sharedPreferences = getSharedPreferences("MyApp", Context.MODE_PRIVATE)
        return sharedPreferences.getBoolean("isLoggedIn", false)
    }

    fun showTaskHomeScreen() {
        val fragment = TodoHomeFragment()
        supportFragmentManager.beginTransaction()
            .replace(R.id.fragment_container, fragment)
            .commit()
    }

    fun showSignInScreen() {
        val fragment = SignInFragment()
        supportFragmentManager.beginTransaction()
            .replace(R.id.fragment_container, fragment)
            .commit()
    }

    fun showSignUpScreen() {
        val fragment = SignUpFragment()
        supportFragmentManager.beginTransaction()
            .replace(R.id.fragment_container, fragment)
            .commit()
    }

    fun showAddTaskScreen() {
        val fragment = TodoAddFragment()
        supportFragmentManager.beginTransaction()
            .replace(R.id.fragment_container, fragment)
            .addToBackStack(null)
            .commit()
    }

    fun showEditTaskScreen(taskId: Int) {
        val bundle = Bundle().apply {
            putInt("taskId", taskId)
        }
        val fragment = TodoEditFragment().apply {
            arguments = bundle
        }
        supportFragmentManager.beginTransaction()
            .replace(R.id.fragment_container, fragment)
            .addToBackStack(null)
            .commit()
    }
}



