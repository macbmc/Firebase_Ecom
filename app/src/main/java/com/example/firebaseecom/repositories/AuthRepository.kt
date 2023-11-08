package com.example.firebaseecom.repositories

import android.content.res.Resources
import android.util.Log
import com.example.firebaseecom.utils.Resource
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.example.firebaseecom.R
import kotlinx.coroutines.tasks.await
import javax.inject.Inject


interface AuthRepository
{
 val currentUser : FirebaseUser?
 suspend fun userLogin(email:String,password:String):Resource<FirebaseUser>
 suspend fun userSignUp(email: String,password: String):Resource<FirebaseUser>

 fun userSignOut()

}

class AuthRepositoryImpl @Inject constructor(
 private val firebaseAuth: FirebaseAuth
): AuthRepository {
 override val currentUser: FirebaseUser?
  get() = firebaseAuth.currentUser

 override suspend fun userLogin(email: String, password: String): Resource<FirebaseUser> {

  return try {
   val user = firebaseAuth.signInWithEmailAndPassword(email, password).await()
   Resource.Success(user.user!!)
  } catch (e: Exception) {
   Log.e("SignIn", "$e")
   Resource.Failed(e.toString())
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

 override fun userSignOut() {
  try {
   firebaseAuth.signOut()
  } catch (e: Exception) {
   Log.e("signOut", "$e")
  }
 }

 fun isValidated(password: String): String {
   var msg = " "
  var nD=0
  var nL=0

  if (password.length < 6) {
   msg = Resources.getSystem().getString(R.string.lengthMsg)

  }
  for(i in 0 until password.length)
  {
   if(password[i].isDigit())
   {
    nD++
   }
   if(password[i].isLetter())
   {
    nL++
   }

  }
  if(nD<2)
  {
   msg=Resources.getSystem().getString(R.string.DigitMsg)
  }
  if(nL<2)
  {
   msg=Resources.getSystem().getString(R.string.letterMsg)
  }
  return msg

 }
}