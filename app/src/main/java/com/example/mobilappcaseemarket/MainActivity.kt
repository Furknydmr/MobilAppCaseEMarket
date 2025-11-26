package com.example.mobilappcaseemarket

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.example.mobilappcaseemarket.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {



    private lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)



        // Ekran yüksekliğini al
        val screenHeight = resources.displayMetrics.heightPixels
        val iconSize = (screenHeight * 0.06).toInt()

        // BottomNavigationView yüksekliğini %10 yapmak
        binding.bottomNav.itemIconSize = iconSize

        binding.bottomNav.requestLayout()
        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.fragmentContainerView) as NavHostFragment
        val navController = navHostFragment.navController

        // BottomNavigationView ile bağla
        binding.bottomNav.setupWithNavController(navController)



    }
}