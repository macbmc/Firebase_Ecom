package com.example.firebaseecom.api

import com.example.firebaseecom.model.ProductDetailsModel
import com.example.firebaseecom.model.ProductHomeModel
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Url

interface EkartApi {
    @GET
    suspend fun getProducts(@Url url: String): Response<List<ProductHomeModel>?>

    @GET
    suspend fun getProductDetails(@Url url: String): Response<List<ProductDetailsModel>?>


}