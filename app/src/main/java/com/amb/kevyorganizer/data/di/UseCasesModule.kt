package com.amb.kevyorganizer.data.di

import com.amb.core.repository.ProductRepository
import com.amb.core.usecase.AddProductUseCase
import com.amb.core.usecase.GetAllProductsUseCase
import com.amb.core.usecase.GetProductUseCase
import com.amb.core.usecase.RemoveProductUseCase
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
}