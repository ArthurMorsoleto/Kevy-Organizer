package com.amb.core.usecase

import com.amb.core.repository.ProductRepository

class GetAllProductsUseCase(
    private val productRepository: ProductRepository
) {
    suspend operator fun invoke() = productRepository.getAllProducts()
}