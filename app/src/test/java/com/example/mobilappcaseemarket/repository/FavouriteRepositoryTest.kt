package com.example.mobilappcaseemarket.data.repository

import com.example.mobilappcaseemarket.data.FakeFavouriteDao
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Assert.assertFalse
import org.junit.Test

class FavouriteRepositoryTest {

    private val fakeDao = FakeFavouriteDao()
    private val repo = FavouriteRepository(fakeDao)

    // 1) Favoriye ekleniyor mu?
    @Test
    fun addToFavourite_shouldAdd() = runBlocking {
        repo.addToFavourite("1")

        val result = repo.getAllFavourites()
        assertEquals(1, result.size)
        assertEquals("1", result[0].productId)
    }

    // 2) Favoriden çıkarılıyor mu?
    @Test
    fun removeFromFavourite_shouldRemove() = runBlocking {
        repo.addToFavourite("2")
        repo.removeFromFavourite("2")

        val result = repo.getAllFavourites()
        assertEquals(0, result.size)
    }

    // 3) Favori mi kontrolü doğru çalışıyor mu?
    @Test
    fun isFavourite_shouldReturnTrue() = runBlocking {
        repo.addToFavourite("5")

        val isFav = repo.isFavourite("5")
        assertTrue(isFav)
    }

    @Test
    fun isFavourite_shouldReturnFalse() = runBlocking {
        repo.addToFavourite("5")

        val isFav = repo.isFavourite("99")
        assertFalse(isFav)
    }

    // 4) Tüm favorileri getiriyor mu?
    @Test
    fun getAllFavourites_shouldReturnAll() = runBlocking {
        repo.addToFavourite("1")
        repo.addToFavourite("2")
        repo.addToFavourite("3")

        val list = repo.getAllFavourites()
        assertEquals(3, list.size)
    }
}
