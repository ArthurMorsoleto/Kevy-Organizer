package com.amb.kevyorganizer.db

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy.REPLACE
import androidx.room.Query
import com.amb.kevyorganizer.db.ProductEntity.Companion.TABLE_NAME

@Dao
interface ProductDao {
    @Insert(onConflict = REPLACE)
    suspend fun addProductEntity(productEntity: ProductEntity)

    @Delete
    suspend fun deleteProductEntity(productEntity: ProductEntity)

    @Query("SELECT * FROM $TABLE_NAME WHERE id == :id")
    suspend fun getProductEntity(id: Long): ProductEntity?

    @Query("SELECT * FROM $TABLE_NAME")
    suspend fun getAllProductsEntities(): List<ProductEntity>
}
