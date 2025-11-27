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
import com.example.mobilappcaseemarket.ui.home.favourite.FavouriteViewModel

class FavouriteAdapter(
    private val list: MutableList<Product>,
    private val favouriteViewModel: FavouriteViewModel,
    private val lifecycleOwner: LifecycleOwner,
    private val onItemClick: (Product) -> Unit
) : RecyclerView.Adapter<FavouriteAdapter.FavViewHolder>() {

    class FavViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val img = itemView.findViewById<ImageView>(R.id.imgProduct)
        val name = itemView.findViewById<TextView>(R.id.txtName)
        val price = itemView.findViewById<TextView>(R.id.txtPrice)
        val favIcon = itemView.findViewById<ImageView>(R.id.imgFavourite)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FavViewHolder {
        val v = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_favourite, parent, false)
        return FavViewHolder(v)
    }

    override fun onBindViewHolder(holder: FavViewHolder, position: Int) {
        val item = list[position]

        holder.name.text = item.name
        holder.price.text = "${item.price} ₺"

        Glide.with(holder.itemView.context)
            .load(item.image)
            .into(holder.img)

        holder.itemView.setOnClickListener {
            onItemClick(item)
        }

        favouriteViewModel.favourites.observe(lifecycleOwner) { favSet ->
            val isFav = favSet.contains(item.id)
            holder.favIcon.setImageResource(
                if (isFav) R.drawable.ic_fav_24_filled
                else R.drawable.ic_fav_24_favourite
            )
        }

        holder.favIcon.setOnClickListener {
            favouriteViewModel.toggleFavourite(item.id)

            // Favoriden çıkarınca UI’dan da kaldır
            list.removeAt(position)
            notifyItemRemoved(position)
            notifyItemRangeChanged(position, list.size)
        }
    }

    override fun getItemCount() = list.size

    fun updateList(newList: List<Product>) {
        list.clear()
        list.addAll(newList)
        notifyDataSetChanged()
    }
}
