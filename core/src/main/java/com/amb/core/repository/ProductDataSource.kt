package com.amb.core.repository

import com.amb.core.data.Product

interface ProductDataSource {

    suspend fun add(product: Product)

    suspend fun get(id: Long): Product?

    suspend fun getAll(): List<Product>

    suspend fun remove(product: Product)
}