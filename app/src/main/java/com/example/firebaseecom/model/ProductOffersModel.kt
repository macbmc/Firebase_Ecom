package com.example.firebaseecom.model

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import java.io.Serializable

@JsonClass(generateAdapter = true)
data class ProductOffersModel(
    @Json(name = "product_id")
    val productId: Int?,
    @Json(name = "product_discount_percent")
    val productDiscount: Int?
):Serializable{

    constructor():this(0,0)
}
