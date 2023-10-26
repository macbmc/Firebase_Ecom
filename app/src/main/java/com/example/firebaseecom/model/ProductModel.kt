package com.example.firebaseecom.model

data class ProductModel
    (
    val productTitle: String?,
    val productPrice: String?,
    val productImage: String?,
    val productDiscount: String?,
    val productCategory: String?,
    val productDesc: String?,
    val productReview: List<String>


) {

    constructor() : this("", "", "", "", "", "",listOf(""))

}
