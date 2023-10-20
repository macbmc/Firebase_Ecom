package com.example.firebaseecom.model

data class ProductModel
    (
    val productTitle: String?,
    val productPrice: String?,
    val productImage: String?,
    val productDiscount: String?,
    val productDesc:String?,
    val productReview: List<Review>


) {
    data class Review(
        val customerName: String?,
        val content: String?
    )
}
