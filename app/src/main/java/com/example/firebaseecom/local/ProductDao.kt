package com.example.firebaseecom.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.firebaseecom.model.ProductHomeModel
import javax.sql.DataSource

@Dao

interface ProductDao {


    @Insert(onConflict=OnConflictStrategy.REPLACE )
    suspend fun insertProduct(productList:List<ProductHomeModel>)

    @Query("SELECT * FROM ProductTable")
    suspend fun getProductFromDb():List<ProductHomeModel>

    @Query("SELECT * FROM ProductTable WHERE productCategory=:category")
    suspend fun getProductByCategory(category:String):List<ProductHomeModel>

    @Query("SELECT * FROM ProductTable WHERE en LIKE '%' || :searchQuery || '%' OR productCategory LIKE '%' || :searchQuery ||  '%' OR ml LIKE '%' || :searchQuery || '%'")
    suspend fun searchForProducts(searchQuery:String):List<ProductHomeModel>

    @Query("SELECT * FROM ProductTable WHERE productId =:productId")
    suspend fun getOfferProduct(productId:Int):ProductHomeModel

    @Query("DELETE FROM ProductTable")
    suspend fun deleteAll()

    @Query("SELECT productId FROM ProductTable WHERE en LIKE '%' || :productName || '%' OR ml LIKE '%' || :productName || '%'")
    suspend fun getProductId(productName:String):Int?

    @Query("SELECT * FROM ProductTable ORDER BY productId ASC LIMIT :limit OFFSET :offset")
    suspend fun getProductByPage(limit: Int, offset: Int):List<ProductHomeModel>

    @Query("SELECT * FROM ProductTable")
     fun getProductFromDbByPaging():androidx.paging.DataSource.Factory<Int,ProductHomeModel>

}