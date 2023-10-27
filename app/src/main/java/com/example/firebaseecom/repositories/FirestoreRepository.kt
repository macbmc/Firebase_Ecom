package com.example.firebaseecom.repositories

import android.util.Log
import com.example.firebaseecom.model.ProductModel
import com.example.firebaseecom.model.UserModel
import com.example.firebaseecom.utils.Resource
import com.google.android.gms.tasks.Tasks
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.FirebaseFirestore
import javax.inject.Inject

interface FirestoreRepository {
    val currentUser: FirebaseUser?
    suspend fun addToUsers(userModel: UserModel): Int
    suspend fun getFromUsers(): UserModel?
    suspend fun getAllProducts(): Resource<List<ProductModel>>
    suspend fun getFromProducts(cat: String): Resource<List<ProductModel>>
    suspend fun addToDest(dest: String,productModel: ProductModel)
    suspend fun getAd(): List<String>


    /*suspend fun addToProducts(productModel: ProductModel):Int


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
        val db = firestore.collection("users").document(uid.toString())
        lateinit var userInfo: UserModel

        try {
            val snapshot = Tasks.await(
                db.get()
                    .addOnCompleteListener {
                        if (it.isSuccessful)
                            it.result
                    }
            )
            val data = snapshot.data
            userInfo = UserModel(
                data?.get("displayName").toString(), data?.get("email").toString(),
                data?.get("imageUrl").toString(), data?.get("phNumber").toString(),
            )
        } catch (e: Exception) {
            Log.d("exceptio", e.toString())
        }
        return userInfo

    }

    override suspend fun getAllProducts(): Resource<List<ProductModel>> {
        var productList: MutableList<ProductModel> = mutableListOf()
        var response = 0
        val db = firestore.collection("product-details")
        try {
            val snapshot = Tasks.await(db.get()
                .addOnCompleteListener {
                    if (it.isSuccessful) {
                        it.result
                    }
                })
            snapshot.let {
                for (doc in it.documents) {
                    val data = doc.toObject(ProductModel::class.java)
                    if (data != null) {
                        response = 200
                        productList.add(data)
                    }
                }
            }
        } catch (e: Exception) {
            Log.d("exception", e.toString())
        }
        if (response != 200) {
            return Resource.Failed("")
        }
        return Resource.Success(productList)


    }

    override suspend fun getFromProducts(cat: String): Resource<List<ProductModel>> {
        var productListByCat: MutableList<ProductModel> = mutableListOf()
        var response = 400
        val db = firestore.collection("product-details").whereEqualTo("productCategory", cat)
        try {
            val snapshot = Tasks.await(db.get()
                .addOnCompleteListener {
                    if (it.isSuccessful) {
                        it.result
                    }
                })
            snapshot.let {
                for (doc in it.documents) {
                    val data = doc.toObject(ProductModel::class.java)
                    if (data != null) {
                        response = 200
                        productListByCat.add(data)
                    }
                }
            }
        } catch (e: Exception) {
            Log.d("exception", e.toString())
        }
        if (response != 200) {
            return Resource.Failed("")
        }
        return Resource.Success(productListByCat)


    }

    override suspend fun addToDest(dest:String,productModel: ProductModel) {
        try {
            val db = firestore.collection("user-$dest").document(currentUser!!.uid)
                .collection("items")
            db.add(productModel)
                .addOnSuccessListener {
                    Log.d("add", "success")
                }
                .addOnFailureListener {
                    Log.d("add", "failure")
                }
        } catch (e: Exception) {
            Log.d("exception", "$e")
        }
    }

    override suspend fun getAd(): List<String> {
        var adList: MutableList<String> = mutableListOf()
        val db = firestore.collection("ads")
        try {
            val snapshot = Tasks.await(
                db.get()
                    .addOnCompleteListener {
                        if (it.isSuccessful)
                            it.result
                    }
            )
            snapshot.let {
                for (doc in it.documents) {

                    val data = doc.data!!.get("imageUrl").toString()
                    adList.add(data)
                }
            }
        } catch (e: Exception) {
            Log.d("exceptio", e.toString())
        }
        Log.d("adList", adList.toString())
        return adList
    }
}
