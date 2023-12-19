package com.example.firebaseecom.model


import com.squareup.moshi.Json


data class OfferModelClass(
    @Json(name = "offer_image")
    val offerImage: String?,
    @Json(name = "offer_name")
    val offerName: String?,
    @Json(name = "offer_text")
    val offerText: String?
)
