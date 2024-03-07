package com.example.group_5_project_1

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Patterns
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity


class CheckOutActivity : AppCompatActivity() {
    private var nameEditText: EditText? = null
    private var emailEditText: EditText? = null
    private var phoneEditText: EditText? = null
    private var creditCardEditText: EditText? = null
    private var expiryDateEditText: EditText? = null
    private var cvvEditText: EditText? = null

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_check_out)
        nameEditText = findViewById(R.id.nameEditText)
        emailEditText = findViewById(R.id.emailEditText)
        phoneEditText = findViewById(R.id.phoneEditText)
        creditCardEditText = findViewById(R.id.creditCardEditText)
        expiryDateEditText = findViewById(R.id.expiryDateEditText)
        cvvEditText = findViewById(R.id.cvvEditText)
        val submitButton = findViewById<Button>(R.id.placeOrderButton)
        submitButton.setOnClickListener {
            if (validateForm()) {
                // TODO: Add your form submission logic here
                Toast.makeText(
                    this@CheckOutActivity,
                    "Form Submitted Successfully",
                    Toast.LENGTH_LONG
                ).show()
            }
        }
    }

    private fun validateForm(): Boolean {
        var isValid = true

        // Validate Name
        if (nameEditText!!.text.toString().trim { it <= ' ' }.isEmpty()) {
            nameEditText!!.error = "Name is required"
            isValid = false
        }

        // Validate Email
        val email = emailEditText!!.text.toString().trim { it <= ' ' }
        if (email.isEmpty() || !Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            emailEditText!!.error = "Valid email is required"
            isValid = false
        }

        // Validate Phone
        val phone = phoneEditText!!.text.toString().trim { it <= ' ' }
        if (phone.isEmpty() || !phone.matches("\\d{10}".toRegex())) { // Assuming a 10-digit phone number
            phoneEditText!!.error = "Valid 10-digit phone number is required"
            isValid = false
        }

        // Validate Credit Card Number (basic length check)
        val creditCard = creditCardEditText!!.text.toString().trim { it <= ' ' }
        if (creditCard.length != 16) { // Assuming a 16-digit credit card number
            creditCardEditText!!.error = "Valid 16-digit credit card number is required"
            isValid = false
        }

        // Validate Expiry Date (basic format check)
        val expiryDate = expiryDateEditText!!.text.toString().trim { it <= ' ' }
        if (expiryDate.isEmpty() || !expiryDate.matches("\\d{2}/\\d{2}".toRegex())) { // Assuming MM/YY format
            expiryDateEditText!!.error = "Valid expiry date (MM/YY) is required"
            isValid = false
        }

        // Validate CVV (basic length check)
        val cvv = cvvEditText!!.text.toString().trim { it <= ' ' }
        if (cvv.length != 3) { // Assuming a 3-digit CVV
            cvvEditText!!.error = "Valid 3-digit CVV is required"
            isValid = false
        }
        return isValid
    }
}
