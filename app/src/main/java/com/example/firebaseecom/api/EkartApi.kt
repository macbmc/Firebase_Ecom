package com.example.firebaseecom.api

import com.example.firebaseecom.model.ProductHomeModel
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.GET

interface EkartApi {
    @GET("/mac-bmc/mac-bmc.github.io/main/product-home.json")
     suspend fun getProducts(): Response<List<ProductHomeModel>?>

    companion object{
        const val BASE_URL="https://raw.githubusercontent.com/"
    }


}