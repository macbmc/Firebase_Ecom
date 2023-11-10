package com.example.firebaseecom.profile

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.firebaseecom.model.UserModel
import com.example.firebaseecom.repositories.AuthRepository
import com.example.firebaseecom.repositories.FirestoreRepository
import com.example.firebaseecom.utils.AuthState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val firestoreRepository: FirestoreRepository,
    val authRepository:AuthRepository
) : ViewModel() {
    val user=UserModel("","","","","")
    private val _userDetails = MutableStateFlow(user)
    val userDetails: StateFlow<UserModel> = _userDetails
    val authState = MutableStateFlow<AuthState>(AuthState.SignedIn())

    fun getUserData() {
        viewModelScope.launch(Dispatchers.IO) {
            _userDetails.value = firestoreRepository.getFromUsers()!!
            Log.d("userdataview", _userDetails.value.toString())
        }
    }
    fun updateUser(userModel: UserModel)
    {
        viewModelScope.launch(Dispatchers.IO){
            firestoreRepository.addToUsers(userModel)
        }
    }
    fun updateUserEmail(email:String,password:String){
        viewModelScope.launch(Dispatchers.IO){
            authRepository.userEmailUpdate(email,password)
        }
    }



    }



