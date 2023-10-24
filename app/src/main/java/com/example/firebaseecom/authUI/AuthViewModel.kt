package com.example.firebaseecom.authUI


import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.firebaseecom.AuthRepository
import com.example.firebaseecom.utils.Resource
import com.google.firebase.auth.FirebaseUser
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class AuthViewModel @Inject constructor(val repository: AuthRepository) : ViewModel() {

    private val _loginAuth = MutableStateFlow<Resource<FirebaseUser>?>(null)
    private val _signUpAuth = MutableStateFlow<Resource<FirebaseUser>?>(null)
    val currentUser :FirebaseUser?
        get() = repository.currentUser

    init {
        if(repository.currentUser!=null)
        {
            _loginAuth.value=Resource.Success(repository.currentUser!!)
        }
    }

    var logiAuth : StateFlow<Resource<FirebaseUser>?> = _loginAuth
    var signUpAuth : StateFlow<Resource<FirebaseUser>?> = _signUpAuth

    fun logIn(email:String,password:String)
    {
        viewModelScope.launch(Dispatchers.IO){
            _loginAuth.value=Resource.Loading()
            val result = repository.userLogin(email,password)
            _loginAuth.value=result


        }
    }

    fun signUp(email: String,password: String)
    {
        viewModelScope.launch(Dispatchers.IO) {
            _signUpAuth.value=Resource.Loading()
            _signUpAuth.value=repository.userSignUp(email,password)
        }
    }

    fun logout()
    {
        repository.userSignOut()
        _loginAuth.value=null
        _signUpAuth.value=null
    }


}