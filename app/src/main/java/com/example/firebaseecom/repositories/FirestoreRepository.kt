package com.example.firebaseecom.repositories

import android.util.Log
import com.example.firebaseecom.model.ProductModel
import com.example.firebaseecom.model.UserModel
import com.example.firebaseecom.utils.Resource
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

interface FirestoreRepository {
    val currentUser: FirebaseUser?
    suspend fun addToUsers(userModel: UserModel): Int
    suspend fun getFromUsers(): UserModel
    suspend fun getAllProducts(): Resource<List<ProductModel>>
    suspend fun getFromProducts(cat: String): Resource<List<ProductModel>>


    /*suspend fun addToProducts(productModel: ProductModel):Int

    suspend fun getFromProducts(cat:String)

    suspend fun addToWishlist(productModel: ProductModel):Int

    suspend fun addToCart(productModel: ProductModel):Int

    suspend fun addToOrders(productModel: ProductModel):Int
    */
}


class FirestoreRepositoryImpl @Inject constructor(
    private val firebaseAuth: FirebaseAuth,
    private val firestore: FirebaseFirestore
) : FirestoreRepository {
    override val currentUser: FirebaseUser?
        get() = firebaseAuth.currentUser

    override suspend fun addToUsers(userModel: UserModel): Int {
        var status = 400
        val user = hashMapOf(
            "displayName" to userModel.userName,
            "email" to userModel.userEmail,
            "imageUrl" to userModel.userImg,
            "phNumber" to userModel.phNo
        )
        try {
            val doc = firestore.collection("users").document(currentUser!!.uid)
            doc.set(user)
                .addOnSuccessListener {
                    status = 200
                    Log.d("success", "$status")
                }
                .addOnFailureListener {
                    Log.e("toUser", "${it.message}")
                }
        } catch (e: Exception) {
            Log.e("toUser", "$e")

        }
        return status

    }

    override suspend fun getFromUsers(): UserModel {
        val uid = currentUser?.uid
        lateinit var userInfo: UserModel
        try {
            val db = firestore.collection("users").document(uid.toString())
            db.get()
                .addOnSuccessListener {
                    Log.d("data", "${it.data}")
                    userInfo = UserModel(
                        it.data!!["displayName"].toString(),
                        it.data!!["email"].toString(),
                        it.data!!["imageUrl"].toString(),
                        it.data!!["phNumber"].toString()
                    )
                }
                .addOnFailureListener {
                    Log.d("fail", "${it.message}")
                }.await()
        } catch (e: Exception) {
            Log.d("fromUser", "$e")
        }
        return userInfo

    }

    override suspend fun getAllProducts(): Resource<List<ProductModel>> {
        var productList: MutableList<ProductModel> = mutableListOf()
        var response = 400

        try {
            val db = firestore.collection("product-details")
            db.get()
                .addOnSuccessListener {
                    response = 200
                    for (doc in it.documents) {
                        val data = doc.toObject(ProductModel::class.java)
                        if (data != null) {
                            productList.add(data)
                        }
                    }
                    Log.d("data", "$productList")


                }
                .addOnFailureListener {
                    Log.e("fromproduct", "${it.message}")
                }.await()
        } catch (e: Exception) {
            Log.e("errorinproduct", "$e")
        }
        if (response == 200) {
            return Resource.Success(productList)
        }
        return Resource.Failed("Firebase Error Try Again")


    }

    override suspend fun getFromProducts(cat: String): Resource<List<ProductModel>> {
        var productListByCat: MutableList<ProductModel> = mutableListOf()
        var response = 400

        try {
            val db = firestore.collection("product-details").whereEqualTo("productCategory", cat)
            db.get()
                .addOnSuccessListener {
                    response = 200
                    for (doc in it.documents) {
                        val data = doc.toObject(ProductModel::class.java)
                        if (data != null) {
                            productListByCat.add(data)
                        }
                    }
                    Log.d("data", "$productListByCat")


                }
                .addOnFailureListener {
                    Log.e("fromproduct", "${it.message}")
                }.await()
        } catch (e: Exception) {
            Log.e("errorinproduct", "$e")
        }
        if (response == 200) {
            return Resource.Success(productListByCat)
        }
        return Resource.Failed("Firebase Error Try Again")
    }
}
