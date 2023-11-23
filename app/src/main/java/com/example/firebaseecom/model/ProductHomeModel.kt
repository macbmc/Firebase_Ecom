package com.example.firebaseecom.model


import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import java.io.Serializable

@JsonClass(generateAdapter = true)
@Entity(tableName = "ProductTable")
data class ProductHomeModel(
    @Json(name = "product_category")
    val productCategory: String?,
    @PrimaryKey
    @Json(name = "product_id")
    val productId: Int?,
    @Json(name = "product_image")
    val productImage: String?,
    @Json(name = "product_price")
    val productPrice: Int?,
    @Embedded
    @Json(name = "product_title")
    val productTitle: ProductMultiLanguage,

    ) : Serializable {
    constructor() : this("", 0, "", 0, ProductMultiLanguage("",""))
}