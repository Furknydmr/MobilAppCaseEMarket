package com.example.mobilappcaseemarket.ui.cart

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.mobilappcaseemarket.data.model.CartItem
import com.example.mobilappcaseemarket.databinding.ItemCartBinding

class CartAdapter(
    private var items: MutableList<CartItem> = mutableListOf(),
    private val onIncrease: (CartItem) -> Unit,
    private val onDecrease: (CartItem) -> Unit
) : RecyclerView.Adapter<CartAdapter.CartViewHolder>() {

    class CartViewHolder(val binding: ItemCartBinding)
        : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CartViewHolder {
        val binding = ItemCartBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return CartViewHolder(binding)
    }


    override fun onBindViewHolder(holder: CartViewHolder, position: Int) {
        val item = items[position] //Tek seferde tüm item’ları çizmez. Her satır için hangi veriyi vereceğini bilmek zorundadır

        holder.binding.txtName.text = item.name
        holder.binding.txtPrice.text = "${item.price} ₺"
        holder.binding.txtQty.text = item.quantity.toString()

        holder.binding.btnIncrease.setOnClickListener {
            onIncrease(item)
        }

        holder.binding.btnDecrease.setOnClickListener {
            onDecrease(item)
        }
    }

    override fun getItemCount(): Int = items.size //Liste kaç eleman içeriyorsa RecyclerView’a O KADAR satır çizmesini söyle

    fun updateList(newList: List<CartItem>) {
        items = newList.toMutableList()
        notifyDataSetChanged()
    }

}
