package com.example.firebaseecom.api

enum class ProductOrderDeliveryStatus(val statusCode:Int,val msg:String) {
    ORDER_PACKING(100,"Order Dispatch in Progress"),
    ORDER_DISPATCHED(101,"Order on the move"),
    ORDER_OUT_FOR_DELIVERY(102,"Order out for delivery"),
    ORDER_DELIVERED(103,"Order Delivered")


}