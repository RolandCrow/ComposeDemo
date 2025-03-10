package com.example.compose_room.viewmodel

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.compose_room.ProductRoomDatabase
import com.example.compose_room.entities.Product
import com.example.compose_room.repository.ProductRepository

class MainViewModel(application: Application): ViewModel() {
    val allProducts: LiveData<List<Product>>
    private val repository: ProductRepository
    val searchResult: MutableLiveData<List<Product>>

    init {
        val productDb = ProductRoomDatabase.getInstance(application)
        val productDao = productDb.productDao()

        repository = ProductRepository(productDao)
        allProducts= repository.allProducts
        searchResult = repository.searchResults
    }

    fun insertProduct(product: Product) {
        repository.insertProduct(product)
    }
    fun findProduct(name: String) {
        repository.findProduct(name)
    }
    fun deleteProduct(name:String) {
        repository.deleteProduct(name)
    }
}