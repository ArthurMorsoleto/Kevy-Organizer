package com.amb.kevyorganizer.data

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.amb.core.data.Product
import com.amb.core.repository.ProductRepository
import com.amb.core.usecase.GetAllProductsUseCase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class ProductListViewModel(application: Application) : AndroidViewModel(application) {

    private val coroutineScope: CoroutineScope = CoroutineScope(Dispatchers.IO)
    private val productRepository = ProductRepository(RoomProductDataSource(application))
    private val getAllProductsUseCase = GetAllProductsUseCase(productRepository)

    val productsList = MutableLiveData<List<Product>>()

    fun getAllProducts() {
        coroutineScope.launch {
            productsList.postValue(getAllProductsUseCase())
        }
    }
}