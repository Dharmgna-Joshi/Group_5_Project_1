package com.example.group_5_project_1

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.util.Patterns
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import java.text.SimpleDateFormat
import java.util.*

class CheckOutActivity : AppCompatActivity() {
    private var nameEditText: EditText? = null
    private var emailEditText: EditText? = null
    private var phoneEditText: EditText? = null
    private var creditCardEditText: EditText? = null
    private var expiryDateEditText: EditText? = null
    private var cvvEditText: EditText? = null
    private val auth: FirebaseAuth = FirebaseAuth.getInstance()
    private lateinit var database: DatabaseReference

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
        database = FirebaseDatabase.getInstance().reference
        submitButton.setOnClickListener {
            if (validateForm()) {
                // TODO: Add your form submission logic here
                Toast.makeText(
                    this@CheckOutActivity,
                    "Order Placed Successfully",
                    Toast.LENGTH_LONG
                ).show()
                placeOrder()

                // Redirect to the login screen (assuming LoginActivity is your login activity)
                val intent = Intent(this@CheckOutActivity, LoginActivity::class.java)
                startActivity(intent)
                finish() //
            }
        }
    }
    private fun placeOrder() {
        val currentUser = auth.currentUser
        val userID = currentUser?.uid

        if (userID != null) {
            // Get user information
            val name = nameEditText!!.text.toString().trim()
            val email = emailEditText!!.text.toString().trim()
            val phone = phoneEditText!!.text.toString().trim()

            // Get current date and time
            val sdf = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault())
            val currentDateAndTime: String = sdf.format(Date())

            // Create an order object
            val order = Order(name, email, phone, currentDateAndTime)

            // Get user cart items
            val userCartRef = database.child("Carts").child(userID)
            userCartRef.addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    for (cartItemSnapshot in dataSnapshot.children) {
                        val cartItem = cartItemSnapshot.getValue(CartItem::class.java)
                        cartItem?.let {
                            // Add cart item to the order
                            order.addCartItem(it)
                        }
                    }

                    // Store order in Firebase
                    val ordersRef = database.child("Orders")
                    val orderKey = ordersRef.push().key
                    orderKey?.let {
                        ordersRef.child(userID).child(it).setValue(order)
                    }

                    // Clear user cart
                    userCartRef.removeValue()

                    // Logout user
                    logoutUser()

                    // Redirect to the login screen (assuming LoginActivity is your login activity)
                    val intent = Intent(this@CheckOutActivity, LoginActivity::class.java)
                    startActivity(intent)
                    finish()
                }

                override fun onCancelled(databaseError: DatabaseError) {
                    // Handle error
                }
            })
        }
    }
    private fun logoutUser() {
        auth.signOut()
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
