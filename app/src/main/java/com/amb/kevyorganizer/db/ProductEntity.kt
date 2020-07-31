package com.amb.kevyorganizer.db

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.amb.core.data.Product
import com.amb.kevyorganizer.db.ProductEntity.Companion.TABLE_NAME

@Entity(tableName = TABLE_NAME)
data class ProductEntity(
    @PrimaryKey(autoGenerate = true)
    var id: Long = 0L,

    @ColumnInfo(name = "name")
    var name: String,

    @ColumnInfo(name = "ammount")
    var ammount: Long,

    @ColumnInfo(name = "price")
    var price: Double,

    @ColumnInfo(name = "image_file")
    var imageFilePath: String,

    @ColumnInfo(name = "description")
    var description: String,

    @ColumnInfo(name = "creation_date")
    var creationTime: Long,

    @ColumnInfo(name = "update_time")
    var updateTime: Long
) {
    companion object {
        const val TABLE_NAME = "product"

        fun fromProduct(product: Product) = ProductEntity(
            name = product.name,
            ammount = product.ammount,
            price = product.price,
            description = product.description,
            imageFilePath = product.imageFilePath,
            creationTime = product.creationTime,
            updateTime = product.updateTime
        )
    }

    fun toProduct() = Product(
        id = id,
        name = name,
        ammount = ammount,
        price = price,
        imageFilePath = imageFilePath,
        description = description,
        creationTime = creationTime,
        updateTime = updateTime
    )
}