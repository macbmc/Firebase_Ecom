package com.example.firebaseecom.di

import com.example.firebaseecom.AuthRepository
import com.example.firebaseecom.AuthRepositoryImpl
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object FirebaseModule {
    @Provides
    @Singleton
    fun provideFirestore()=FirebaseFirestore.getInstance()

    @Provides
    @Singleton
    fun provideAuth():FirebaseAuth= FirebaseAuth.getInstance()

    @Provides
    fun provideAuthRepository(repository: AuthRepositoryImpl):AuthRepository = repository

}