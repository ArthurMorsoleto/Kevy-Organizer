package com.amb.kevyorganizer.data.di

import android.app.Application
import com.amb.core.repository.ProductRepository
import com.amb.kevyorganizer.data.RoomProductDataSource
import dagger.Module
import dagger.Provides

@Module
class RepositoryModule {

    @Provides
    fun providesProductRepository(app: Application) = ProductRepository(RoomProductDataSource(app))
}