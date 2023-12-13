package com.example.firebaseecom.model


import com.squareup.moshi.Json
import java.io.Serializable

data class ProductDetailsModel(
    @Json(name = "product_description")
    val productDescription: ProductMultiLanguage,
    @Json(name = "product_id")
    val productId: Int?,
    @Json(name = "product_reviews")
    val productReviews: ProductMultiLanguage,
    @Json(name = "product_image")
    val productImage: List<String>?
) : Serializable {

    constructor() : this(ProductMultiLanguage("",""), 0, ProductMultiLanguage("",""), listOf())

}
