package com.amb.core.repository

import com.amb.core.data.Product

class ProductRepository(private val dataSource: ProductDataSource) {

    suspend fun addProduct(product: Product) = dataSource.add(product)

    suspend fun getProduct(id: Long) = dataSource.get(id)

    suspend fun getAllProducts() = dataSource.getAll()

    suspend fun removeProduct(product: Product) = dataSource.remove(product)
}