package com.example.firebaseecom.repositories

import android.content.res.Resources
import android.util.Log
import com.example.firebaseecom.R
import com.example.firebaseecom.profile.UserProfileActivity
import com.example.firebaseecom.utils.AuthState
import com.example.firebaseecom.utils.Resource
import com.google.firebase.auth.EmailAuthProvider
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import kotlinx.coroutines.tasks.await
import javax.inject.Inject


interface AuthRepository {
    var currentUser: FirebaseUser?
    suspend fun userLogin(email: String, password: String): Resource<FirebaseUser>
    suspend fun userSignUp(email: String, password: String): Resource<FirebaseUser>
    suspend fun userEmailUpdate(email: String,password: String):AuthState
    suspend fun deleteUserAccount(password: String)

    fun userSignOut()

}

class AuthRepositoryImpl @Inject constructor(
    private val firebaseAuth: FirebaseAuth,
    private val authStateChangeImpl: AuthStateChange
) : AuthRepository {

    interface AuthStateChange{
        fun navToSignUp()
    }
    override var currentUser: FirebaseUser?
        get() = firebaseAuth.currentUser
        set(value) {}

    override suspend fun userLogin(email: String, password: String): Resource<FirebaseUser> {

        return try {
            val user = firebaseAuth.signInWithEmailAndPassword(email, password).await()
            Resource.Success(user.user!!)
        } catch (e: Exception) {
            Log.e("SignIn", "$e")
            Resource.Failed("Invalid Credentials")
        }
    }

    override suspend fun userSignUp(email: String, password: String): Resource<FirebaseUser> {
        val msg = isValidated(password)
        if (msg != " ") {
            return Resource.Failed(message = msg)
        } else {

            return try {
                val user = firebaseAuth.createUserWithEmailAndPassword(email, password).await()
                Resource.Success(user.user!!)
            } catch (e: Exception) {
                Log.e("signUp", "$e")
                Resource.Failed(e.toString())
            }
        }
    }

    override suspend fun userEmailUpdate(email: String, password: String):AuthState {
        var auth = EmailAuthProvider.getCredential(currentUser?.email!!,password)
        var response=400
        currentUser?.reauthenticate(auth)

            ?.addOnCompleteListener{reAuth->
                if(reAuth.isSuccessful)
                {
                    Log.d("reAuth","success")
                    currentUser?.verifyBeforeUpdateEmail(email)
                        ?.addOnCompleteListener { emailVerification->
                            if(emailVerification.isSuccessful)
                            {
                                response=200

                            }
                        }?.addOnFailureListener{
                            Log.d("emailVerification",it.toString())
                        }
                }

            }?.addOnFailureListener{
                Log.d("reAuth",it.toString())
            }
        if(response!=200)
            return AuthState.SignedIn()

        return AuthState.SignedOut()
    }

    override suspend fun deleteUserAccount(password: String) {
        val authCredential= EmailAuthProvider.getCredential(currentUser?.email!!,password)
        try {
            currentUser?.reauthenticate(authCredential)
                ?.addOnCompleteListener { reAuth->
                    if(reAuth.isSuccessful)
                    {
                        Log.d("reAuth","success")
                        currentUser?.delete()?.addOnCompleteListener {delete->
                            if(delete.isSuccessful)
                            {
                                Log.d("delete","success")
                                //currentUser=FirebaseAuth.getInstance().currentUser
                            }
                        }
                            ?.addOnFailureListener {
                                Log.d("delete",it.toString())
                            }
                    }
                }
                ?.addOnFailureListener {
                    Log.d("reAuth",it.toString())
                }
        }
        catch (e:Exception)
        {
            Log.d("deleteUserAccount",e.toString())
        }
    }


    override fun userSignOut() {
        try {
            firebaseAuth.signOut()
        } catch (e: Exception) {
            Log.e("signOut", "$e")
        }
    }

    fun isValidated(password: String): String {
        var msg = " "
        var nD = 0
        var nL = 0

        if (password.length < 6) {
            msg = Resources.getSystem().getString(R.string.lengthMsg)

        }
        for (i in 0 until password.length) {
            if (password[i].isDigit()) {
                nD++
            }
            if (password[i].isLetter()) {
                nL++
            }

        }
        if (nD < 2) {
            msg = Resources.getSystem().getString(R.string.DigitMsg)
        }
        if (nL < 2) {
            msg = Resources.getSystem().getString(R.string.letterMsg)
        }
        return msg

    }
}