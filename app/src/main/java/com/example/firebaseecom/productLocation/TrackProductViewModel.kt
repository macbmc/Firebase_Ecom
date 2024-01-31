package com.example.firebaseecom.productLocation

import android.content.Context
import androidx.lifecycle.ViewModel
import com.example.firebaseecom.model.ProductOrderModel
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

class TrackProductViewModel @Inject constructor(@ApplicationContext applicationContext:Context) :ViewModel()
{
    fun getCurrentLocation(productOrder: ProductOrderModel) {

    }

}