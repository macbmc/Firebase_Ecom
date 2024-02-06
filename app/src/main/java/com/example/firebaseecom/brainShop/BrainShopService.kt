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


//eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJhdWQiOiJhdXRoLWJhY2tlbmQ6YXBwIiwic3ViIjoiMzQ4YzVmZjAtNjc3OS00YWJhLTg3N2QtNWM2Y2M1NjAxNmE2In0.A4icTPL3wHSWjVOXZUMngVQOlib_3gacfl2ov5rSLxU