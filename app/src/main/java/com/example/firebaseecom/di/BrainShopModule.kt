package com.example.firebaseecom.di

import com.example.firebaseecom.api.EkartApiEndPoints
import com.example.firebaseecom.api.EkartApiService
import com.example.firebaseecom.brainShop.BrainShopApiEndPoints
import com.example.firebaseecom.brainShop.BrainShopService
import com.example.firebaseecom.repositories.BrainShopRepository
import com.example.firebaseecom.repositories.BrainShopRepositoryImpl
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)

object BrainShopModule {

    @Provides
    @Singleton
    fun provideBrainRetrofit():BrainShopService{
        return Retrofit.Builder()
            .baseUrl(BrainShopApiEndPoints.BRAIN_SHOP_BASE_URL.url)
            //.client(okHttpClient)
            .addConverterFactory(
                MoshiConverterFactory.create(
                    Moshi.Builder().add(KotlinJsonAdapterFactory()).build()
                )
            )
            .build()
            .create(BrainShopService::class.java)
    }

    @Provides
    fun provideBrainRepository(impl:BrainShopRepositoryImpl):BrainShopRepository = impl
}