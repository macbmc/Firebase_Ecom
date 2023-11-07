package com.example.firebaseecom.CartOrder

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.firebaseecom.model.ProductHomeModel
import com.example.firebaseecom.repositories.FirestoreRepository
import com.example.firebaseecom.utils.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject
@HiltViewModel
class ProductListViewModel @Inject constructor(val firestoreRepository: FirestoreRepository)
    :ViewModel()
{
        private val _productList = MutableStateFlow<Resource<List<ProductHomeModel>>>(Resource.Loading())
        val productList :StateFlow<Resource<List<ProductHomeModel>>> = _productList

    fun getProductFromDest(dest:String)
    {
        viewModelScope.launch(Dispatchers.IO) {
            val data=firestoreRepository.getFromDest(dest)
            if(data!=null)
            {
                _productList.value=data
            }
            else{
                _productList.value=Resource.Failed("NO Available Data")
            }

        }
    }
    fun removeFromCart(productModel: ProductHomeModel) {
        viewModelScope.launch(Dispatchers.IO) {
            firestoreRepository.removeFromDest("cart", productModel)
        }
    }
}