package com.example.firebaseecom.di

import com.example.firebaseecom.profile.EditProfileActivity
import com.example.firebaseecom.profile.UserProfileActivity
import com.example.firebaseecom.repositories.AuthRepositoryImpl
import com.example.firebaseecom.repositories.ProductRepository
import com.example.firebaseecom.repositories.ProductRepositoryImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
object EkartAppModule {

    @Provides
    fun providesProductRepository(repository: ProductRepositoryImpl):ProductRepository=repository

    @Provides
    fun provideAuthStateChange(): AuthRepositoryImpl.AuthStateChange {
        return EditProfileActivity().AuthStateChangeImpl()
    }

}