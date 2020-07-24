package com.amb.core.usecase

import com.amb.core.data.Product
import com.amb.core.repository.ProductRepository

class RemoveProductUseCase(
    private val productRepository: ProductRepository
) {
    suspend operator fun invoke(product: Product) = productRepository.removeProduct(product)
}