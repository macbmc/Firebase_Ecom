package com.example.firebaseecom.homeUI

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.firebaseecom.model.ProductModel
import com.example.firebaseecom.repositories.FirestoreRepository
import com.example.firebaseecom.utils.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(private val repository: FirestoreRepository):ViewModel() {

    private val _products = MutableStateFlow<Resource<List<ProductModel>>?>(null)
    val adList =MutableStateFlow<List<String>>(listOf())

    var products : StateFlow<Resource<List<ProductModel>>?> = _products


    fun getAll()
    {
        viewModelScope.launch(Dispatchers.IO){
            _products.value=Resource.Loading()
            val result = repository.getAllProducts()
            _products.value = result
            Log.d("-product",_products.value.toString())
        }
    }

    fun addToWishlist(productModel: ProductModel)
    {
        viewModelScope.launch(Dispatchers.IO){
            repository.addToDest("wishlist",productModel)
        }
    }
    fun getAd()
    {

        viewModelScope.launch(Dispatchers.IO){
            adList.value=repository.getAd()
        }
    }


}