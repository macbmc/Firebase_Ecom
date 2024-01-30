package com.example.firebaseecom.auth


import android.content.Context
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.firebaseecom.model.UserModel
import com.example.firebaseecom.repositories.AuthRepository
import com.example.firebaseecom.repositories.AuthRepositoryImpl.Companion.userStateFlow
import com.example.firebaseecom.repositories.FirestoreRepository
import com.example.firebaseecom.utils.AlarmTriggerUtils
import com.example.firebaseecom.utils.Resource
import com.example.firebaseecom.utils.UserState
import com.google.firebase.auth.FirebaseUser
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class AuthViewModel @Inject constructor(
    @ApplicationContext val applicationContext: Context,
    private val authRepository: AuthRepository,
    private val firestoreRepository: FirestoreRepository
) : ViewModel() {

    private val _loginAuth = MutableStateFlow<Resource<FirebaseUser>?>(Resource.Loading())
    private val _signUpAuth = MutableStateFlow<Resource<FirebaseUser>?>(Resource.Loading())


    val currentUser: FirebaseUser?
        get() = authRepository.currentUser

    init {
        if (authRepository.currentUser != null) {
            _loginAuth.value = Resource.Success(authRepository.currentUser!!)
        }
    }

    var loginAuth: StateFlow<Resource<FirebaseUser>?> = _loginAuth
    var signUpAuth: StateFlow<Resource<FirebaseUser>?> = _signUpAuth

    fun logIn(email: String, password: String) {
        _loginAuth.value = Resource.Loading()
        viewModelScope.launch(Dispatchers.IO) {
            val result = authRepository.userLogin(email, password)
            _loginAuth.value = result


        }
    }

    fun signUp(email: String, password: String, phNum: String) {
        val userModel = UserModel("", email, "", phNum, "")
        _signUpAuth.value = Resource.Loading()
        viewModelScope.launch(Dispatchers.IO) {
            _signUpAuth.value = authRepository.userSignUp(email, password, phNum)
            Log.d("userData", userModel.toString())
            Log.d("msgRepo", _signUpAuth.value.toString())
            if (_signUpAuth.value is Resource.Success) {
                firestoreRepository.addToUsers(userModel)
            }
        }

    }

    fun forgotPassword(email: String) {
        authRepository.forgotPassword(email)
    }


    fun logout() {
        authRepository.userSignOut()
        _loginAuth.value = null
        _signUpAuth.value = null
    }

    fun setAlarmTrigger()
    {
        AlarmTriggerUtils().setAlarmTriggerForNotification(applicationContext)
    }
    fun setUserState()
    {
        userStateFlow.value = UserState.LoggedIn
    }


}