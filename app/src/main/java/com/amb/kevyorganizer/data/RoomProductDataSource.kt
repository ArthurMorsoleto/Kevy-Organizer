package com.amb.kevyorganizer.data

import android.content.Context
import com.amb.core.data.Product
import com.amb.core.repository.ProductDataSource
import com.amb.kevyorganizer.db.DataBaseService
import com.amb.kevyorganizer.db.ProductEntity

class RoomProductDataSource(context: Context) : ProductDataSource {

    private val productDao = DataBaseService.getInstance(context).productDao()

    override suspend fun add(product: Product) {
        return productDao.addProductEntity(ProductEntity.fromProduct(product))
    }

    override suspend fun get(id: Long): Product? {
        return productDao.getProductEntity(id)?.toProduct()
    }

    override suspend fun getAll(): List<Product> {
        return productDao.getAllProductsEntities().map { it.toProduct() }
    }

    override suspend fun remove(product: Product) {
        return productDao.deleteProductEntity(ProductEntity.fromProduct(product))
    }

    override suspend fun updateProductPhoto(id: Long, filePath: String) {
        return productDao.updateProductPhoto(id, filePath)
    }

}