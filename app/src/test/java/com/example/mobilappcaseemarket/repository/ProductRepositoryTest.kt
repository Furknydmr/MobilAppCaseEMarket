package com.example.mobilappcaseemarket.repository

import com.example.mobilappcaseemarket.data.FakeProductApi
import com.example.mobilappcaseemarket.data.FakeProductRepository
import junit.framework.TestCase
import kotlinx.coroutines.runBlocking
import org.junit.Test

class ProductRepositoryTest {

    private val fakeApi = FakeProductApi()
    private val fakeRepo = FakeProductRepository(fakeApi)

    @Test
    fun testGetProducts() = runBlocking {
        val result = fakeRepo.getProducts()

        TestCase.assertEquals(2, result.size)
        TestCase.assertEquals("iPhone", result[0].name)
    }

    @Test
    fun testGetProductById() = runBlocking {
        val product = fakeRepo.getProductById("2")

        TestCase.assertEquals("Samsung", product.name)
    }

}