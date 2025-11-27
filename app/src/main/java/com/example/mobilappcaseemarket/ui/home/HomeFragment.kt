package com.example.mobilappcaseemarket.ui.home

import android.content.res.Configuration
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.widget.EditText
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import android.widget.SearchView
import androidx.core.content.ContextCompat
import androidx.lifecycle.MutableLiveData
import androidx.navigation.fragment.findNavController
import com.example.mobilappcaseemarket.MainActivity
import com.example.mobilappcaseemarket.R
import com.example.mobilappcaseemarket.data.local.AppDatabase
import com.example.mobilappcaseemarket.data.model.Product
import com.example.mobilappcaseemarket.data.repository.CartRepository
import com.example.mobilappcaseemarket.data.repository.ProductRepository
import com.example.mobilappcaseemarket.ui.cart.CartViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class HomeFragment : Fragment() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var viewModel: HomeViewModel
    private lateinit var cartViewModel: CartViewModel
    val productList = MutableLiveData<MutableList<Product>>()
    private var userIsScrolling = false



    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {


        return inflater.inflate(R.layout.fragment_home, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        //  1) ViewModel'i Activity scope'unda oluşturuyoruz
        // Böylece Home, Detail ve Cart ekranları aynı sepet verisini paylaşır
        cartViewModel = ViewModelProvider(
            requireActivity(),
            CartViewModel.CartViewModelFactory(requireContext())
        )[CartViewModel::class.java]

        //Uygulama açılır açılmaz mevcut sepet durumunu yükler
        cartViewModel.loadCart()



        val orientation = resources.configuration.orientation

        val screenHeight = resources.displayMetrics.heightPixels
        val imageHeight = (screenHeight * 0.14).toInt()
        val searchHeight = if (orientation == Configuration.ORIENTATION_PORTRAIT) {
            (screenHeight * 0.05).toInt()
        } else {
            (screenHeight * 0.10).toInt()
        }

        Log.d("HOME_DEBUG", "Screen Height: $screenHeight")
        Log.d("HOME_DEBUG", "Image Height (%20): $imageHeight")



        // --- RECYCLER VIEW ---
        recyclerView = view.findViewById(R.id.recyclerView)
        recyclerView.layoutManager = GridLayoutManager(requireContext(), 2)

        // --- SEARCH VIEW RENK AYARI ---
        val searchView = view.findViewById<SearchView>(R.id.search)
        val params = searchView.layoutParams
        params.height = searchHeight
        searchView.layoutParams = params
        val magId = searchView.context.resources.getIdentifier("android:id/search_mag_icon", null, null)
        val magImage = searchView.findViewById<ImageView>(magId)
        // 🎨 İkon rengini değiştir
        magImage?.setColorFilter(ContextCompat.getColor(requireContext(), R.color.gray))   // istediğin renk

        val searchEditText = searchView.findViewById<EditText>(
            resources.getIdentifier("android:id/search_src_text", null, null)
        )

        searchEditText?.apply {
            setTextColor(Color.BLACK)
            setHintTextColor(Color.GRAY)
        }

        // --- VIEWMODEL BAĞLAMA ---
        val repo = ProductRepository()
        viewModel = ViewModelProvider(
            this,
            HomeViewModelFactory(repo)
        )[HomeViewModel::class.java]

        val adapter = ProductAdapter(
            mutableListOf(),
            imageHeight,
            onItemClick = { product ->
                val bundle = Bundle()
                bundle.putString("productId", product.id)
                findNavController().navigate(R.id.fragment_productdetail, bundle)
            },
            onAddClick = { product ->
                cartViewModel.addProductToCart(product)
            }
        )

        recyclerView.adapter = adapter


        // --- API'DEN VERİYİ ÇEK ---
        viewModel.fetchProducts()


        // --- GÖZLEMLE ---

        viewModel.productList.observe(viewLifecycleOwner) { list ->
            Log.d("HOME_DEBUG", "Product list updated: ${list.size} items")

            val adapter = recyclerView.adapter as ProductAdapter
            adapter.updateList(list)
        }

        addInfiniteScroll()



    }
    private fun addInfiniteScroll() {
        recyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {

            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)

                if (newState == RecyclerView.SCROLL_STATE_DRAGGING) {
                    userIsScrolling = true
                }
            }

            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)

                if (!userIsScrolling) return  // ❗ Kullanıcı gerçekten scroll etmeden eklenmesi engellendi!

                val layoutManager = recyclerView.layoutManager as GridLayoutManager
                val visibleItemCount = layoutManager.childCount
                val totalItemCount = layoutManager.itemCount
                val firstVisibleItem = layoutManager.findFirstVisibleItemPosition()

                val reachedEnd = firstVisibleItem + visibleItemCount >= totalItemCount - 4

                if (reachedEnd) {
                    Log.d("SCROLL", "KULLANICI scroll ile SONa geldi → loadNextPage()")

                    userIsScrolling = false  // ❗ bir kere tetiklenmesini sağlıyor
                    viewModel.loadNextPage()
                }
            }
        })
    }


}
