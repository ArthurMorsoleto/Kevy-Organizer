package com.amb.kevyorganizer.data

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.amb.core.data.Product
import com.amb.core.usecase.GetAllProductsUseCase
import com.amb.kevyorganizer.data.di.AppModule
import com.amb.kevyorganizer.data.di.components.DaggerViewModelComponent
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

class ProductListViewModel(application: Application) : AndroidViewModel(application) {

    @Inject
    lateinit var getAllProductsUseCase: GetAllProductsUseCase

    init {
        DaggerViewModelComponent.builder()
            .appModule(AppModule(getApplication()))
            .build()
            .inject(this)
    }

    private val coroutineScope: CoroutineScope = CoroutineScope(Dispatchers.IO)

    val productsList: MutableLiveData<List<Product>>
        get() = _productsListLiveData
    private val _productsListLiveData = MutableLiveData<List<Product>>()

    fun getAllProducts() {
        coroutineScope.launch {
            _productsListLiveData.postValue(getAllProductsUseCase())
        }
    }
}