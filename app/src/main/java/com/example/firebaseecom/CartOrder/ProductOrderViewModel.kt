package com.example.firebaseecom.CartOrder

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.firebaseecom.model.ProductOrderReviews
import com.example.firebaseecom.repositories.FirestoreRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel

class ProductOrderViewModel @Inject constructor(
    val firestoreRepository: FirestoreRepository
) :ViewModel() {

    fun addToReviews(productOrderReviews: ProductOrderReviews)
    {
       viewModelScope.launch(Dispatchers.IO){
           firestoreRepository.addToReviews(productOrderReviews)
       }
    }
}