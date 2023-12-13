package com.example.firebaseecom.category

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
class ProductCategoryViewModel @Inject constructor(
    private val networkRepository: NetworkRepository,
    val firestoreRepository: FirestoreRepository,
    private val databaseRepository: DatabaseRepository
) : ViewModel() {

    private val _products = MutableStateFlow<Resource<List<ProductHomeModel>>>(Resource.Loading())
    val products: StateFlow<Resource<List<ProductHomeModel>>> = _products
    fun getProductsByCat(category: String) {
        viewModelScope.launch(Dispatchers.IO) {
            _products.value = databaseRepository.fetchProductByCategory(category)

        }
    }

    fun addToCart(productModel: ProductHomeModel) {
        viewModelScope.launch(Dispatchers.IO) {
            firestoreRepository.addToDest("cart", productModel)
        }
    }
}