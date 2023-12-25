package com.example.firebaseecom.offers

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.firebaseecom.model.ProductHomeModel
import com.example.firebaseecom.repositories.DatabaseRepository
import com.example.firebaseecom.repositories.NetworkRepository
import com.example.firebaseecom.repositories.ProductRepository
import com.example.firebaseecom.utils.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class OfferZoneViewModel @Inject constructor(
    private val networkRepository: NetworkRepository,
    private val databaseRepository: DatabaseRepository,
    private val productRepository: ProductRepository
) : ViewModel() {

     val offerProductByCategory =
        MutableStateFlow<Resource<List<ProductHomeModel>>>(Resource.Loading())


    suspend fun getOffersByCategory(category: String) {
        viewModelScope.launch(Dispatchers.IO) {
            val offerData = networkRepository.fetchProductOffers()
            if (offerData != null) {
                val offerProducts = databaseRepository.mapOfferWithProducts(offerData)
                val discountedProducts = productRepository.getNewDiscount(offerProducts!!,offerData)
                offerProductByCategory.value =
                    productRepository.getOffersByCategory(discountedProducts, category)
                Log.d("offerZoneModel",offerProductByCategory.value.toString())
            }
        }
    }

}