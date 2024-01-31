package com.example.firebaseecom.api

enum class EkartApiEndPoints(val url: String) {
    END_POINT_BASE("https://raw.githubusercontent.com/"),
    END_POINT_PRODUCTS("/mac-bmc/mac-bmc.github.io/main/product-home.json"),
    END_POINT_DETAIL_MULTILINGUAL("/mac-bmc/mac-bmc.github.io/main/product-details-multilanguage.json"),
    END_POINT_OFFERS("/mac-bmc/mac-bmc.github.io/main/offer-coupons.json"),
    END_POINT_PRODUCT_TESTING("/mac-bmc/mac-bmc.github.io/main/new-product-test.json"),
    END_POINT_OFFER_TYPES("/mac-bmc/mac-bmc.github.io/main/seasonal-offers.json")
}

//val requestBody = jsonObjectString.toRequestBody("application/json".toMediaTypeOrNull())