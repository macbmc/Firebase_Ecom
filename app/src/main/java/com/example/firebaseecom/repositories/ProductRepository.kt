package com.example.firebaseecom.repositories

import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.os.Environment
import android.util.Log
import androidx.core.content.FileProvider
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.CustomTarget
import com.bumptech.glide.request.transition.Transition
import com.example.firebaseecom.detailsPg.ProductDetailsActivity
import com.example.firebaseecom.model.ProductHomeModel
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.util.Objects
import javax.inject.Inject

interface ProductRepository {

    fun shareProduct(productHomeModel: ProductHomeModel)


}

class ProductRepositoryImpl @Inject constructor(
    private val context: Context
) :
    ProductRepository {

    override fun shareProduct(productHomeModel: ProductHomeModel) {
        val timeStamp = System.currentTimeMillis()
        Glide.with(context)
            .asBitmap()
            .load(productHomeModel.productImage)
            .into(object : CustomTarget<Bitmap>() {
                override fun onResourceReady(resource: Bitmap, transition: Transition<in Bitmap>?) {
                    try {

                        val file = File(
                            Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS),
                            "share_image${productHomeModel.productTitle}_${timeStamp}.png"
                        )
                        Objects.requireNonNull(file.parentFile).mkdirs()
                        val out = FileOutputStream(file)
                        resource.compress(Bitmap.CompressFormat.PNG, 90, out)
                        out.close()
                        val bitmapUri = FileProvider
                            .getUriForFile(
                                context, context.applicationContext
                                    .packageName + ".provider", file
                            )
                        Log.d("bitmap", bitmapUri.toString())
                        if (bitmapUri != null) {
                            val msg =
                                " " + productHomeModel.productTitle.en + " " + productHomeModel.productPrice.toString()
                            val shareIntent = Intent()
                            shareIntent.action = Intent.ACTION_SEND
                            shareIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                            shareIntent.type = "text/plain"
                            shareIntent.putExtra(
                                Intent.EXTRA_TEXT,
                                msg
                            )
                            shareIntent.putExtra(Intent.EXTRA_STREAM, bitmapUri)
                            shareIntent.type = "image/*"

                            val chooserIntent = Intent.createChooser(shareIntent, "Share via")
                            chooserIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                            context.startActivity(chooserIntent)
                        } else {
                            Log.d("bitmap uri", "failed")
                        }
                    } catch (e: IOException) {
                        e.printStackTrace()
                    }
                }

                override fun onLoadCleared(placeholder: Drawable?) {
                    Log.d("clear", "clear")
                }

            })
    }


}