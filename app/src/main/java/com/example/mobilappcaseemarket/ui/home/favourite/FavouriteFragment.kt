package com.example.mobilappcaseemarket.ui.home.favourite

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.example.mobilappcaseemarket.R
import com.example.mobilappcaseemarket.data.local.AppDatabase
import com.example.mobilappcaseemarket.data.repository.FavouriteRepository
import com.example.mobilappcaseemarket.data.repository.ProductRepository
import com.example.mobilappcaseemarket.databinding.FragmentFavouriteBinding
import com.example.mobilappcaseemarket.ui.cart.CartViewModel
import kotlinx.coroutines.launch


class FavouriteFragment : Fragment() {

    private var _binding: FragmentFavouriteBinding? = null
    private val binding get() = _binding!!

    private lateinit var favouriteViewModel: FavouriteViewModel
    private lateinit var cartViewModel: CartViewModel
    private lateinit var adapter: FavouriteAdapter

    private val productRepo = ProductRepository()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentFavouriteBinding.inflate(inflater, container, false)
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        cartViewModel = ViewModelProvider(
            requireActivity(),
            CartViewModel.CartViewModelFactory(requireContext())
        )[CartViewModel::class.java]


        val favDao = AppDatabase.getDatabase(requireContext()).favouriteDao()
        val favRepo = FavouriteRepository(favDao)
        favouriteViewModel = FavouriteViewModel(favRepo)

        // RECYCLER SETUP
        val screenHeight = resources.displayMetrics.heightPixels
        val imageHeight = (screenHeight * 0.14).toInt()

        binding.recyclerFavourite.layoutManager = GridLayoutManager(requireContext(), 1)

        adapter = FavouriteAdapter(
            mutableListOf(),
            favouriteViewModel,
            viewLifecycleOwner,
            onItemClick = { product ->
                val b = Bundle()
                b.putString("productId", product.id)
                findNavController().navigate(
                    R.id.fragment_productdetail,
                    b
                )
            }
        )



        binding.recyclerFavourite.adapter = adapter


        favouriteViewModel.favourites.observe(viewLifecycleOwner) { favSet ->
            lifecycleScope.launch {

                val allProducts = productRepo.getProducts()

                val favouriteProducts =
                    allProducts.filter { product -> favSet.contains(product.id) }

                adapter.updateList(favouriteProducts)
            }
        }
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}