package com.example.group_5_project_1

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth

class SignUpActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_up)

        auth = FirebaseAuth.getInstance()

        val emailEditText: EditText = findViewById(R.id.editTextEmailSignUp)
        val passwordEditText: EditText = findViewById(R.id.editTextPasswordSignUp)
        val signUpButton: Button = findViewById(R.id.buttonSignUp)
        val LogInButton: Button = findViewById(R.id.buttonToLogin)

        emailEditText.addTextChangedListener(object :TextWatcher{
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {
            }
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                emailEditText.error = null
            }
            override fun afterTextChanged(s: Editable) {
            }
        })
        passwordEditText.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {
            }
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                passwordEditText.error = null
            }
            override fun afterTextChanged(s: Editable) {
            }
        })

        signUpButton.setOnClickListener {
            val email = emailEditText.text.toString().trim()
            val password = passwordEditText.text.toString().trim()

            if (email.isNotBlank() && isPasswordValid(password)) {
                // If the email is not blank and the password is valid, attempt to create a new user
                auth.createUserWithEmailAndPassword(email, password)
                    .addOnCompleteListener(this) { task ->
                        if (task.isSuccessful) {
                            Toast.makeText(this, "Sign up successful.", Toast.LENGTH_SHORT).show()
                            val intent = Intent(this, LoginActivity::class.java)
                            startActivity(intent)
                            finish()
                        } else {
                            Toast.makeText(this, "Authentication failed: ${task.exception?.message}", Toast.LENGTH_LONG).show()
                        }
                    }
            } else {
                if (email.isBlank()) {
                    emailEditText.error = "Email cannot be blank."
                }

                if (!isPasswordValid(password)) {
                    passwordEditText.error = "Password must contain at least 8 characters, including an uppercase letter, a lowercase letter, a number, and a special character."
                }
            }
        }

        LogInButton.setOnClickListener {
            // Navigate to the login activity when the user clicks on the text view
            startActivity(Intent(this, LoginActivity::class.java))
        }
    }

    private fun isPasswordValid(password: String): Boolean {
        val passwordRegex = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=!])(?=\\S+$).{8,}$"
        return password.matches(passwordRegex.toRegex())
    }

}
