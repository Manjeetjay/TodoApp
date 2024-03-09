package com.android.teachnooktodo.fragments

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.room.Room
import com.android.teachnooktodo.MainActivity
import com.android.teachnooktodo.R
import com.android.teachnooktodo.user.User
import com.android.teachnooktodo.user.UserDatabase


class SignUpFragment : Fragment() {

    private lateinit var db: UserDatabase

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_sign_up, container, false)

        db = UserDatabase.getInstance(requireContext())

        val usernameField = view.findViewById<EditText>(R.id.username_field)
        val passwordField = view.findViewById<EditText>(R.id.password_field)
        val signUpButton = view.findViewById<Button>(R.id.sign_up_button)

        signUpButton.setOnClickListener {
            val username = usernameField.text.toString()
            val password = passwordField.text.toString()

            Thread {
                val user = db.userDao().getUser(username)
                if (user == null) {
                    db.userDao().insert(User(0, username, password))
                    activity?.runOnUiThread {
                        Toast.makeText(context, "User created", Toast.LENGTH_SHORT).show()
                        (activity as MainActivity).showTaskHomeScreen()
                    }
                } else {
                    activity?.runOnUiThread {
                        Toast.makeText(context, "Username already taken", Toast.LENGTH_SHORT).show()
                    }
                }
            }.start()
        }

        view.findViewById<TextView>(R.id.tv_sign_in).setOnClickListener {
            (activity as MainActivity).showSignInScreen()
        }

        return view
    }
}




