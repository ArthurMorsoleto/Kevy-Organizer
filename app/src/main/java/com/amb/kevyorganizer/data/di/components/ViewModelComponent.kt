package com.amb.kevyorganizer.data.di.components

import com.amb.kevyorganizer.data.ProductDetailsViewModel
import com.amb.kevyorganizer.data.ProductListViewModel
import com.amb.kevyorganizer.data.di.AppModule
import com.amb.kevyorganizer.data.di.RepositoryModule
import com.amb.kevyorganizer.data.di.UseCasesModule
import dagger.Component

@Component(modules = [AppModule::class, RepositoryModule::class, UseCasesModule::class])
interface ViewModelComponent {

    fun inject(productDetailsViewModel: ProductDetailsViewModel)

    fun inject(productListViewModel: ProductListViewModel)
}