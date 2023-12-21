package com.example.firebaseecom.model

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import java.io.Serializable

@JsonClass(generateAdapter = true)
data class ProductOffersModel(
    @Json(name = "product_id")
    val productId: Int?,
    @Json(name = "product_discount_percent")
    val productDiscount: Int?,
    @Json(name = "product_offer_description")
    val productOfferDesc: String?,
    @Json(name = "coupon_discount")
    val couponDiscount: String?,
    @Json(name = "coupon_discount_description")
    val couponDiscountDesc: String?,
    @Json(name = "coupon_vouchers")
    val couponVouchers: String?,
    @Json(name = "coupon_vouchers_description")
    val couponVouchersDesc: String?
) : Serializable {
    constructor() : this(0, 0, "", "", "", "", "")
}

