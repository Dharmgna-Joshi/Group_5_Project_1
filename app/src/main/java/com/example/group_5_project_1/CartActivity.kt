package com.example.group_5_project_1

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener


class CartActivity : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: CartAdapter
    private var cartItems = mutableListOf<CartItem>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_cart)

        recyclerView = findViewById(R.id.cartRecyclerView)
        recyclerView.layoutManager = LinearLayoutManager(this)
        adapter = CartAdapter(cartItems)
        recyclerView.adapter = adapter

        loadUserCart()
        val moveToProduct = findViewById<Button>(R.id.movetoproduct)
        moveToProduct.setOnClickListener {
            val intent = Intent(
                this@CartActivity,
                ProductActivity::class.java
            )
            startActivity(intent)
        }
        val CheckoutForm = findViewById<Button>(R.id.checkout)
        CheckoutForm.setOnClickListener {
            val intent = Intent(
                this@CartActivity,
                CheckOutActivity::class.java
            )
            startActivity(intent)
        }
    }

    private fun loadUserCart() {
        val currentUser = FirebaseAuth.getInstance().currentUser
        val userID = currentUser?.uid

        if (userID != null) {
            val userCartRef = FirebaseDatabase.getInstance().getReference("Carts").child(userID)
            userCartRef.addValueEventListener(object : ValueEventListener {
                @SuppressLint("NotifyDataSetChanged")
                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    cartItems.clear()
                    for (cartItemSnapshot in dataSnapshot.children) {
                        val cartItem = cartItemSnapshot.getValue(CartItem::class.java)
                        Log.d(
                            "CartActivity",
                            "Fetched item: ${cartItem?.name} with prize: ${cartItem?.prize}"
                        )
                        cartItem?.let { cartItems.add(it) }
                    }
                    adapter.notifyDataSetChanged()
                }

                override fun onCancelled(databaseError: DatabaseError) {
                    Log.w("CartActivity", "loadUserCart:onCancelled", databaseError.toException())
                }
            })
        }
    }
}