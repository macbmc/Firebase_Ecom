package com.example.firebaseecom.repositories

import android.content.Context
import android.util.Log
import android.widget.Toast
import com.example.firebaseecom.R
import com.example.firebaseecom.utils.AuthState
import com.example.firebaseecom.utils.Resource
import com.google.firebase.auth.EmailAuthProvider
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthEmailException
import com.google.firebase.auth.FirebaseAuthException
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.FirebaseAuthUserCollisionException
import com.google.firebase.auth.FirebaseUser
import kotlinx.coroutines.tasks.await
import javax.inject.Inject


interface AuthRepository {
    val currentUser: FirebaseUser?
    suspend fun userLogin(email: String, password: String): Resource<FirebaseUser>
    suspend fun userSignUp(email: String, password: String, phNum: String): Resource<FirebaseUser>
    suspend fun userEmailUpdate(email: String, password: String): AuthState
    suspend fun deleteUserAccount(password: String)
    fun userSignOut()

    fun forgotPassword(email: String)

}

class AuthRepositoryImpl @Inject constructor(
    private val firebaseAuth: FirebaseAuth,
    private val context: Context
) : AuthRepository {

    interface AuthStateChange {
        fun navToSignUp()
    }

    override val currentUser: FirebaseUser?
        get() = firebaseAuth.currentUser

    override suspend fun userLogin(email: String, password: String): Resource<FirebaseUser> {

        return try {
            val user = firebaseAuth.signInWithEmailAndPassword(email, password).await()
            Resource.Success(user.user!!)
        } catch (e: Exception) {
            Log.e("SignIn", "$e")
            Resource.Failed(context.getString(R.string.invalid_credentials_try_again))
        }
    }

    override suspend fun userSignUp(
        email: String,
        password: String,
        phNum: String
    ): Resource<FirebaseUser> {
        val msg = isValidated(password, phNum)
        var errorMsg = ""
        Log.d("msg", msg)
        return if (msg == "") {
            try {
                val user = firebaseAuth.createUserWithEmailAndPassword(email, password).await()
                Resource.Success(user.user!!)
            } catch (e: FirebaseAuthException) {
                Log.e("signUp", "$e")
                when(e)
                {
                    is FirebaseAuthUserCollisionException ->{
                        errorMsg="Credential already in use"
                    }

                    is FirebaseAuthInvalidCredentialsException ->{
                        errorMsg = "Credentials not valid"
                    }

                    else->{
                        errorMsg = e.message.toString()
                    }
                }
                Resource.Failed(errorMsg)
            }

        } else {
            Resource.Failed(msg)
        }
    }

    override suspend fun userEmailUpdate(email: String, password: String): AuthState {
        val auth = EmailAuthProvider.getCredential(currentUser?.email!!, password)
        var response = 400
        currentUser?.reauthenticate(auth)

            ?.addOnCompleteListener { reAuth ->
                if (reAuth.isSuccessful) {
                    Log.d("reAuth", "success")
                    currentUser?.verifyBeforeUpdateEmail(email)
                        ?.addOnCompleteListener { emailVerification ->
                            if (emailVerification.isSuccessful) {
                                response = 200

                            }
                        }?.addOnFailureListener {
                            Log.d("emailVerification", it.toString())
                        }
                }

            }?.addOnFailureListener {
                Log.d("reAuth", it.toString())
            }
        if (response != 200)
            return AuthState.SignedIn()

        return AuthState.SignedOut()
    }

    override suspend fun deleteUserAccount(password: String) {
        val authCredential = EmailAuthProvider.getCredential(currentUser?.email!!, password)
        try {
            currentUser?.reauthenticate(authCredential)
                ?.addOnCompleteListener { reAuth ->
                    if (reAuth.isSuccessful) {
                        Log.d("reAuth", "success")
                        currentUser?.delete()?.addOnCompleteListener { delete ->
                            if (delete.isSuccessful) {
                                Log.d("delete", "success")
                                //currentUser=FirebaseAuth.getInstance().currentUser
                            }
                        }
                            ?.addOnFailureListener {
                                Log.d("delete", it.toString())
                            }
                    }
                }
                ?.addOnFailureListener {
                    Log.d("reAuth", it.toString())
                }
        } catch (e: Exception) {
            Log.d("deleteUserAccount", e.toString())
        }
    }


    override fun userSignOut() {
        try {
            firebaseAuth.signOut()
        } catch (e: Exception) {
            Log.e("signOut", "$e")
        }
    }

    override fun forgotPassword(email: String) {
        try {
            firebaseAuth.sendPasswordResetEmail(email).addOnSuccessListener {
                Toast.makeText(context, context.getString(R.string.check_your_mail), Toast.LENGTH_LONG)
                    .show()
            }
                .addOnFailureListener {
                    Toast.makeText(context, context.getString(R.string.invalid_credentials_try_again), Toast.LENGTH_LONG)
                        .show()
                }

        } catch (e: Exception) {
            Log.d("forgotPassword", e.toString())
        }
    }

    private fun isValidated(password: String, phNum: String): String {
        var msg = ""
        var nD = 0
        var nL = 0

        if (password.length < 6) {
            msg = context.getString(R.string.lengthMsg)
            return msg

        }
        if (phNum.length != 10) {
            msg = context.getString(R.string.invalid_phone_number)
            return msg
        }
        for (i in password.indices) {
            if (password[i].isDigit()) {
                nD++
            }
            if (password[i].isLetter()) {
                nL++
            }

        }
        if (nD < 2) {
            msg = context.getString(R.string.DigitMsg)
        }
        if (nL < 2) {
            msg = context.getString(R.string.letterMsg)
        }
        return msg


    }
}