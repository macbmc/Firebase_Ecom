package com.example.firebaseecom.model


import com.squareup.moshi.Json
import java.io.Serializable

data class ProductDetailsModel(
        @Json(name = "product_description")
        val productDescription: String?,
        @Json(name = "product_discount")
        val productDiscount: String?,
        @Json(name = "product_id")
        val productId: Int?,
        @Json(name = "product_reviews")
        val productReviews: String?
    ): Serializable {

        constructor() : this("","", 0,"")

}
