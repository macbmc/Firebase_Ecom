package com.example.firebaseecom.repositories

import android.util.Log
import com.example.firebaseecom.api.EkartApi
import com.example.firebaseecom.local.ProductDao
import com.example.firebaseecom.model.ProductDetailsModel
import com.example.firebaseecom.model.ProductHomeModel
import com.example.firebaseecom.utils.Resource
import retrofit2.Response
import javax.inject.Inject

interface NetworkRepository {

    suspend fun fetchFromRemote(): List<ProductHomeModel>?
    suspend fun storeInLocal(remoteData: List<ProductHomeModel>?)
    suspend fun fetchFromLocal(): Resource<List<ProductHomeModel>>

    suspend fun fetchDetailsFromRemote(): Resource<List<ProductDetailsModel>?>?
}

class NetworkRepositoryImpl @Inject constructor(
    private val ekartApi: EkartApi,
    private val productDao: ProductDao
) :
    NetworkRepository {

    override suspend fun fetchFromRemote(): List<ProductHomeModel>? {
        lateinit var apiCall: Response<List<ProductHomeModel>?>
        try {
            apiCall = ekartApi.getProducts()
        } catch (e: Exception) {
            Log.d("apiCall", e.toString())
        }
        if (apiCall.code() != 200) {
            return null
        }
        return apiCall.body()


    }

    override suspend fun fetchFromLocal(): Resource<List<ProductHomeModel>> {
        var productData = listOf<ProductHomeModel>()
        try {
            productData = productDao.getProductFromDb()
        } catch (e: Exception) {
            Log.d("fetchFromLocal", e.toString())
        }
        if (productData == null) {
            return Resource.Failed("data from local is null")
        }
        return Resource.Success(productData)
    }

    override suspend fun storeInLocal(remoteData: List<ProductHomeModel>?) {
        try {
            productDao.insertProduct(remoteData!!)
        } catch (e: Exception) {
            Log.e("storeinLocal", e.toString())

        }

    }

    override suspend fun fetchDetailsFromRemote(): Resource<List<ProductDetailsModel>?>? {
        lateinit var apiCall: Response<List<ProductDetailsModel>>
        try {
            apiCall = ekartApi.getProductDetails()
        } catch (e: Exception) {
            Log.d("apiCall", e.toString())
        }
        if (apiCall.code() != 200) {
            return Resource.Failed("API Error")
        }

        return Resource.Success(apiCall.body()!!)

    }
}
