package com.example.firebaseecom.repositories

import android.util.Log
import com.example.firebaseecom.model.ProductModel
import com.example.firebaseecom.model.UserModel
import com.example.firebaseecom.utils.Resource
import com.google.android.gms.tasks.Tasks
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

interface FirestoreRepository {
    val currentUser: FirebaseUser?
    suspend fun addToUsers(userModel: UserModel): Int
    suspend fun getFromUsers(): UserModel?
    suspend fun getAllProducts(): Resource<List<ProductModel>>
    suspend fun getFromProducts(cat: String): Resource<List<ProductModel>>
    suspend fun addToWishlist(productModel: ProductModel): Int
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

    override suspend fun getFromUsers(): UserModel? {
        val uid = currentUser?.uid
        val db = firestore.collection("users").document(uid.toString())
        lateinit var userInfo: UserModel

        try{
            val snapshot = Tasks.await(
                db.get()
                    .addOnCompleteListener {
                        if(it.isSuccessful)
                            it.result
                    }
            )
            val data = snapshot.data
            userInfo= UserModel(data?.get("displayName").toString(),data?.get("email").toString(),
                data?.get("imageUrl").toString(),data?.get("phNumber").toString(),)
        }
        catch(e:Exception)
        {
            Log.d("exceptio",e.toString())
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
                    response = 400
                    Log.e("fromproduct", "${it.message}")
                }.await()
        } catch (e: Exception) {
            Log.e("errorinproduct", "$e")
        }
        if (response == 200) {
            return Resource.Success(productListByCat)
        } else if (response == 400) {
            return Resource.Failed("FirebaseError")
        }
        return Resource.Loading()
    }

    override suspend fun addToWishlist(productModel: ProductModel): Int {
        try {
            val db = firestore.collection("user-wishlist").document(currentUser!!.uid)
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
        return 123
    }

    override suspend fun getAd(): List<String> {
        var adList: MutableList<String> = mutableListOf()
        val db = firestore.collection("ads")
        try {
            val snapshot = Tasks.await(
                db.get()
                    .addOnCompleteListener {
                        if(it.isSuccessful)
                            it.result
                    }
            )
            snapshot.let{
                for(doc in it.documents)
                {

                    val data = doc.data!!.get("imageUrl").toString()
                    adList.add(data)
                }
            }
        }
        catch (e:Exception)
        {
            Log.d("exceptio",e.toString())
        }
        Log.d("adList",adList.toString())
        return adList
    }
}
