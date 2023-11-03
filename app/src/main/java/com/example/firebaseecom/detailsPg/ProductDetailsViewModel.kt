package com.example.firebaseecom.detailsPg

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.firebaseecom.model.ProductDetailsModel
import com.example.firebaseecom.model.ProductHomeModel
import com.example.firebaseecom.repositories.FirestoreRepository
import com.example.firebaseecom.repositories.NetworkRepository
import com.example.firebaseecom.repositories.ProductRepository
import com.example.firebaseecom.utils.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProductDetailsViewModel @Inject constructor(
    private val networkRepository: NetworkRepository,
    private val firestoreRepository: FirestoreRepository,
    private val productRepository: ProductRepository
) : ViewModel() {

    private val _productDetails = MutableStateFlow<Resource<List<ProductDetailsModel>?>?>(null)
    val productDetails: StateFlow<Resource<List<ProductDetailsModel>?>?> = _productDetails

    fun getProductDetails(id: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            _productDetails.value = networkRepository.fetchDetailsFromRemote()

        }
    }

    fun addToCart(productModel: ProductHomeModel) {
        viewModelScope.launch(Dispatchers.IO) {
            firestoreRepository.addToDest("cart", productModel)
        }
    }

    fun addToOrders(productModel: ProductHomeModel) {
        viewModelScope.launch(Dispatchers.IO) {
            firestoreRepository.addToDest("orders", productModel)

        }
    }


    fun shareProduct(productModel: ProductHomeModel,context: Context) {
        productRepository.getUri(productModel,context)

    }

}