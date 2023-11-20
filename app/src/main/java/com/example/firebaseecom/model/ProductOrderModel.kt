package com.example.firebaseecom.model

import java.io.Serializable

data class ProductOrderModel(
    val productCategory: String?,
    val productId: Int?,
    val productImage: String?,
    val productPrice: Int?,
    val productTitle: String?,
    val orderDate: String?,
    val deliveryDate:String?
):Serializable{
    constructor() : this("",0, "",0,"","","")
}
