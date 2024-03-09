package com.android.teachnooktodo.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.android.teachnooktodo.MainActivity
import com.android.teachnooktodo.R
import com.android.teachnooktodo.user.UserDatabase

class SignInFragment : Fragment() {

    private lateinit var db: UserDatabase

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_sign_in, container, false)

        db = UserDatabase.getInstance(requireContext())

        val usernameField = view.findViewById<EditText>(R.id.username_field)
        val passwordField = view.findViewById<EditText>(R.id.password_field)
        val signInButton = view.findViewById<Button>(R.id.sign_in_button)

        signInButton.setOnClickListener {
            val username = usernameField.text.toString()
            val password = passwordField.text.toString()

            Thread {
                val user = db.userDao().getUser(username)
                activity?.runOnUiThread {
                    if (user != null) {
                        if (user.password == password) {
                            (activity as MainActivity).showTaskHomeScreen()
                        } else {
                            Toast.makeText(context, "Incorrect password", Toast.LENGTH_SHORT).show()
                        }
                    } else {
                        Toast.makeText(context, "User does not exist", Toast.LENGTH_SHORT).show()
                    }
                }
            }.start()
        }

        view.findViewById<TextView>(R.id.tv_sign_up).setOnClickListener {
            (activity as MainActivity).showSignUpScreen()
        }

        return view
    }
}

