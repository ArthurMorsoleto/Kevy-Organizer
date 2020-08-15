package com.amb.kevyorganizer.data.di

import com.amb.core.repository.ProductRepository
import com.amb.core.usecase.*
import dagger.Module
import dagger.Provides

@Module
class UseCasesModule {

    @Provides
    fun providesGetProductUseCase(repository: ProductRepository) = GetProductUseCase(repository)

    @Provides
    fun providesGetAllProductsUseCase(repository: ProductRepository) = GetAllProductsUseCase(repository)

    @Provides
    fun providesAddProductUseCase(repository: ProductRepository) = AddProductUseCase(repository)

    @Provides
    fun providesRemoveProductUseCase(repository: ProductRepository) = RemoveProductUseCase(repository)

    @Provides
    fun providesUpdateProductPhotoUseCase(repository: ProductRepository) = UpdateProductPhotoUseCase(repository)
}