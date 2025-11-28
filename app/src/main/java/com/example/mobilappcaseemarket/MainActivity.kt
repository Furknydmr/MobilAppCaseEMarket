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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val screenHeight = resources.displayMetrics.heightPixels
        val iconSize = (screenHeight * 0.04).toInt()

        binding.bottomNav.itemIconSize = iconSize

        binding.bottomNav.requestLayout()
        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.fragmentContainerView) as NavHostFragment

        cartViewModel = ViewModelProvider(
            this,
            CartViewModel.CartViewModelFactory(this)
        ).get(CartViewModel::class.java)

        val navController = navHostFragment.navController

        binding.bottomNav.setupWithNavController(navController)

        cartViewModel.cartCount.observe(this) { count ->

            val badge = binding.bottomNav.getOrCreateBadge(R.id.nav_cart)

            if (count > 0) {
                badge.isVisible = true
                badge.number = count
                badge.badgeTextColor = ContextCompat.getColor(this, android.R.color.white)
                badge.backgroundColor = ContextCompat.getColor(this, android.R.color.holo_red_light)
            } else {
                badge.isVisible = false
            }
        }
        cartViewModel.loadCart()




    }
}