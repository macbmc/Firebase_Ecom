package com.example.firebaseecom.utils


sealed class UserState{
    object LoggedIn : UserState()
    class Deleted(val message: String) : UserState()
    class DeleteFailure(val message: String) : UserState()
    object LoggedOut : UserState()
    object SignedUp : UserState()
}