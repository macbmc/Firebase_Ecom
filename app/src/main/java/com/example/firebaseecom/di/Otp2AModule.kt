package com.example.firebaseecom.di

import com.example.firebaseecom.otp2FA.OtpEndPoints
import com.example.firebaseecom.otp2FA.OtpService
import com.example.firebaseecom.repositories.OtpRepository
import com.example.firebaseecom.repositories.OtpRepositoryImpl
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
object Otp2AModule {

    @Singleton
    @Provides
    fun provideOtp2A() : OtpService{
        return Retrofit.Builder()
            .baseUrl(OtpEndPoints.OTP_BASE_URL.url)
            //.client(okHttpClient)
            .addConverterFactory(
                MoshiConverterFactory.create(
                    Moshi.Builder().add(KotlinJsonAdapterFactory()).build()
                )
            )
            .build()
            .create(OtpService::class.java)
    }

    @Provides
    fun provideRepo(impl:OtpRepositoryImpl):OtpRepository = impl
}