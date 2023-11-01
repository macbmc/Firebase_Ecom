package com.example.firebaseecom.detailsUI

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.firebaseecom.model.ProductDetailsModel
import com.example.firebaseecom.repositories.NetworkRepository
import com.example.firebaseecom.utils.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProductDetailsViewModel @Inject constructor(
    val networkRepository: NetworkRepository
) : ViewModel() {

    val _productDetails = MutableStateFlow<Resource<List<ProductDetailsModel>?>?>(null)
    val productDetails: StateFlow<Resource<List<ProductDetailsModel>?>?> = _productDetails

    fun getProductDetails(id:Int) {
        viewModelScope.launch(Dispatchers.IO) {
            _productDetails.value = networkRepository.fetchDetailsFromRemote()

        }
    }

}