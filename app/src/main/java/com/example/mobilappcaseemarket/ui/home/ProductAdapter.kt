package com.example.mobilappcaseemarket.ui.home

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.mobilappcaseemarket.R
import com.example.mobilappcaseemarket.data.model.Product

class ProductAdapter(
    private val list: List<Product>,
    private val imageHeight: Int,
    private val onItemClick: (Product) -> Unit
) : RecyclerView.Adapter<ProductAdapter.ProductViewHolder>() {

    class ProductViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val img = itemView.findViewById<ImageView>(R.id.imgProduct)
        val name = itemView.findViewById<TextView>(R.id.txtName)
        val price = itemView.findViewById<TextView>(R.id.txtPrice)
        val btnAdd = itemView.findViewById<Button>(R.id.btnAddToCart)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_product, parent, false)
        return ProductViewHolder(view)
    }

    override fun onBindViewHolder(holder: ProductViewHolder, position: Int) {
        val item = list[position]

        val params = holder.img.layoutParams
        params.height = imageHeight
        holder.img.layoutParams = params
        holder.name.text = item.name
        holder.price.text = "${item.price} ₺"

        Glide.with(holder.itemView.context)
            .load(item.image)
            .into(holder.img)

        holder.itemView.setOnClickListener {
            onItemClick(item)
        }
    }

    override fun getItemCount(): Int = list.size
}
