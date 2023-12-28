package com.example.firebaseecom.model

import java.io.Serializable

data class ProductOrderModel(
    val productCategory: String?,
    val productId: Int?,
    val productImage: String?,
    val productPrice: Int?,
    val productMultiLanguage: ProductMultiLanguage,
    val orderDate: String?,
    val deliveryDate:String?,
    val currentGeoPoint:List<Double>
):Serializable{
    constructor() : this("",0, "",0,ProductMultiLanguage("",""),"","", listOf())
}
