package com.example.firebaseecom.homeUI

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.firebaseecom.model.ProductHomeModel
import com.example.firebaseecom.repositories.FirestoreRepository
import com.example.firebaseecom.repositories.NetworkRepository
import com.example.firebaseecom.utils.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val firestoreRepository: FirestoreRepository,
    private val networkRepository: NetworkRepository
) : ViewModel() {

    private val _products = MutableStateFlow<Resource<List<ProductHomeModel>>>(Resource.Loading())
    val adList = MutableStateFlow<Resource<List<String>>>(Resource.Loading())
    var products: StateFlow<Resource<List<ProductHomeModel>>> = _products
    var status:Boolean =false


    fun addToWishlist(productModel: ProductHomeModel) {
        viewModelScope.launch(Dispatchers.IO) {
            firestoreRepository.addToDest("wishlist", productModel)
        }
    }

    fun removeFromWishlist(productModel: ProductHomeModel) {
        viewModelScope.launch(Dispatchers.IO) {
            firestoreRepository.removeFromDest("wishlist", productModel)
        }
    }

    fun getAd() {

        viewModelScope.launch(Dispatchers.IO) {
            val adData = firestoreRepository.getAd()
            if(adData!=null)
            {
                adList.value=Resource.Success(adData)
            }
            else{
                adList.value=Resource.Loading()
            }
        }
    }

    fun getProductHome() {
        viewModelScope.launch(Dispatchers.IO) {
            _products.value = Resource.Loading()
            val remoteData = networkRepository.fetchFromRemote()
            if (remoteData != null) {
                networkRepository.storeInLocal(remoteData)
                _products.value = networkRepository.fetchFromLocal()

            }


        }
    }
    fun checkInWishlist(dest:String,id:Int){
        viewModelScope.launch(Dispatchers.IO){
            status=firestoreRepository.checkInDest(dest,id)
        }

    }
}

