package com.amb.core.data

class Product(
    var id: Long = 0,
    var name: String,
    var ammount: Long,
    var price: Double,
    var imageFilePath: String,
    var description: String,
    var creationTime: Long,
    var updateTime: Long
)