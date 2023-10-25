package com.example.firebaseecom.di

import com.example.firebaseecom.repositories.AuthRepository
import com.example.firebaseecom.repositories.AuthRepositoryImpl
import com.example.firebaseecom.repositories.FirestoreRepository
import com.example.firebaseecom.repositories.FirestoreRepositoryImpl
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
    fun provideAuthRepository(repository: AuthRepositoryImpl): AuthRepository = repository

    @Provides
    fun provideFirestoreRepository(repository: FirestoreRepositoryImpl): FirestoreRepository = repository

}