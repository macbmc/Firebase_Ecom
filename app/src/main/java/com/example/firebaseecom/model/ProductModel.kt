package com.example.firebaseecom.model

import java.io.Serializable

data class ProductModel
    (
    val productId:String?,
    val productTitle: String?,
    val productPrice: String?,
    val productImage: String?,
    val productDiscount: String?,
    val productCategory: String?,
    val productDesc: String?,
    val productReview: List<String>


):Serializable {

    constructor() : this("","", "", "", "", "", "",listOf(""))

}
