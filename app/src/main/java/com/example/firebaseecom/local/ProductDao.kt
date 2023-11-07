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

    @Query("SELECT * FROM ProductTable WHERE productTitle LIKE '%' || :productName || '%'")
    fun searchForProducts(productName:String):List<ProductHomeModel>

}