package com.example.firebaseecom.productSearch

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.firebaseecom.model.ProductHomeModel
import com.example.firebaseecom.repositories.DatabaseRepository
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
class ProductSearchVIewModel @Inject constructor(
    private val networkRepository: NetworkRepository,
    private val firestoreRepository: FirestoreRepository,
    private val databaseRepository: DatabaseRepository
) :
    ViewModel() {

    private val _product = MutableStateFlow<Resource<List<ProductHomeModel>>>(Resource.Loading())
    val product: StateFlow<Resource<List<ProductHomeModel>>> = _product

    fun searchProducts(searchQuery: String) {
        viewModelScope.launch(Dispatchers.IO) {
            when (val result = databaseRepository.searchForProducts(searchQuery)) {
                is Resource.Success -> {
                    if (result.data.isNotEmpty())
                        _product.value = result
                    else
                        _product.value = Resource.Success(listOf())
                }

                else -> {
                    _product.value = result
                }
            }
        }
    }

    fun addToCart(productModel: ProductHomeModel) {
        viewModelScope.launch(Dispatchers.IO) {
            firestoreRepository.addToDest("cart", productModel)
        }
    }

}