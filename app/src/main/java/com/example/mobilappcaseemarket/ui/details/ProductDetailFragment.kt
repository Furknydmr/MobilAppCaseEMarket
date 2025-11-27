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
import com.example.mobilappcaseemarket.data.model.CartItem
import com.example.mobilappcaseemarket.data.repository.ProductRepository
import com.example.mobilappcaseemarket.databinding.FragmentProductDetailBinding
import com.example.mobilappcaseemarket.ui.cart.CartViewModel

class ProductDetailFragment : Fragment() {
    private var _binding: FragmentProductDetailBinding? = null
    private val binding get() = _binding!!

    private lateinit var viewModel: ProductDetailViewModel
    private var productId: String? = null
    private lateinit var cartViewModel: CartViewModel


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val cartFactory = CartViewModel.CartViewModelFactory(requireContext())
        cartViewModel = ViewModelProvider(this, cartFactory)[CartViewModel::class.java]


        productId = requireArguments().getString("productId")
        Log.d("DETAIL_DEBUG", "Gelen productId: $productId")

        val repo = ProductRepository()
        viewModel = ViewModelProvider(
            this,
            ProductDetailViewModelFactory(repo)
        )[ProductDetailViewModel::class.java]

        productId?.let { viewModel.fetchProductById(it) }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {


        _binding = FragmentProductDetailBinding.inflate(inflater, container, false)
        return binding.root

    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // GERİ BUTONU
        binding.btnBack.setOnClickListener {
            requireActivity().onBackPressedDispatcher.onBackPressed()
        }
        binding.btnAddToCart.setOnClickListener {

            Log.d("DETAIL_ADD", "➡️ Add to Cart butonuna tıklandı")
            val p = viewModel.product.value ?: return@setOnClickListener

            if (p == null) {
                Log.e("DETAIL_ADD", "Ürün bulunamadı (ViewModel.product null)")
                return@setOnClickListener
            }

            Log.d("DETAIL_ADD", "📦 Ürün bulundu: id=${p.id}, name=${p.name}, price=${p.price}")

            cartViewModel.addProductToCart(p )

            Log.d("DETAIL_ADD", "ViewModel'e gönderildi.")
        }


        //  🔥 ÜRÜN GÖZLEMİ
        viewModel.product.observe(viewLifecycleOwner) { product ->

            if (product == null) {
                Log.e("DETAIL_DEBUG", "Ürün bulunamadı!")
                return@observe
            }


            // BAŞLIK
            binding.txtToolbarTitle.text = product.name
            binding.txtTitle.text = product.name

            // AÇIKLAMA
            binding.txtDescription.text = product.description

            // FİYAT
            binding.txtPrice.text = "${product.price} ₺"

            // GÖRSEL YÜKLEME (GLIDE)
            Glide.with(this)
                .load(product.image)
                .into(binding.imgProduct)
        }

    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
