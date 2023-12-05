package com.example.firebaseecom.home

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.firebaseecom.model.ProductHomeModel
import com.example.firebaseecom.repositories.FirestoreRepository
import com.example.firebaseecom.repositories.NetworkRepository
import com.example.firebaseecom.utils.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val firestoreRepository: FirestoreRepository,
    private val networkRepository: NetworkRepository
) : ViewModel() {

    private val _products = MutableStateFlow<Resource<List<ProductHomeModel>>>(Resource.Loading())
    val adList = MutableLiveData<List<String>>()
    var products: StateFlow<Resource<List<ProductHomeModel>>> = _products


    private suspend fun getAdData() {

        withContext(Dispatchers.IO) {
            val adData = firestoreRepository.getAd()
            adList.postValue(adData)
        }
    }

    fun getAd() {
        viewModelScope.launch {
            getAdData()
        }
    }

    fun getProductHome() {
        _products.value = Resource.Loading()
        viewModelScope.launch(Dispatchers.IO) {
            _products.value = networkRepository.fetchFromLocal()
            val remoteData = networkRepository.fetchFromRemote()
            if (remoteData != null) {
                networkRepository.storeInLocal(remoteData)
                _products.value = networkRepository.fetchFromLocal()
            }
        }
    }

    suspend fun checkNumbWishlist(dest: String): Int {
        val size = viewModelScope.async(Dispatchers.IO) {
            firestoreRepository.checkNumDest(dest)
        }
        return size.await()

    }
}

