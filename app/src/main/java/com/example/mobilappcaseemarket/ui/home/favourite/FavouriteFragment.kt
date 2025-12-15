package com.example.mobilappcaseemarket.ui.home.favourite

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.mobilappcaseemarket.R
import com.example.mobilappcaseemarket.data.local.AppDatabase
import com.example.mobilappcaseemarket.data.remote.RetrofitClient
import com.example.mobilappcaseemarket.data.repository.FavouriteRepository
import com.example.mobilappcaseemarket.data.repository.ProductRepository
import com.example.mobilappcaseemarket.databinding.FragmentFavouriteBinding
import com.example.mobilappcaseemarket.ui.cart.CartViewModel
import kotlinx.coroutines.launch


class FavouriteFragment : Fragment() {

    private var _binding: FragmentFavouriteBinding? = null
    private val binding get() = _binding!!
    private lateinit var favouriteViewModel: FavouriteViewModel
    private lateinit var adapter: FavouriteAdapter

    private val productRepo by lazy {
        ProductRepository(RetrofitClient.api)
    }


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentFavouriteBinding.inflate(inflater, container, false)
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        setupViewModel()
        setupRecycler()
        setupObservers()
        favouriteViewModel.isLoading.observe(viewLifecycleOwner) { isLoading ->
            binding.progressBar.isVisible = isLoading
            binding.recyclerFavourite.isVisible = !isLoading
        }
        favouriteViewModel.loadFavourites()

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun setupViewModel(){
        favouriteViewModel = ViewModelProvider(
            requireActivity(),
            FavouriteViewModel.FavouriteViewModelFactory(requireContext())
        )[FavouriteViewModel::class.java]
    }

    private fun setupRecycler() {
        adapter = FavouriteAdapter(
            mutableListOf(),
            onItemClick = { product ->
                val bundle = Bundle()
                bundle.putString("productId", product.id)
                findNavController().navigate(R.id.fragment_productdetail, bundle)
            }
        )

        binding.recyclerFavourite.layoutManager = LinearLayoutManager(requireContext())
        binding.recyclerFavourite.adapter = adapter
    }
    private fun setupObservers() {
        favouriteViewModel.favourites.observe(viewLifecycleOwner) { favIds ->

            lifecycleScope.launch {

                // Tüm ürünleri çek
                val allProducts = productRepo.getProducts()

                // Favori ürünleri filtrele
                val favouriteProducts = allProducts.filter { it.id in favIds }

                // Adapter’e gönder
                adapter.updateList(favouriteProducts)
            }
        }

    }


}