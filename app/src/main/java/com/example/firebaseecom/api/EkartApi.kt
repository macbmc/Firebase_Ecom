package com.example.firebaseecom.api

import com.example.firebaseecom.model.ProductDetailsModel
import com.example.firebaseecom.model.ProductHomeModel
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.GET

interface EkartApi {
    @GET("/mac-bmc/mac-bmc.github.io/main/product-home.json")
     suspend fun getProducts(): Response<List<ProductHomeModel>?>

     @GET("/mac-bmc/mac-bmc.github.io/main/product-details.json")
     suspend fun getProductDetails() : Response<List<ProductDetailsModel>>

    companion object{
        const val BASE_URL="https://raw.githubusercontent.com/"

    }


}