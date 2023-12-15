package com.example.firebaseecom.home

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.firebaseecom.model.ProductHomeModel
import com.example.firebaseecom.repositories.AuthRepository
import com.example.firebaseecom.repositories.DatabaseRepository
import com.example.firebaseecom.repositories.FirestoreRepository
import com.example.firebaseecom.repositories.NetworkRepository
import com.example.firebaseecom.repositories.ProductRepository
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
    private val productRepository: ProductRepository,
    private val firestoreRepository: FirestoreRepository,
    private val authRepository: AuthRepository,
    private val networkRepository: NetworkRepository,
    private val databaseRepository: DatabaseRepository
) : ViewModel() {

    private val _products = MutableStateFlow<Resource<List<ProductHomeModel>>>(Resource.Loading())
    val adList = MutableLiveData<List<String>>()
    var products: StateFlow<Resource<List<ProductHomeModel>>> = _products

    companion object {
        val getChange = MutableLiveData<Boolean>()
    }


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
            val localData = databaseRepository.fetchFromLocal()
            _products.value = localData
            val remoteData = networkRepository.fetchFromRemote()
            if (remoteData != null) {
                databaseRepository.storeInLocal(remoteData)
                _products.value = databaseRepository.fetchFromLocal()
                when (localData) {
                    is Resource.Success -> {
                        val localDbData = localData.data
                        getProductChange(localDbData, remoteData)
                    }

                    else -> {}
                }
            }
        }
    }

    private suspend fun getProductChange(
        localData: List<ProductHomeModel>,
        remoteData: List<ProductHomeModel>
    ) {
        val productChange = productRepository.getChangeInProduct(localData, remoteData)
        getChange.postValue(productChange)
    }

    suspend fun checkNumbWishlist(dest: String): Int {
        val size = viewModelScope.async(Dispatchers.IO) {
            firestoreRepository.checkNumDest(dest)
        }
        return size.await()

    }

    suspend fun checkForNewUser(): Boolean {
        val ifNewUser = viewModelScope.async(Dispatchers.IO) {
            authRepository.checkForNewUser()
        }
        return ifNewUser.await()
    }

}

