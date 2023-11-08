package com.example.firebaseecom.profile

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.firebaseecom.model.UserModel
import com.example.firebaseecom.repositories.FirestoreRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(
    val repository: FirestoreRepository
):ViewModel() {
    val _userDetails = MutableStateFlow<UserModel>(UserModel("","","",""))
    val userDetails :StateFlow<UserModel> = _userDetails

    fun getUserData()
    {
        viewModelScope.async(Dispatchers.IO) {
            _userDetails.value=repository.getFromUsers()!!
            Log.d("userdataview",_userDetails.value.toString())
        }
        }
    }


