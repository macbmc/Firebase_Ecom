package com.example.firebaseecom.customerChat

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.firebaseecom.model.BrainShopModel
import com.example.firebaseecom.model.ProductOrderModel
import com.example.firebaseecom.repositories.BrainShopRepository
import com.example.firebaseecom.repositories.FirestoreRepository
import com.example.firebaseecom.utils.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel

class CustomerChatViewModel @Inject constructor(private val brainRepository: BrainShopRepository,
    val firestoreRepository: FirestoreRepository) :
    ViewModel() {

 private val productOrderDetails = MutableLiveData<ProductOrderModel>()

    suspend fun getResponse(msg: String): String {

        val botMsg = viewModelScope.async(Dispatchers.IO) {
            when (val response = brainRepository.getBrainResponse(msg)) {
                is Resource.Success -> {
                    Log.d("BrainShopModel", response.data.cnt.toString())
                    response.data.cnt.toString()
                }

                else -> {
                    Log.d("BrainShopModel", "NoResponse")
                    "No Response"
                }
            }

        }
        return botMsg.await()
    }

    fun getOrderDetails(orderId:String)
    {
        viewModelScope.launch(Dispatchers.IO){
           productOrderDetails.postValue(firestoreRepository.getOrderForChat(orderId))
        }
    }
}