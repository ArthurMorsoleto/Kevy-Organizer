package com.amb.core.usecase

import com.amb.core.repository.ProductRepository

class GetProductUseCase(
    private val productRepository: ProductRepository
) {
    suspend operator fun invoke(id: Long) = productRepository.getProduct(id)
}