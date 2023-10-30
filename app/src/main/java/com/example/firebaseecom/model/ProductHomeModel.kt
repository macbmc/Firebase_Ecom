package com.example.firebaseecom.model


import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class ProductHomeModel
    (
    @Json(name = "product_category")
    val productCategory: String?,
    @Json(name = "product_id")
    val productId: Int?,
    @Json(name = "product_image")
    val productImage: String?,
    @Json(name = "product_price")
    val productPrice: Int?,
    @Json(name = "product_title")
    val productTitle: String?
)