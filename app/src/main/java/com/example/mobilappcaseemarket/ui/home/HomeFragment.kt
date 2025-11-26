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
import com.example.mobilappcaseemarket.MainActivity
import com.example.mobilappcaseemarket.R
import com.example.mobilappcaseemarket.data.repository.ProductRepository

class HomeFragment : Fragment() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var viewModel: HomeViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {


        return inflater.inflate(R.layout.fragment_home, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

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

        // --- API'DEN VERİYİ ÇEK ---
        viewModel.fetchProducts()


        // --- GÖZLEMLE ---

        viewModel.productList.observe(viewLifecycleOwner) { list ->

            Log.d("HOME_DEBUG", "Product list received: ${list.size} items")
            Log.d("HOME_DEBUG", "Creating adapter with imageHeight: $imageHeight")

            recyclerView.adapter = ProductAdapter(list, imageHeight) { product ->

                Log.d("HOME_DEBUG", "Clicked product: ${product.name}")
                // TODO: detay ekranına git
            }
        }



    }
}
