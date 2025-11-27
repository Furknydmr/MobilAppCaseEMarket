package com.example.mobilappcaseemarket.ui.cart

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.mobilappcaseemarket.R
import com.example.mobilappcaseemarket.data.model.CartItem
import com.example.mobilappcaseemarket.data.model.Product
import com.example.mobilappcaseemarket.data.repository.ProductRepository
import com.example.mobilappcaseemarket.databinding.FragmentCartBinding

class CartFragment : Fragment() {

    private lateinit var binding: FragmentCartBinding
    private lateinit var cartViewModel: CartViewModel
    private lateinit var adapter: CartAdapter
    private lateinit var recyclerView: RecyclerView




    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentCartBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupViewModel()
        setupRecyclerView()
        observeViewModel()

        cartViewModel.loadCart() // ilk yükleme
    }

    private fun setupViewModel() {
        val factory = CartViewModel.CartViewModelFactory(requireContext())

        cartViewModel = ViewModelProvider(
            requireActivity(),
            factory
        )[CartViewModel::class.java]
    }

    private fun setupRecyclerView() {
        adapter = CartAdapter(
            mutableListOf(),
            onIncrease = { cartViewModel.increaseQuantity(it) },
            onDecrease = { cartViewModel.decreaseQuantity(it) }
        )


        recyclerView = binding.rvCart
        recyclerView.layoutManager = GridLayoutManager(requireContext(), 1)
        recyclerView.adapter = adapter
    }

    private fun observeViewModel() {
        cartViewModel.cartItems.observe(viewLifecycleOwner) { list ->
            Log.d("CART_DEBUG", "FRAGMENT'TE GÖRÜNEN LİSTE: ${list.size}")
            adapter.updateList(list)
            updateTotalPrice(list)
        }
    }

    private fun updateTotalPrice(list: List<CartItem>) {

        val total = list.sumOf { item ->
            // price zaten "19.90" veya "19" gibi geliyor
            val priceDouble = item.price.toDoubleOrNull() ?: 0.0

            priceDouble * item.quantity
        }

        binding.txtTotalPrice.text = "%.2f ₺".format(total)
    }

}
