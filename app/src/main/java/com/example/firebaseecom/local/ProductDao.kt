package com.example.firebaseecom.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.firebaseecom.model.ProductHomeModel

@Dao

interface ProductDao {


    @Insert(onConflict=OnConflictStrategy.REPLACE )
    suspend fun insertProduct(productList:List<ProductHomeModel>)

    @Query("SELECT * FROM ProductTable")
    fun getProductFromDb():List<ProductHomeModel>

    @Query("SELECT * FROM ProductTable WHERE productCategory=:category")
    fun getProductByCategory(category:String):List<ProductHomeModel>

    @Query("SELECT * FROM ProductTable WHERE en LIKE '%' || :searchQuery || '%' OR productCategory LIKE '%' || :searchQuery ||  '%' OR ml LIKE '%' || :searchQuery || '%'")
    fun searchForProducts(searchQuery:String):List<ProductHomeModel>

    @Query("SELECT * FROM ProductTable WHERE productId =:productId")
    fun getOfferProduct(productId:Int):ProductHomeModel

    @Query("DELETE FROM ProductTable")
    fun deleteAll()

    @Query("SELECT productId FROM ProductTable WHERE en LIKE '%' || :productName || '%' OR ml LIKE '%' || :productName || '%'")
    fun getProductId(productName:String):Int?

}