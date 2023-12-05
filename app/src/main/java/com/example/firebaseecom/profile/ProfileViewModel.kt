package com.example.firebaseecom.profile

import android.net.Uri
import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.firebaseecom.model.UserModel
import com.example.firebaseecom.repositories.AuthRepository
import com.example.firebaseecom.repositories.FirestoreRepository
import com.example.firebaseecom.repositories.StorageRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val firestoreRepository: FirestoreRepository,
    private val authRepository: AuthRepository,
    private val storageRepository: StorageRepository
) : ViewModel() {
    val user = UserModel("", "", "", "", "")
    val userDetails = MutableLiveData<UserModel>()
    val userImage = MutableLiveData<String>()

    fun updateUser(userModel: UserModel) {
        viewModelScope.launch(Dispatchers.IO) {
            firestoreRepository.addToUsers(userModel)
        }
    }

    fun userData() {
        viewModelScope.launch {
            getUserData()
        }
    }

    private suspend fun getUserData() {
        withContext(Dispatchers.IO)
        {
            val user = firestoreRepository.getFromUsers()!!
            userDetails.postValue(user)
        }
    }

    fun updateUserEmail(email: String, password: String) {
        viewModelScope.launch(Dispatchers.IO) {
            authRepository.userEmailUpdate(email, password)
        }
    }


   suspend fun storeImageAndGetUrl(imageUri: Uri){
       viewModelScope.launch(Dispatchers.IO){
           storageRepository.storeImageAndGetUrl(imageUri).collect{downloadUrl->
               Log.d("ImageDownloadUrlModel", downloadUrl)
               userImage.postValue(downloadUrl)
           }
       }
   }


}



