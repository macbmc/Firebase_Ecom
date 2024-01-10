package com.example.firebaseecom.detailsPg

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.firebaseecom.model.ProductOrderReviews
import com.example.firebaseecom.model.UserModel
import com.example.firebaseecom.repositories.FirestoreRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProductReviewViewModel @Inject constructor(val firestoreRepository: FirestoreRepository) :
    ViewModel() {

    val userDetails = MutableLiveData<List<UserModel>>()


    fun getReviewUser(reviewData: List<ProductOrderReviews>) {
        viewModelScope.launch(Dispatchers.IO) {
            val reviewUserList = firestoreRepository.getReviewUsers(reviewData)
            Log.d("reviewUserView", reviewUserList.toString())
            userDetails.postValue(reviewUserList)
        }
    }
}