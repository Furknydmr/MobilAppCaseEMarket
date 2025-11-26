package com.example.mobilappcaseemarket.ui.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.mobilappcaseemarket.data.repository.ProductRepository

class HomeViewModelFactory (

        private val repository: ProductRepository ): ViewModelProvider.Factory{

        override fun <T: ViewModel> create(modelClass: Class<T>): T {
            return HomeViewModel(repository) as T

        }
    }
