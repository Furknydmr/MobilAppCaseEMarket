package com.example.mobilappcaseemarket.ui.home.favourite

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.lifecycle.LifecycleOwner
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.mobilappcaseemarket.R
import com.example.mobilappcaseemarket.data.model.Product
import com.example.mobilappcaseemarket.databinding.ItemFavouriteBinding
import com.example.mobilappcaseemarket.databinding.ItemProductBinding

class FavouriteAdapter(
    private val list: MutableList<Product>,
    private val onItemClick: (Product) -> Unit
) : RecyclerView.Adapter<FavouriteAdapter.FavViewHolder>() {



    class FavViewHolder(val binding: ItemFavouriteBinding) : RecyclerView.ViewHolder(binding.root) {

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FavViewHolder {
        val binding = ItemFavouriteBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return FavViewHolder(binding)
    }

    override fun onBindViewHolder(holder: FavViewHolder, position: Int) {
        val item = list[position]


        holder.binding.txtName.text = item.name
        holder.binding.txtPrice.text = "${item.price} ₺"

        holder.itemView.setOnClickListener {
            onItemClick(item)
        }

    }

    override fun getItemCount() = list.size

    fun updateList(newList: List<Product>) {
        list.clear()
        list.addAll(newList)
        notifyDataSetChanged()
    }

}
