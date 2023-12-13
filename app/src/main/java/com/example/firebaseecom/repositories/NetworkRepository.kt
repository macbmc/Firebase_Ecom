package com.example.firebaseecom.repositories

import android.util.Log
import com.example.firebaseecom.api.EkartApiEndPoints
import com.example.firebaseecom.api.EkartApiService
import com.example.firebaseecom.model.ProductDetailsModel
import com.example.firebaseecom.model.ProductHomeModel
import com.example.firebaseecom.model.ProductOffersModel
import com.example.firebaseecom.utils.Resource
import retrofit2.Response
import javax.inject.Inject

interface NetworkRepository {

    suspend fun fetchFromRemote(): List<ProductHomeModel>?

    suspend fun fetchDetailsFromRemote(): Resource<List<ProductDetailsModel>?>?

    suspend fun fetchProductOffers(): List<ProductOffersModel>?
}

class NetworkRepositoryImpl @Inject constructor(
    private val ekartApiService: EkartApiService
) :
    NetworkRepository {
    private lateinit var apiCall: Response<List<ProductHomeModel>?>
    private lateinit var apiCallDetails: Response<List<ProductDetailsModel>?>
    private lateinit var apiCallOffers: Response<List<ProductOffersModel>?>

    override suspend fun fetchFromRemote(): List<ProductHomeModel>? {
        try {
            apiCall = ekartApiService.getProducts(EkartApiEndPoints.END_POINT_PRODUCTS.url)
            Log.d("apiCall", "success")
        } catch (e: Exception) {
            Log.d("apiCall", e.toString())
        }
        if (::apiCall.isInitialized) {

            if (apiCall.code() != 200) {
                return null
            }
            return apiCall.body()
        }
        return null


    }


    override suspend fun fetchDetailsFromRemote(): Resource<List<ProductDetailsModel>?> {
        try {
            apiCallDetails =
                ekartApiService.getProductDetails(EkartApiEndPoints.END_POINT_DETAIL_MULTILINGUAL.url)
        } catch (e: Exception) {
            Log.d("apiCall", e.toString())
        }
        if (::apiCallDetails.isInitialized) {

            if (apiCallDetails.code() != 200) {
                return Resource.Failed("apiFail")
            }
            return Resource.Success(apiCallDetails.body())
        }
        return Resource.Failed("apiFail")

    }


    override suspend fun fetchProductOffers(): List<ProductOffersModel>? {
        try {
            apiCallOffers = ekartApiService.getProductOffers(EkartApiEndPoints.END_POINT_OFFERS.url)
        } catch (e: Exception) {
            Log.d("apiCallOffers", e.toString())
        }
        if (::apiCallOffers.isInitialized) {

            if (apiCallOffers.code() != 200) {
                return null
            }
            return apiCallOffers.body()
        }
        return null
    }


}
