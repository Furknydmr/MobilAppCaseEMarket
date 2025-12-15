package com.example.mobilappcaseemarket.ui.cart

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import com.example.mobilappcaseemarket.databinding.FragmentCartBinding

class CartFragment : Fragment() {

    private lateinit var binding: FragmentCartBinding //İçindeki tüm view’lara ulaşmanı sağlar
    //Benim bir ViewModel’im olacak ama henüz oluşturmuyorum. onViewCreated içinde factory ile oluşturacağım.
    private lateinit var cartViewModel: CartViewModel
    //Benim bu Fragment için bir CartAdapter’ım olacak ama daha oluşturmadım.
    private lateinit var adapter: CartAdapter


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

        cartViewModel.loadCart()
    }

    private fun setupViewModel() { //CartFragment açıldığında CartViewModel örneğini oluştur ve Fragment’a bağla.

        cartViewModel = ViewModelProvider( //Bir ViewModelProvider oluşturuyorum. Bu provider bana ViewModel instance’ını verecek.
            //Aynı Activity içindeki tüm fragmentler AYNI ViewModel’i paylaşır.
            //Sepete bir ürünü başka fragmenttan eklediğinde CartFragment açıldığında aynı ViewModel’den veri gelir.
            requireActivity(), //Bu ViewModel, Activity kapsamına (scope) sahip olsun.
            CartViewModel.CartViewModelFactory(requireContext())
        )[CartViewModel::class.java]
    }


    //RecyclerView’i tamamen çalışır hale getir:
    //Adapter’ı oluştur, callbackleri bağla, layout’u ayarla ve UI’ya bağla
    private fun setupRecyclerView() {
        adapter = CartAdapter( //CartAdapter örneğini oluşturuyorum
            mutableListOf(), //Çünkü gerçek veri DB’den loadCart() ile gelecek.
            onIncrease = { cartViewModel.increaseQuantity(it) },
            onDecrease = { cartViewModel.decreaseQuantity(it) }
        )

        //Listeyi 1 kolonlu dikey grid halinde göster.
        binding.rvCart.layoutManager = GridLayoutManager(requireContext(), 1)
        binding.rvCart.adapter = adapter
    }


    //ViewModel’den gelen LiveData değişimlerini dinle ve UI’yı güncelle.
    private fun observeViewModel() {

        //viewLifecycleOwner: Fragment’ın view’ı yaşadığı sürece gözlemle. View yok olursa gözlemlemeyi bırak.

        //Eğer this yazsaydın (hata):
        //Fragment destroy olurken UI yok olur
        //Ama observer yaşamaya devam eder
        //Memory leak olur
        //NullPointerException fırlayabilir
        cartViewModel.cartItems.observe(viewLifecycleOwner) { list ->
            adapter.updateList(list)

            val total = cartViewModel.calculateTotalPrice(list)
            binding.txtTotalPrice.text = "%.2f ₺".format(total)
        }
    }
}
