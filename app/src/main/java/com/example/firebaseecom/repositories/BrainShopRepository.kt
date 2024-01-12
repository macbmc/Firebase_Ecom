package com.example.firebaseecom.repositories

import android.util.Log
import com.example.firebaseecom.brainShop.BrainShopApiEndPoints
import com.example.firebaseecom.brainShop.BrainShopService
import com.example.firebaseecom.model.BrainShopModel
import com.example.firebaseecom.utils.Resource
import retrofit2.Response
import javax.inject.Inject

interface BrainShopRepository {

    suspend fun getBrainResponse(msg: String): Resource<BrainShopModel>
}


class BrainShopRepositoryImpl @Inject constructor(private val brainShopService: BrainShopService) :
    BrainShopRepository {

    private lateinit var brainResponse: Response<BrainShopModel>
    override suspend fun getBrainResponse(msg: String): Resource<BrainShopModel> {
        try {
            brainResponse = brainShopService.getResponse(
                BrainShopApiEndPoints.BRAIN_SHOP_BID.url,
                BrainShopApiEndPoints.BRAIN_SHOP_KEY.url,
                BrainShopApiEndPoints.BRAIN_SHOP_UID.url,
                msg
            )

        } catch (e: Exception) {
            Log.d("BrainShop", e.toString())
        }
        if(::brainResponse.isInitialized)
        {
            if(brainResponse.code()!=200)
                return Resource.Failed("BrainResponse not valid")
            return Resource.Success(brainResponse.body()!!)
        }
        else
            return Resource.Failed("BrainResponse not valid")
    }

}