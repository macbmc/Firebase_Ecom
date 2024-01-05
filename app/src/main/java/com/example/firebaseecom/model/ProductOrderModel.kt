package com.example.firebaseecom.model

import java.io.Serializable

data class ProductOrderModel(
    val productCategory: String?,
    val productId: Int?,
    val productImage: String?,
    val productPrice: Int?,
    val productMultiLanguage: ProductMultiLanguage,
    val orderDate: String?,
    val deliveryLocation: String?,
    val deliveryDate:String?,
    val currentGeoPoint:List<Double>,
    val currentLocation:String?
):Serializable{
    constructor() : this("",0, "",0,ProductMultiLanguage("",""),"","","", listOf(),"")
}
