package com.example.mobilappcaseemarket.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.fragment.app.setFragmentResultListener
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.mobilappcaseemarket.R
import com.example.mobilappcaseemarket.databinding.FragmentHomeBinding
import com.example.mobilappcaseemarket.ui.cart.CartViewModel
import com.example.mobilappcaseemarket.ui.home.favourite.FavouriteViewModel
import com.google.android.material.bottomsheet.BottomSheetBehavior


class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!


    //lateinit diyoruz çünkü onCreateView içinde bağlayacağız, şu anda null değil ama daha tanımlanmadı.
    //private lateinit var recyclerView: RecyclerView --- recyclerView artık binding üzerinden erişilebileceği için gereksiz
    private lateinit var viewModel: HomeViewModel
    private lateinit var cartViewModel: CartViewModel
    private lateinit var favouriteViewModel: FavouriteViewModel


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) { //Bu fonksiyon ekran oluşturulduktan sonra çağrılır.
        super.onViewCreated(view, savedInstanceState)

        cartViewModel = ViewModelProvider(
            //Bu yüzden ViewModel Activity scope içinde tutulur → böylece her fragment’dan erişilebilir.
            //requireActivity: Bu fragment'ın bağlı olduğu Activity’yi getir, ben bu Activity üzerinden ViewModel istiyorum
            requireActivity(), //Çünkü SEPET (Cart) ekranlar arasında ORTAKTIR. requireActivity() ile oluşturulur.
            CartViewModel.CartViewModelFactory(requireContext())
        )[CartViewModel::class.java]

        favouriteViewModel = ViewModelProvider(
            requireActivity(),
            FavouriteViewModel.FavouriteViewModelFactory(requireContext())
        )[FavouriteViewModel::class.java]

        viewModel = ViewModelProvider(
            this,
            HomeViewModel.HomeViewModelFactory()
        )[HomeViewModel::class.java]


        val screenHeight = resources.displayMetrics.heightPixels
        val imageHeight = (screenHeight * 0.2).toInt()
        val adapter = ProductAdapter( //Yani adapter pasif bir UI katmanıdır.
            imageHeight,
            onItemClick = { product ->
                val bundle = Bundle()
                bundle.putString("productId", product.id)
                findNavController().navigate(R.id.fragment_productdetail, bundle)
            },

            onAddClick = { product ->
                cartViewModel.addProductToCart(product)
            },
            onFavouriteClick = { product ->
                favouriteViewModel.toggleFavourite(product.id)
            }
        )
        binding.recyclerView.layoutManager = GridLayoutManager(requireContext(), 2)
        binding.recyclerView.adapter = adapter



        viewModel.productList.observe(viewLifecycleOwner) { list ->
            adapter.submitList(list)
        }

        favouriteViewModel.favourites.observe(viewLifecycleOwner) { favSet ->
            adapter.updateFavourites(favSet)
        }

        viewModel.isLoading.observe(viewLifecycleOwner) { isLoading ->
            binding.progressBar.isVisible = isLoading
            binding.recyclerView.isVisible = !isLoading

        }



        viewModel.fetchProducts()

        //Eğer herhangi bir fragment ‘filter_result’ anahtarıyla bir sonuç gönderirse, ben bunu dinleyeceğim ve çalışacağım.
        setFragmentResultListener("filter_result") { _, bundle -> //Bu fonksiyon, bir Fragment’ın başka bir Fragment’tan sonuç dinlemesine yarar
            when (bundle.getString("sort_type")) {
                "DEFAULT" -> {
                    binding.searchInput.setText("")
                    binding.searchInput.clearFocus()

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


        binding.btnSelectFilter.setOnClickListener {
            openFilterModalBelowButton(binding.btnSelectFilter)
        }

        binding.searchInput.addTextChangedListener { text ->
            val newOptions = viewModel.filterOptions.copy(
                searchQuery = text.toString()
            )

            viewModel.updateFilter(newOptions)

        }

        //search bar
        /*
        val orientation = resources.configuration.orientation
        val searchHeight = if (orientation == Configuration.ORIENTATION_PORTRAIT) {
            (screenHeight * 0.05).toInt()
        } else (screenHeight * 0.10).toInt()

        val magId = searchView.context.resources.getIdentifier("android:id/search_mag_icon", null, null)
        val magImage = searchView.findViewById<ImageView>(magId)
        val searchEditText = searchView.findViewById<EditText>(
            resources.getIdentifier("android:id/search_src_text", null, null)
        )

        magImage?.setColorFilter(ContextCompat.getColor(requireContext(), R.color.gray))
        searchEditText?.apply {
            setTextColor(Color.BLACK)
            setHintTextColor(Color.GRAY)
        }

        val params = searchView.layoutParams //Bu satır SearchView’in şu anki layout parametrelerini alır.
        params.height = searchHeight
        searchView.layoutParams = params

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

*/

        addInfiniteScroll()
    }



    private fun addInfiniteScroll() {
        binding.recyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {

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

        //Bu kısım normal. BottomSheet açılıyor.
        val modal = FilterModalFragment()
        modal.show(parentFragmentManager, "FilterModal")




        modal.dialog?.setOnShowListener { //Çünkü bottom sheet inflated olmadan boyutunu alamazsın.
            val bottomSheet =

                //Material Design’ın bottom sheet layout’unu direkt DOM’dan çekiyorsun.
                modal.dialog!!.findViewById<View>(com.google.android.material.R.id.design_bottom_sheet)
            val behavior = BottomSheetBehavior.from(bottomSheet!!)

            //BottomSheet’in tam olarak nereye kadar aşağıdan başlaması gerektiğini hesaplıyorsun.
            val location = IntArray(2)
            anchorView.getLocationOnScreen(location)
            val anchorY = location[1] + anchorView.height

            val screenHeight = resources.displayMetrics.heightPixels
            val desiredPeek = screenHeight - anchorY

            behavior.peekHeight = desiredPeek //BottomSheet ilk açıldığında butonun hemen altından başlasın.
            behavior.isFitToContents = false //BottomSheet’in içeriğe göre değil senin istediğin yüksekliğe göre çalışmasını sağlar.
            behavior.state = BottomSheetBehavior.STATE_COLLAPSED //Sheet en alttan (collapsed halde) açılıyor.
        }
    }
}
