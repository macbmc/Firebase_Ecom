package com.example.firebaseecom.repositories

interface PaymentRepository {
    suspend fun launchRazorpay()
}

class PaymentRepositoryImpl:PaymentRepository {
    override suspend fun launchRazorpay() {
        TODO("Not yet implemented")
    }
}