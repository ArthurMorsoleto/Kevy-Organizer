package com.amb.kevyorganizer.data

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.amb.core.data.Product
import com.amb.core.repository.ProductRepository
import com.amb.core.usecase.AddProductUseCase
import com.amb.core.usecase.GetAllProductsUseCase
import com.amb.core.usecase.GetProductUseCase
import com.amb.core.usecase.RemoveProductUseCase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class AddProductViewModel(application: Application) : AndroidViewModel(application) {

    private val coroutineScope: CoroutineScope = CoroutineScope(Dispatchers.IO)
    private val productRepository = ProductRepository(RoomProductDataSource(application))

    private val addProductUseCase = AddProductUseCase(productRepository)
    private val getAllProductsUseCase = GetAllProductsUseCase(productRepository)
    private val getProductUseCase = GetProductUseCase(productRepository)
    private val removeProductUseCase = RemoveProductUseCase(productRepository)

    val saved = MutableLiveData<Boolean>()

    val currentProduct: MutableLiveData<Product?>
        get() = _currentProduct
    private val _currentProduct = MutableLiveData<Product?>()

    fun saveProduct(product: Product) {
        coroutineScope.launch {
            addProductUseCase(product)
            saved.postValue(true)
        }
    }

    fun getProduct(id: Long) {
        coroutineScope.launch {
            val product = getProductUseCase(id)
            _currentProduct.postValue(product)
        }
    }

    fun deleteProduct(product: Product) {
        coroutineScope.launch {
            removeProductUseCase(product)
            saved.postValue(true)
        }

    }

}