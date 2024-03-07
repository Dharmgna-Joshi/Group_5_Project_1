package com.example.group_5_project_1

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.group_5_project_1.databinding.ActivityProductBinding
import com.firebase.ui.database.FirebaseRecyclerOptions
import com.google.firebase.database.FirebaseDatabase

class ProductActivity : AppCompatActivity() {
    private lateinit var binding:ActivityProductBinding
    private var adapter: ProductsAdapter? =null




    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityProductBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val query = FirebaseDatabase.getInstance().reference.child("Products")
        val options = FirebaseRecyclerOptions.Builder<Products>().setQuery(query, Products::class.java).build()
        adapter = ProductsAdapter(options)


        val rView : RecyclerView = findViewById(R.id.products_recycler_view)
        rView.layoutManager = LinearLayoutManager(this)
        rView.adapter = adapter

    }
    override fun onStart() {
        super.onStart()
        adapter?.startListening()
    }

}