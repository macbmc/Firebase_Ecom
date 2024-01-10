package com.example.firebaseecom.model

import java.io.Serializable
import java.util.UUID

data class ProductOrderModel(
    val orderId:String = UUID.randomUUID().toString(),
    val productCategory: String?,
    val productId: Int?,
    val productImage: String?,
    val productPrice: Int?,
    val productMultiLanguage: ProductMultiLanguage,
    val orderDate: String?,
    val deliveryLocation: String?,
    val deliveryDate:String?,
    val currentGeoPoint:List<Double>,
    val currentLocation:String?,
    val productDeliveryStatusCode:Int?
):Serializable{
    constructor() : this("","",0, "",0,ProductMultiLanguage("",""),"","","", listOf(),"",100)
}
