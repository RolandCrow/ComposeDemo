package com.example.compose_room.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.compose_room.dao.ProductDao
import com.example.compose_room.entities.Product
import kotlinx.coroutines.*

class ProductRepository(private val productDao: ProductDao) {
    val allProducts: LiveData<List<Product>> = productDao.getAllProducts()
    val searchResults = MutableLiveData<List<Product>>()

    private val coroutineScope = CoroutineScope(Dispatchers.Main)

    fun insertProduct(newProduct: Product) {
        coroutineScope.launch(Dispatchers.IO) {
            productDao.insertProduct(newProduct)
        }
    }

    fun deleteProduct(name: String) {
        coroutineScope.launch(Dispatchers.IO) {
            productDao.deleteProduct(name)
        }
    }

    fun findProduct(name: String) {
        coroutineScope.launch(Dispatchers.Main) {
            searchResults.value = asyncFind(name).await()
        }
    }

    private fun asyncFind(name: String): Deferred<List<Product>> {
        return coroutineScope.async(Dispatchers.IO) {
            productDao.findProduct(name)
        }
    }
}