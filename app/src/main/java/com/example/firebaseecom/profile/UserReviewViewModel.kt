package com.example.firebaseecom.profile

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.firebaseecom.model.ProductHomeModel
import com.example.firebaseecom.model.ProductOrderReviews
import com.example.firebaseecom.repositories.DatabaseRepository
import com.example.firebaseecom.repositories.FirestoreRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class UserReviewViewModel @Inject constructor(
    val firestoreRepository: FirestoreRepository,
    private val databaseRepository: DatabaseRepository
) :
    ViewModel() {
    val reviewInfo = MutableLiveData<List<ProductOrderReviews>>()
    val productInfo = MutableLiveData<List<ProductHomeModel>>()


    fun getProductReviewInfo() {
        viewModelScope.launch(Dispatchers.IO) {
            val reviewList = firestoreRepository.getUserReviews()
            reviewInfo.postValue(reviewList)
            productInfo.postValue(databaseRepository.getProducts(reviewList))


        }
    }
}