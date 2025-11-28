package com.example.mobilappcaseemarket.ui.details

import ProductDetailViewModel
import ProductDetailViewModelFactory
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.example.mobilappcaseemarket.R
import com.example.mobilappcaseemarket.data.local.AppDatabase
import com.example.mobilappcaseemarket.data.repository.FavouriteRepository
import com.example.mobilappcaseemarket.data.repository.ProductRepository
import com.example.mobilappcaseemarket.databinding.FragmentProductDetailBinding
import com.example.mobilappcaseemarket.ui.cart.CartViewModel
import com.example.mobilappcaseemarket.ui.home.favourite.FavouriteViewModel

class ProductDetailFragment : Fragment() {

    private var _binding: FragmentProductDetailBinding? = null
    private val binding get() = _binding!!

    private lateinit var viewModel: ProductDetailViewModel
    private lateinit var cartViewModel: CartViewModel
    private lateinit var favouriteViewModel: FavouriteViewModel

    private var productId: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        cartViewModel = ViewModelProvider(
            this,
            CartViewModel.CartViewModelFactory(requireContext())
        )[CartViewModel::class.java]

        productId = requireArguments().getString("productId") ?: ""
        Log.d("DETAIL_DEBUG", "Gelen productId: $productId")

        val repo = ProductRepository()
        viewModel = ViewModelProvider(
            this,
            ProductDetailViewModelFactory(repo)
        )[ProductDetailViewModel::class.java]

        viewModel.fetchProductById(productId)

        val favDao = AppDatabase.getDatabase(requireContext()).favouriteDao()
        val favRepo = FavouriteRepository(favDao)
        favouriteViewModel = FavouriteViewModel(favRepo)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentProductDetailBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.btnBack.setOnClickListener {
            requireActivity().onBackPressedDispatcher.onBackPressed()
        }

        binding.btnAddToCart.setOnClickListener {
            val product = viewModel.product.value
            if (product != null) {
                cartViewModel.addProductToCart(product)
                Log.d("DETAIL_ADD", "Sepete eklendi.")
            }
        }


        // PRODUCT OBSERVER
        viewModel.product.observe(viewLifecycleOwner) { product ->
            if (product == null) return@observe

            binding.txtToolbarTitle.text = product.name
            binding.txtTitle.text = product.name
            binding.txtDescription.text = product.description
            binding.txtPrice.text = "${product.price} ₺"

            Glide.with(this)
                .load(product.image)
                .into(binding.imgProduct)
        }

        favouriteViewModel.favourites.observe(viewLifecycleOwner) { favSet ->

            val isFav = favSet.contains(productId)

            if (isFav) {
                binding.imgFavouriteDetail.setImageResource(R.drawable.ic_fav_24_filled)
            } else {
                binding.imgFavouriteDetail.setImageResource(R.drawable.ic_fav_24_favourite)
            }
        }

        binding.imgFavouriteDetail.setOnClickListener {
            favouriteViewModel.toggleFavourite(productId)
        }
    }
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
