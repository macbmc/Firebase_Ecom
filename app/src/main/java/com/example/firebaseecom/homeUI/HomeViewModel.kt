package com.example.firebaseecom.homeUI

import android.content.Context
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.firebaseecom.model.ProductModel
import com.example.firebaseecom.repositories.FirestoreRepository
import com.example.firebaseecom.utils.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(@ApplicationContext val context: Context,val repository: FirestoreRepository):ViewModel() {

    private val _products = MutableStateFlow<Resource<List<ProductModel>>?>(null)

    var products : StateFlow<Resource<List<ProductModel>>?> = _products


    fun getAll()
    {
        viewModelScope.launch(Dispatchers.IO){
            _products.value=Resource.Loading()
            val result = repository.getAllProducts()
            _products.value = result
            Log.d("-product","hasValue")
        }
    }

    fun addToWishlist(productModel: ProductModel)
    {
        viewModelScope.launch(Dispatchers.IO){
            repository.addToWishlist(productModel)
        }
    }


}