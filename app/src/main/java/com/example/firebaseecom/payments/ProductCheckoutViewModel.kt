package com.example.firebaseecom.payments

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.firebaseecom.model.ProductHomeModel
import com.example.firebaseecom.repositories.FirestoreRepository
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel

class ProductCheckoutViewModel @Inject constructor(val firestoreRepository: FirestoreRepository):
    ViewModel()
{
    fun addToOrders(productList: List<ProductHomeModel>) {
        viewModelScope.launch(Dispatchers.IO) {
            firestoreRepository.addToOrders(productList)

        }
    }
    fun removeAllFromCart()
    {
        viewModelScope.launch(Dispatchers.IO){
            firestoreRepository.removeAllFromCart()
        }
    }
}