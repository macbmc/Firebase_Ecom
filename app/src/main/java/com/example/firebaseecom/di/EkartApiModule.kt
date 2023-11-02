package com.example.firebaseecom.di

import com.example.firebaseecom.api.EkartApi
import com.example.firebaseecom.api.EkartApiEndPoints
import com.example.firebaseecom.repositories.NetworkRepository
import com.example.firebaseecom.repositories.NetworkRepositoryImpl
import com.squareup.moshi.Moshi
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import retrofit2.converter.moshi.MoshiConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object EkartApiModule {

    @Provides
    @Singleton
    fun provideRetrofit() : EkartApi = Retrofit.Builder()
        .baseUrl(EkartApiEndPoints.END_POINT_BASE.url)
        .addConverterFactory(
            MoshiConverterFactory.create(
                Moshi.Builder().add(KotlinJsonAdapterFactory()).build()
            )
        )
        .build()
        .create(EkartApi::class.java)

    @Provides
    @Singleton
    fun provideNetworkRepository(repository:NetworkRepositoryImpl):NetworkRepository = repository
}
