package com.example.firebaseecom.model

import com.squareup.moshi.Json
import java.io.Serializable
import kotlin.reflect.full.memberProperties
import kotlin.reflect.jvm.isAccessible

data class ProductTitle(
    @Json(name = "en")
    val en : String?,
    @Json(name = "ml")
    val ml : String?

): Serializable {
    constructor() : this("", "")
}
fun ProductTitle.asMap() = this::class.memberProperties.associate { it.name to it.getter.call(this)}



