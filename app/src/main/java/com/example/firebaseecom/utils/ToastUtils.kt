package com.example.firebaseecom.utils

import android.content.Context
import android.widget.Toast
import javax.inject.Inject

class ToastUtils {

    fun giveToast(msg: String,context:Context) {

        Toast.makeText(context,msg,Toast.LENGTH_LONG).show()

    }
}

/*
,
{
    "product_id": 103,
    "product_title": {
    "en": "Asus Rog G16",
    "ml": "അസൂസ് റോഗ് ജി 16"
},
    "product_price": 120000,
    "product_category": "Laptop",
    "product_image": "https://firebasestorage.googleapis.com/v0/b/imageapi-ecom.appspot.com/o/rog-1-removebg-preview.png?alt=media&token=c74c4d83-e7dc-4658-bca9-da974c5f771d"
},
{
    "product_id": 104,
    "product_title": {
    "en": "Samsung S6 lite",
    "ml": "സാംസങ് എസ്6 ലൈറ്റ്"
},
    "product_price": 17000,
    "product_category": "Tablet",
    "product_image": "https://firebasestorage.googleapis.com/v0/b/imageapi-ecom.appspot.com/o/s6lite-1-removebg-preview.png?alt=media&token=8480dc39-52b0-49bf-abf9-952366fa012d"
},
{
    "product_id": 105,
    "product_title": {
    "en": "Samsung S9 FE",
    "ml": "സാംസങ് എസ്9 FE"
},
    "product_price": 46000,
    "product_category": "Tablet",
    "product_image": "https://firebasestorage.googleapis.com/v0/b/imageapi-ecom.appspot.com/o/s9fe-1-removebg-preview.png?alt=media&token=b413cda3-4487-4e5f-a081-abb9d039009b"
},
{
    "product_id": 106,
    "product_title": {
    "en": "Samsung S23 Ultra",
    "ml": "സാംസങ് എസ്23 അൾട്രാ"
},
    "product_price": 125000,
    "product_category": "Phone",
    "product_image": "https://firebasestorage.googleapis.com/v0/b/imageapi-ecom.appspot.com/o/s23-1-removebg-preview.png?alt=media&token=0fb29bfc-42e1-4f6b-8432-32f67dbaf344"
},
{
    "product_id": 107,
    "product_title": {
    "en": "Nothing Phone(2)",
    "ml": "Nothing ഫോൺ(2)"
},
    "product_price": 45000,
    "product_category": "Phone",
    "product_image": "https://firebasestorage.googleapis.com/v0/b/imageapi-ecom.appspot.com/o/no-1-removebg-preview.png?alt=media&token=8fe4f4ab-55cb-4d09-ae66-afdd5da00b19"
},
{
    "product_id": 108,
    "product_title": {
    "en": "Oneplus 11",
    "ml": "വൺപ്ലസ് 11"
},
    "product_price": 45000,
    "product_category": "Phone",
    "product_image": "https://firebasestorage.googleapis.com/v0/b/imageapi-ecom.appspot.com/o/11-1-removebg-preview.png?alt=media&token=ff47e7fa-49a9-4b1b-9f6a-7fb790a86b4e"
},
{
    "product_id": 109,
    "product_title": {
    "en": "Apple iPhone 15",
    "ml": "ആപ്പിൾ ഐഫോൺ 15 പ്രോ"
},
    "product_price": 135000,
    "product_category": "Phone",
    "product_image": "https://firebasestorage.googleapis.com/v0/b/imageapi-ecom.appspot.com/o/i-1-removebg-preview.png?alt=media&token=f0857e4c-d67b-4c74-a61a-d0670c627784"
},
{
    "product_id": 110,
    "product_title": {
    "en": "Apple iPhone 15",
    "ml": "ആപ്പിൾ ഐഫോൺ 15 പ്രോ"
},
    "product_price": 135000,
    "product_category": "Phone",
    "product_image": "https://firebasestorage.googleapis.com/v0/b/imageapi-ecom.appspot.com/o/i-1-removebg-preview.png?alt=media&token=f0857e4c-d67b-4c74-a61a-d0670c627784"
}*/
