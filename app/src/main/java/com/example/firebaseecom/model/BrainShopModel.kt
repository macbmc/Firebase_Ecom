package com.example.firebaseecom.model


import com.squareup.moshi.Json

data class BrainShopModel(
    @Json(name = "cnt")
    val cnt: String?,
    val code:Int?
)