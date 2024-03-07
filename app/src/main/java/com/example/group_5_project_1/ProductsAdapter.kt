package com.example.group_5_project_1

import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.firebase.ui.database.FirebaseRecyclerAdapter
import com.firebase.ui.database.FirebaseRecyclerOptions
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference

class ProductsAdapter(options: FirebaseRecyclerOptions<Products>)
    : FirebaseRecyclerAdapter<Products, ProductsAdapter.MyViewHolder>(options){
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return MyViewHolder(inflater, parent)
    }
    override fun onBindViewHolder(holder: MyViewHolder, position: Int, model: Products) {
        holder.txtName.text = model.name
        holder.txtPrize.text = model.prize.toString()
        val storageReference : StorageReference = FirebaseStorage.getInstance().getReferenceFromUrl(model.image)
        Glide.with(holder.productImage.context)
            .load(storageReference)
            .into(holder.productImage)
        holder.itemView.setOnClickListener {
            val context = holder.itemView.context
            val intent = Intent(context, ProductDetailActivity::class.java).apply {
                putExtra("NAME", model.name)
                putExtra("PRICE", model.prize)
                putExtra("DESCRIPTION", model.description)
                putExtra("IMAGE_URL", model.image)
            }
            context.startActivity(intent)
        }
    }

    class MyViewHolder(inflater: LayoutInflater,parent: ViewGroup):
        RecyclerView.ViewHolder(inflater.inflate(R.layout.product_row_layout,parent,false)) {
        val txtName: TextView = itemView.findViewById(R.id.textViewProductName)
        val txtPrize :TextView = itemView.findViewById(R.id.textViewProductPrice)
        val productImage : ImageView = itemView.findViewById(R.id.imageViewProduct)
    }

}