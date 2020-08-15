package com.amb.core.usecase

import com.amb.core.repository.ProductRepository

class UpdateProductPhotoUseCase(
    private val productRepository: ProductRepository
) {
    suspend operator fun invoke(id: Long, filePath: String) = productRepository.updateProductPhoto(id, filePath)
}