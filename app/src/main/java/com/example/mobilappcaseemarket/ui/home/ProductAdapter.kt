package com.example.mobilappcaseemarket.ui.home

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.mobilappcaseemarket.R
import com.example.mobilappcaseemarket.data.model.Product
import com.example.mobilappcaseemarket.databinding.ItemProductBinding
import com.example.mobilappcaseemarket.ui.home.favourite.FavouriteViewModel

class ProductAdapter(
    private val imageHeight: Int,
    private val onItemClick: (Product) -> Unit,
    private val onAddClick: (Product) -> Unit,
    private val onFavouriteClick: (Product) -> Unit,
) : ListAdapter<Product, ProductAdapter.ProductViewHolder>(DiffCallback) {




    private var favourites: Set<String> = emptySet()


    class ProductViewHolder(val binding: ItemProductBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductViewHolder {
        val binding = ItemProductBinding.inflate(
            LayoutInflater.from(parent.context), parent, false)
           return ProductViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ProductViewHolder, position: Int) {
        val item = getItem(position)


        val params = holder.binding.imgProduct.layoutParams
        params.height = imageHeight
        holder.binding.imgProduct.layoutParams = params

        holder.binding.txtName.text = item.name
        holder.binding.txtPrice.text = "${item.price} ₺"

        Glide.with(holder.itemView.context)
            .load(item.image)
            .into(holder.binding.imgProduct)

        holder.itemView.setOnClickListener {
            onItemClick(item)
        }


        holder.binding.btnAddToCart.setOnClickListener {
            onAddClick(item)
        }

        val isFav = favourites.contains(item.id)
        holder.binding.imgFavourite.setImageResource(
            if (isFav) R.drawable.ic_fav_24_filled
            else R.drawable.ic_fav_24_favourite
        )

        holder.binding.imgFavourite.setOnClickListener {
            onFavouriteClick(item)
        }
    }

    fun updateFavourites(newFavs: Set<String>) {
        favourites = newFavs
        notifyDataSetChanged()
    }


    object DiffCallback : androidx.recyclerview.widget.DiffUtil.ItemCallback<Product>() {

        override fun areItemsTheSame(oldItem: Product, newItem: Product): Boolean {
            return oldItem.id == newItem.id   // Aynı ürün mü?
        }

        override fun areContentsTheSame(oldItem: Product, newItem: Product): Boolean {
            return oldItem == newItem         // İçerik aynı mı?
        }
    }





}
