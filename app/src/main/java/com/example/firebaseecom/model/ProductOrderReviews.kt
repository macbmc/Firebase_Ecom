package com.example.firebaseecom.model

import java.io.Serializable

data class ProductOrderReviews(
    val productId:Int?,
    val deliveryReview:String?,
    val productReview:String?,
    val productRating:Float?,
    var userId:String?
):Serializable
{
    constructor():this(0,"","",0F,"")
}
