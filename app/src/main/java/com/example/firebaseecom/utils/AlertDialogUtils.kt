package com.example.firebaseecom.utils

import android.app.AlertDialog
import android.content.Context
import android.text.InputType
import android.text.method.PasswordTransformationMethod
import android.util.Log
import android.view.LayoutInflater
import android.widget.EditText
import android.widget.RatingBar
import android.widget.TextView
import androidx.lifecycle.MutableLiveData
import com.example.firebaseecom.R
import com.example.firebaseecom.model.ProductOrderModel
import com.example.firebaseecom.model.ProductOrderReviews
import kotlinx.coroutines.flow.MutableStateFlow

class AlertDialogUtils {
    val productReview = MutableLiveData<ProductOrderReviews>()
    val responsePassword = MutableStateFlow<String?>(null)

    fun showAlertDialogNotification(context: Context, dialogContent: String) {
        Log.d("AlertDialog", "called")
        val alertDialog = AlertDialog.Builder(context)
        val optionDialog = alertDialog.create()
        val alertViewInflater =
            LayoutInflater.from(context).inflate(R.layout.newuser_alert_dialog, null)
        alertDialog.setView(alertViewInflater)
        alertViewInflater.findViewById<TextView>(R.id.dialogContent).text = dialogContent
        alertDialog.setPositiveButton(R.string.con) { _, _ ->
            optionDialog.dismiss()
        }
            .setCancelable(false)
        alertDialog.show()
    }

    fun showReviewAlertDialog(context: Context, productOrderModel: ProductOrderModel) {

        val reviewDialog = AlertDialog.Builder(context)
        val optionDialog = reviewDialog.create()
        val reviewInflater =
            LayoutInflater.from(context).inflate(R.layout.review_dialog_layout, null)
        reviewDialog.setView(reviewInflater)
        reviewDialog.setNegativeButton(R.string.cancel) { _, _ ->
            optionDialog.dismiss()
        }
        reviewDialog.setPositiveButton(R.string.submit) { _, _ ->
            reviewInflater.findViewById<EditText>(R.id.editTextProductReview)
            productReview.postValue(
                ProductOrderReviews(
                    productOrderModel.productId,
                    reviewInflater.findViewById<EditText>(R.id.editTextDeliveryReview).text.toString(),
                    reviewInflater.findViewById<EditText>(R.id.editTextProductReview).text.toString(),
                    reviewInflater.findViewById<RatingBar>(R.id.productRating).rating,
                    ""
                )
            )


        }
        reviewDialog.show()

    }

    fun showDeleteUserDialog(context: Context) {
        val builder = AlertDialog.Builder(context)
        val editText = EditText(context)
        editText.transformationMethod = PasswordTransformationMethod.getInstance()
        builder.setTitle(context.getString(R.string.delete_user))
        builder.setMessage(context.getString(R.string.enter_password))
        builder.setView(editText)
        builder.setPositiveButton(R.string.submit) { _, _ ->
            responsePassword.value = editText.text.toString()

        }
        builder.setNegativeButton(R.string.cancel) { _, _ -> }
        builder.show()
    }
}