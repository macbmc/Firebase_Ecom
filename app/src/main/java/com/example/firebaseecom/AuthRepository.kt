package com.example.firebaseecom

import android.util.Log
import com.example.firebaseecom.utils.Resource
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import dagger.hilt.android.qualifiers.ApplicationContext
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
  if (msg != "") {
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
   var msg: String = ""
  var n=0

  if (password.length < 6) {
   msg="Not enough characters"

  }
  for(i in 0 until password.length)
  {
   if(password[i].isDigit())
   {
    n++
   }

  }
  if(n<2)
  {
   msg="Not enough digits in the password"
  }
  return msg

 }
}