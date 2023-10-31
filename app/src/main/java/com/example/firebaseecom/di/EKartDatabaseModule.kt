package com.example.firebaseecom.di

import android.app.Application
import com.example.firebaseecom.local.EkartDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)

object RoomDatabaseModule {

    @Singleton
    @Provides
    fun provideDatabase(application: Application) = EkartDatabase.getDatabase(application)

    @Singleton
    @Provides
    fun provideProductsDao(database: EkartDatabase) = database.productDao()
}
