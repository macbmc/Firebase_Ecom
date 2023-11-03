package com.example.firebaseecom.profile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.firebaseecom.model.UserModel
import com.example.firebaseecom.repositories.FirestoreRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(
    val repository: FirestoreRepository
):ViewModel() {
    val userDetails = MutableStateFlow<UserModel?>(null)

    fun getUserData()
    {
        viewModelScope.launch(Dispatchers.IO){
            userDetails.value=repository.getFromUsers()
        }
    }


}