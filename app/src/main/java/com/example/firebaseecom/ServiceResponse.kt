package com.example.firebaseecom

import com.example.firebaseecom.model.ProductHomeModel

class SearchResponse {
    private val data: List<ProductHomeModel>? = null
    fun getBooks(): List<ProductHomeModel>? {
        return data
    }
}