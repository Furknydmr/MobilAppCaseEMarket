package com.example.mobilappcaseemarket

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.example.mobilappcaseemarket.databinding.ActivityMainBinding
import com.example.mobilappcaseemarket.ui.cart.CartViewModel

class MainActivity : AppCompatActivity() {



    private lateinit var binding: ActivityMainBinding

    private lateinit var cartViewModel: CartViewModel

    override fun onCreate(savedInstanceState: Bundle?) { //Activity ilk açıldığında çalışan fonksiyon.
        super.onCreate(savedInstanceState) //AppCompatActivity’nin kendi init işlemlerini yapmasını sağlar.

        binding = ActivityMainBinding.inflate(layoutInflater) //XML'i Kotlin tarafına bağlar,
        setContentView(binding.root) //Activity'nin ekrana çizilecek ana layout’u.

        cartViewModel = ViewModelProvider(
            this,
            CartViewModel.CartViewModelFactory(this)
        ).get(CartViewModel::class.java)

        cartViewModel.loadCart()

        val screenHeight = resources.displayMetrics.heightPixels
        val iconSize = (screenHeight * 0.04).toInt()
        binding.bottomNav.itemIconSize = iconSize
        binding.bottomNav.requestLayout() //Bottom navigation’ın yeni ölçülere göre yeniden çizilmesini ister.

        //XML’deki FragmentContainerView’i bulur. Bu container, navigation graph’ındaki fragment’leri yönetir.
        val navHostFragment =
            supportFragmentManager.findFragmentById(binding.fragmentContainerView.id) as NavHostFragment



        val navController = navHostFragment.navController //Navigation Graph’ı yöneten controller.

        //BottomNavigationView ile navController bağlanır. Yani menüdeki item’a basınca ilgili fragment açılır.
        binding.bottomNav.setupWithNavController(navController)


        cartViewModel.cartCount.observe(this) { count -> //cartCount LiveData’sı değiştiğinde UI otomatik güncellenir.

            val badge = binding.bottomNav.getOrCreateBadge(R.id.nav_cart) //Cart ikonunun üstüne rozet (badge) oluşturur.

            if (count > 0) {
                badge.isVisible = true
                badge.number = count
                badge.badgeTextColor = ContextCompat.getColor(this, R.color.white) // bu şekilde kendi rengini kullancan
                badge.backgroundColor = ContextCompat.getColor(this, android.R.color.holo_red_light)
            } else {
                badge.isVisible = false  //Eğer sepet boşsa badge gizleniyor.
            }
        }
    }
}