package com.amb.kevyorganizer.data

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.amb.core.data.Product
import com.amb.core.usecase.AddProductUseCase
import com.amb.core.usecase.GetProductUseCase
import com.amb.core.usecase.RemoveProductUseCase
import com.amb.kevyorganizer.data.di.AppModule
import com.amb.kevyorganizer.data.di.components.DaggerViewModelComponent
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

class ProductDetailsViewModel(application: Application) : AndroidViewModel(application) {

    @Inject
    lateinit var addProductUseCase: AddProductUseCase

    @Inject
    lateinit var getProductUseCase: GetProductUseCase

    @Inject
    lateinit var removeProductUseCase: RemoveProductUseCase

    init {
        DaggerViewModelComponent.builder()
            .appModule(AppModule(getApplication()))
            .build()
            .inject(this)
    }

    private val coroutineScope: CoroutineScope = CoroutineScope(Dispatchers.IO)

    val saved: MutableLiveData<Boolean>
        get() = _saved
    private val _saved = MutableLiveData<Boolean>()

    val currentProduct: MutableLiveData<Product?>
        get() = _currentProduct
    private val _currentProduct = MutableLiveData<Product?>()

    fun saveProduct(product: Product) {
        coroutineScope.launch {
            addProductUseCase(product)
            _saved.postValue(true)
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
            _saved.postValue(true)
        }
    }

}