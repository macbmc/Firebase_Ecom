package com.example.firebaseecom.repositories

import android.util.Log
import com.example.firebaseecom.model.ProductDetailsModel
import com.example.firebaseecom.model.ProductHomeModel
import com.example.firebaseecom.model.UserModel
import com.example.firebaseecom.utils.Resource
import com.google.android.gms.tasks.Tasks
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject

interface FirestoreRepository {
    val currentUser: FirebaseUser?
    suspend fun addToUsers(userModel: UserModel): Int
    suspend fun getFromUsers(): UserModel?
    suspend fun addToDest(dest: String,productModel: ProductHomeModel)
    suspend fun checkInDest(dest: String,id: Int): Boolean
    suspend fun removeFromDest(dest: String,productModel: ProductHomeModel)
    suspend fun getAd(): List<String>
    suspend fun getFromDest(dest: String):Resource<List<ProductHomeModel>>



}


class FirestoreRepositoryImpl @Inject constructor(
    private val firebaseAuth: FirebaseAuth,
    private val firestore: FirebaseFirestore
) : FirestoreRepository {
    override val currentUser: FirebaseUser?
        get() = firebaseAuth.currentUser

    override suspend fun addToUsers(userModel: UserModel): Int {
        var status = 400
        try {
            val doc = firestore.collection("users").document(currentUser!!.uid)
            doc.set(userModel)
                .addOnSuccessListener {
                    status = 200
                    Log.d("success", "$status")
                }
                .addOnFailureListener {
                    Log.e("toUse", "${it.message}")
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
                data?.get("userName").toString(), data?.get("userEmail").toString(),
                data?.get("userImg").toString(), data?.get("phNo").toString(),
            )
        } catch (e: Exception) {
            Log.d("exceptio", e.toString())
        }

        return userInfo

    }

    /*override suspend fun getAllProducts(): Resource<List<ProductModel>> {
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


    }*/

    /*override suspend fun getFromProducts(cat: String): Resource<List<ProductModel>> {
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


    }*/

    override suspend fun addToDest(dest:String,productModel: ProductHomeModel) {
        try {
            val db = firestore.collection("user-$dest").document(currentUser!!.uid)
                .collection("items").document(productModel.productId.toString())
            db.set(productModel)
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
            Log.d("exception", e.toString())
        }
        Log.d("adList", adList.toString())
        return adList
    }

    override suspend fun removeFromDest(dest: String, productModel: ProductHomeModel) {
        val db = firestore.collection("user-$dest").document(currentUser!!.uid)
            .collection("items").document(productModel.productId.toString())
        db.delete()
    }

    override suspend fun checkInDest(dest: String, id: Int): Boolean {
        var status = false
        val db=firestore.collection("user-$dest").document(currentUser!!.uid)
            .collection("items").document(id.toString())
        val task = Tasks.await(db.get()
            .addOnCompleteListener {
                if(it.isSuccessful)
                {
                    val doc = it.result
                    if(doc.exists())
                    {
                        status=true
                    }
                }
                Log.d("statusRepo",status.toString())

            })

        return status

    }

    override suspend fun getFromDest(dest: String): Resource<List<ProductHomeModel>> {
        var productList: MutableList<ProductHomeModel> = mutableListOf()
        var response = 0
        val db = firestore.collection("user-$dest").document(currentUser!!.uid)
            .collection("items")
        try {
            val snapshot = Tasks.await(db.get()
                .addOnCompleteListener {
                    if (it.isSuccessful) {
                        it.result
                    }
                })
            snapshot.let {
                for (doc in it.documents) {
                    val data = doc.toObject(ProductHomeModel::class.java)
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
            return Resource.Failed("No Available Data")
        }
        Log.d("cartdatarepo",productList.toString())
        return Resource.Success(productList)
    }
}
