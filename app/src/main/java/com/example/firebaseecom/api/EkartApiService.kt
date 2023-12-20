package com.example.firebaseecom.api

import com.example.firebaseecom.model.OfferModelClass
import com.example.firebaseecom.model.ProductDetailsModel
import com.example.firebaseecom.model.ProductHomeModel
import com.example.firebaseecom.model.ProductOffersModel
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Url

interface EkartApiService {
    @GET
    suspend fun getProducts(@Url url: String): Response<List<ProductHomeModel>?>

    @GET
    suspend fun getProductDetails(@Url url: String): Response<List<ProductDetailsModel>?>

    @GET
    suspend fun getProductOffers(@Url url: String): Response<List<ProductOffersModel>?>

    @GET
    suspend fun getSeasonalOffers(@Url url: String): Response<List<OfferModelClass>?>


}