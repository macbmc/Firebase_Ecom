package com.example.firebaseecom.repositories

import com.example.firebaseecom.model.ProductHomeModel
import retrofit2.Response

interface EkartApiRepository {

    suspend fun fetchFromRemote(): Response<List<ProductHomeModel>?>
    suspend fun storeInLocal()
    suspend fun fetchFromLocal()
}