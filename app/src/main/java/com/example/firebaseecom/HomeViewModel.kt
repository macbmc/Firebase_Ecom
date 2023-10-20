package com.example.firebaseecom

import androidx.lifecycle.ViewModel
import com.example.firebaseecom.model.ProductModel
import com.example.firebaseecom.model.State
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.MutableStateFlow
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(@ApplicationContext val context: ApplicationContext):ViewModel() {

    private var _products : MutableStateFlow<State<List<ProductModel>>> = MutableStateFlow(State.loading())

     var products : MutableStateFlow<State<List<ProductModel>>> = _products


}