package com.example.firebaseecom.brainShop

import com.example.firebaseecom.model.BrainShopModel
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface BrainShopService {

    @GET("get")
    suspend fun getResponse(
        @Query("bid") brainId: String,
        @Query("key") keyId: String,
        @Query("uid") uid: String,
        @Query("msg") msg: String
    ): Response<BrainShopModel>

}