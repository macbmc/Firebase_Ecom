package com.example.firebaseecom

import com.example.firebaseecom.model.ProductHomeModel
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.GET

interface EkartApi {
    @GET("/mac-bmc/4ecba6a75937ee54bbc6ca47080ef2b5/raw/7bd12edc7f0c76833980fe3655afb564e3793be9/product-home")
     suspend fun getProducts(): Response<List<ProductHomeModel>?>

    companion object{
        val BASE_URL="https://gist.githubusercontent.com/"
    }


}