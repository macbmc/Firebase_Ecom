package com.example.firebaseecom.customerChat

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.firebaseecom.api.ProductOrderDeliveryStatus
import com.example.firebaseecom.repositories.BrainShopRepository
import com.example.firebaseecom.repositories.DatabaseRepository
import com.example.firebaseecom.repositories.FirestoreRepository
import com.example.firebaseecom.utils.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import javax.inject.Inject

@HiltViewModel

class CustomerChatViewModel @Inject constructor(
    private val brainRepository: BrainShopRepository,
    val firestoreRepository: FirestoreRepository,
    val databaseRepository: DatabaseRepository
) : ViewModel() {





}