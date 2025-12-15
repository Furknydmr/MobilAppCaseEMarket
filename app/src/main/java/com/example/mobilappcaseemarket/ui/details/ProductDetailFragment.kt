package com.example.mobilappcaseemarket.ui.details

import ProductDetailViewModel
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.example.mobilappcaseemarket.R
import com.example.mobilappcaseemarket.data.repository.ProductRepository
import com.example.mobilappcaseemarket.databinding.FragmentProductDetailBinding
import com.example.mobilappcaseemarket.ui.cart.CartViewModel
import com.example.mobilappcaseemarket.ui.home.HomeViewModel
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


        productId = requireArguments().getString("productId") ?: ""

        // PRODUCT DETAIL VM
        viewModel = ViewModelProvider(
            requireActivity(),
            ProductDetailViewModel.ProductDetailViewModelFactory()
        )[ProductDetailViewModel::class.java]

        cartViewModel = ViewModelProvider(
            requireActivity(),
            CartViewModel.CartViewModelFactory(requireContext())
        )[CartViewModel::class.java]

        favouriteViewModel = ViewModelProvider(
            requireActivity(),
            FavouriteViewModel.FavouriteViewModelFactory(requireContext())
        )[FavouriteViewModel::class.java]


        viewModel.fetchProductById(productId)

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

        binding.btnBack.setOnClickListener {
            requireActivity().onBackPressedDispatcher.onBackPressed()
        }

        binding.btnAddToCart.setOnClickListener {
            viewModel.product.value?.let { product ->
                cartViewModel.addProductToCart(product)
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
