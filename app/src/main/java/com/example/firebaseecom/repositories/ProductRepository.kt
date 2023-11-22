package com.example.firebaseecom.repositories

import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.net.Uri
import android.os.Environment
import android.util.Log
import androidx.core.content.FileProvider
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.CustomTarget
import com.bumptech.glide.request.transition.Transition
import com.example.firebaseecom.model.ProductHomeModel
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.util.Objects
import javax.inject.Inject

interface ProductRepository {

    fun getUri(productHomeModel: ProductHomeModel,context: Context)
    fun shareProduct(uri: Uri, productHomeModel: ProductHomeModel,context: Context)


}

class ProductRepositoryImpl @Inject constructor() :
    ProductRepository {
    override fun getUri(productHomeModel: ProductHomeModel,context: Context) {
        var bitmapUri: Uri? = null
        Glide.with(context)
            .asBitmap()
            .load(productHomeModel.productImage)
            .into(object : CustomTarget<Bitmap>() {
                override fun onResourceReady(resource: Bitmap, transition: Transition<in Bitmap>?) {
                    try {

                        val file = File(
                            Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS),
                            "share_image${productHomeModel.productId}.png"
                        )
                        Objects.requireNonNull(file.parentFile).mkdirs()
                        val out = FileOutputStream(file)
                        resource.compress(Bitmap.CompressFormat.PNG, 90, out)
                        out.close()
                        bitmapUri = FileProvider
                            .getUriForFile(
                                context, context.applicationContext
                                    .packageName + ".provider", file
                            )
                    } catch (e: IOException) {
                        e.printStackTrace()
                    }
                    shareProduct(bitmapUri!!, productHomeModel,context)
                }

                override fun onLoadCleared(placeholder: Drawable?) {
                    Log.d("clear", "clear")
                }

            })
    }

    override fun shareProduct(uri: Uri, productHomeModel: ProductHomeModel, context: Context) {
        if (uri != null) {
            val msg = " "+productHomeModel.productTitle.en +" "+productHomeModel.productPrice.toString()
            val shareIntent = Intent()
            shareIntent.action = Intent.ACTION_SEND
            shareIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            shareIntent.type = "text/plain"
            shareIntent.putExtra(
                Intent.EXTRA_TEXT,
                msg
            )
            shareIntent.putExtra(Intent.EXTRA_STREAM, uri)
            shareIntent.type = "image/*"

            context.startActivity(Intent.createChooser(shareIntent, "Share Image"))
        } else {
            Log.d("bitmap uri", "failed")
        }


    }
}