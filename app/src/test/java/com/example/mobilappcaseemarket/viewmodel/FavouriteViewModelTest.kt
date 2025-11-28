package com.example.mobilappcaseemarket.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.example.mobilappcaseemarket.MainDispatcherRule
import com.example.mobilappcaseemarket.data.FakeFavouriteRepository
import com.example.mobilappcaseemarket.ui.home.favourite.FavouriteViewModel
import com.example.mobilappcaseemarket.utils.getOrAwaitValue
import junit.framework.TestCase.assertEquals
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@ExperimentalCoroutinesApi
class FavouriteViewModelTest {

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    private lateinit var viewModel: FavouriteViewModel
    private lateinit var fakeRepository: FakeFavouriteRepository

    @Before
    fun setup() {
        fakeRepository = FakeFavouriteRepository()
        viewModel = FavouriteViewModel(fakeRepository)
    }

    @Test
    fun `toggleFavourite adds item when not favourite`() = runTest {
        // Act
        viewModel.toggleFavourite("10")

        // Advance coroutines (CRITICAL)
        advanceUntilIdle()

        // Assert
        val result = viewModel.favourites.getOrAwaitValue()
        assertEquals(setOf("10"), result)
    }
}

