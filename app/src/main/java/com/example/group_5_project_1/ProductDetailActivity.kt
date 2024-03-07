package com.example.group_5_project_1

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage

class ProductDetailActivity : AppCompatActivity() {
    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_product_detail)
        val imageViewDetail: ImageView = findViewById(R.id.imageViewDetail)
        val textViewNameDetail: TextView = findViewById(R.id.textViewNameDetail)
        val textViewPriceDetail: TextView = findViewById(R.id.textViewPriceDetail)
        val textViewDescriptionDetail: TextView = findViewById(R.id.textViewDescriptionDetail)
        val addToCartButton: Button = findViewById(R.id.addToCart)




        intent?.let {
            val name = it.getStringExtra("NAME")
            val prize = it.getDoubleExtra("PRICE", 0.0)
            val description = it.getStringExtra("DESCRIPTION")
            val imageUrl = it.getStringExtra("IMAGE_URL")
            Log.d("ProductDetailActivity", "Image URL: $imageUrl")
            val storageReference = imageUrl?.let { it1 ->
                FirebaseStorage.getInstance().getReferenceFromUrl(
                    it1
                )
            }

            Glide.with(this)
                .load(storageReference)
                .into(imageViewDetail)
            textViewNameDetail.text = name
            textViewPriceDetail.text = String.format("$%.2f", prize)
            textViewDescriptionDetail.text = description
            addToCartButton.setOnClickListener {
                if (name != null) {
                    if (description != null) {
                        if (imageUrl != null) {
                            addToCart(name, prize, description, imageUrl)
                        }
                    }
                }
                val intent = Intent(this, CartActivity::class.java)
                startActivity(intent)
            }
        }

    }

    private fun addToCart(name: String, prize: Double, description: String, imageUrl: String) {
        val currentUser = FirebaseAuth.getInstance().currentUser
        val userID = currentUser?.uid

        if (userID != null) {
            val cartItem = mapOf(
                "name" to name,
                "prize" to prize,
                "description" to description,
                "imageUrl" to imageUrl,
                "quantity" to 1  // Assuming a default quantity of 1 for simplicity
            )

            // Reference to the current user's cart
            val userCartRef = FirebaseDatabase.getInstance().getReference("Carts").child(userID).push()
            userCartRef.setValue(cartItem).addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    Toast.makeText(this, "$name added to cart.", Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(this, "Failed to add $name to cart.", Toast.LENGTH_SHORT).show()
                }
            }
        } else {
            Toast.makeText(this, "You must be logged in to add items to the cart.", Toast.LENGTH_LONG).show()
        }
    }

}