package com.example.mobilappcaseemarket.ui.home

import android.content.res.Configuration
import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.SearchView
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.setFragmentResultListener
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.mobilappcaseemarket.R
import com.example.mobilappcaseemarket.data.local.AppDatabase
import com.example.mobilappcaseemarket.data.repository.FavouriteRepository
import com.example.mobilappcaseemarket.data.repository.ProductRepository
import com.example.mobilappcaseemarket.ui.cart.CartViewModel
import com.example.mobilappcaseemarket.ui.home.favourite.FavouriteViewModel
import com.google.android.material.bottomsheet.BottomSheetBehavior


class HomeFragment : Fragment() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var viewModel: HomeViewModel
    private lateinit var cartViewModel: CartViewModel
    private lateinit var favouriteViewModel: FavouriteViewModel


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_home, container, false)
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val searchView = view.findViewById<SearchView>(R.id.search)

        // ---------------------------
        // FILTER MODAL RESULT LISTENER
        // ---------------------------
        setFragmentResultListener("filter_result") { _, bundle ->
            when (bundle.getString("sort_type")) {
                "DEFAULT" -> {
                    searchView.setQuery("", false)
                    searchView.clearFocus()

                    viewModel.updateFilter(
                        viewModel.filterOptions.copy(
                            searchQuery = "",
                            sortType = SortType.NONE
                        )
                    )
                }

                "PRICE_ASC"  -> applySort(SortType.PRICE_ASC)
                "PRICE_DESC" -> applySort(SortType.PRICE_DESC)
                "NAME_ASC"   -> applySort(SortType.NAME_ASC)
                "NAME_DESC"  -> applySort(SortType.NAME_DESC)
            }
        }


        // ---------------------------
        // CART VIEWMODEL
        // ---------------------------
        cartViewModel = ViewModelProvider(
            requireActivity(),
            CartViewModel.CartViewModelFactory(requireContext())
        )[CartViewModel::class.java]

        cartViewModel.loadCart()


        // ---------------------------
        // ORIENTATION / SCREEN SIZE
        // ---------------------------
        val orientation = resources.configuration.orientation
        val screenHeight = resources.displayMetrics.heightPixels

        val imageHeight = (screenHeight * 0.14).toInt()
        val searchHeight = if (orientation == Configuration.ORIENTATION_PORTRAIT) {
            (screenHeight * 0.05).toInt()
        } else (screenHeight * 0.10).toInt()


        // ---------------------------
        // RECYCLERVIEW
        // ---------------------------
        recyclerView = view.findViewById(R.id.recyclerView)
        recyclerView.layoutManager = GridLayoutManager(requireContext(), 2)


        // ---------------------------
        // SEARCHVIEW TASARIM
        // ---------------------------
        val params = searchView.layoutParams
        params.height = searchHeight
        searchView.layoutParams = params

        val magId = searchView.context.resources.getIdentifier("android:id/search_mag_icon", null, null)
        val magImage = searchView.findViewById<ImageView>(magId)
        magImage?.setColorFilter(ContextCompat.getColor(requireContext(), R.color.gray))

        val searchEditText = searchView.findViewById<EditText>(
            resources.getIdentifier("android:id/search_src_text", null, null)
        )
        searchEditText?.apply {
            setTextColor(Color.BLACK)
            setHintTextColor(Color.GRAY)
        }


        // ---------------------------
        // FILTER BUTTON
        // ---------------------------
        val btnSelectFilter = view.findViewById<Button>(R.id.btnSelectFilter)
        btnSelectFilter.setOnClickListener {
            openFilterModalBelowButton(btnSelectFilter)
        }


        // ---------------------------
        // HOME VIEWMODEL
        // ---------------------------
        val repo = ProductRepository()
        viewModel = ViewModelProvider(
            this,
            HomeViewModelFactory(repo)
        )[HomeViewModel::class.java]


        // ---------------------------
        // ★★★ FAVOURITE VIEWMODEL OLUŞTURULDU
        // ---------------------------
        val favDao = AppDatabase.getDatabase(requireContext()).favouriteDao()
        val favRepo = FavouriteRepository(favDao)
        favouriteViewModel = FavouriteViewModel(favRepo)


        // ---------------------------
        // ADAPTER (FAVOURITE DAHİL!)
        // ---------------------------
        val adapter = ProductAdapter(
            mutableListOf(),
            imageHeight,
            favouriteViewModel = favouriteViewModel,      // ★ eklendi
            lifecycleOwner = viewLifecycleOwner,          // ★ eklendi

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


        // ---------------------------
        // SEARCH FILTERING
        // ---------------------------
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean = false

            override fun onQueryTextChange(newText: String?): Boolean {
                viewModel.updateFilter(
                    FilterOptions(
                        searchQuery = newText.orEmpty(),
                        sortType = viewModel.filterOptions.sortType
                    )
                )
                return true
            }
        })


        // ---------------------------
        // LOAD PRODUCTS
        // ---------------------------
        viewModel.fetchProducts()

        viewModel.productList.observe(viewLifecycleOwner) { list ->
            adapter.updateList(list)
        }

        addInfiniteScroll()
    }


    // ---------------------------
    // INFINITE SCROLL
    // ---------------------------
    private fun addInfiniteScroll() {
        recyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {

            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)

                if (dy <= 0) return

                val layoutManager = recyclerView.layoutManager as GridLayoutManager
                val lastVisible = layoutManager.findLastVisibleItemPosition()
                val total = layoutManager.itemCount

                if (lastVisible >= total - 2 && viewModel.isLoading.value == false) {
                    viewModel.loadNextPage()
                }
            }
        })
    }


    fun applySort(option: SortType) {
        viewModel.updateFilter(
            viewModel.filterOptions.copy(sortType = option)
        )
    }


    private fun openFilterModalBelowButton(anchorView: View) {
        val modal = FilterModalFragment()
        modal.show(parentFragmentManager, "FilterModal")

        modal.dialog?.setOnShowListener {
            val bottomSheet =
                modal.dialog!!.findViewById<View>(com.google.android.material.R.id.design_bottom_sheet)
            val behavior = BottomSheetBehavior.from(bottomSheet!!)

            val location = IntArray(2)
            anchorView.getLocationOnScreen(location)
            val anchorY = location[1] + anchorView.height

            val screenHeight = resources.displayMetrics.heightPixels
            val desiredPeek = screenHeight - anchorY

            behavior.peekHeight = desiredPeek
            behavior.isFitToContents = false
            behavior.state = BottomSheetBehavior.STATE_COLLAPSED
        }
    }
}
