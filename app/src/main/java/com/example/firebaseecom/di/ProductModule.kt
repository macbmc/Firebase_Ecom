package com.example.firebaseecom.di

import com.example.firebaseecom.repositories.ProductRepository
import com.example.firebaseecom.repositories.ProductRepositoryImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
object ProductModule {

    @Provides
    fun providesProductRepository(repository: ProductRepositoryImpl):ProductRepository=repository
}