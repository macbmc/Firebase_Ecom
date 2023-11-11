package com.example.firebaseecom.utils

sealed class AuthState() {

    class SignedIn():AuthState()

    class SignedOut():AuthState()
}