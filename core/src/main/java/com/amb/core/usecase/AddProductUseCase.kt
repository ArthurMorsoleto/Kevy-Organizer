package com.amb.core.usecase

import com.amb.core.data.Product
import com.amb.core.repository.ProductRepository

class AddProductUseCase(
    private val productRepository: ProductRepository
) {
    suspend operator fun invoke(product: Product) = productRepository.addProduct(product)
}
