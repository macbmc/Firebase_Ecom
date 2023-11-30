package com.example.firebaseecom.repositories

import android.net.Uri
import android.util.Log
import com.google.android.gms.tasks.Tasks
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.storage.FirebaseStorage
import javax.inject.Inject

interface StorageRepository {
    suspend fun addImagetoStorage(imgUri: Uri)
    suspend fun getImageUrl(): String
}

class StorageRepositoryImpl @Inject constructor(
    firebaseStorage: FirebaseStorage,
    firebaseAuth: FirebaseAuth
) : StorageRepository {

    private val storage = firebaseStorage.reference
    private val currentUser = firebaseAuth.currentUser

    override suspend fun addImagetoStorage(imgUri: Uri) {
        val pathReference = storage.child("profileImages/${currentUser?.uid}")
        try {
            pathReference.putFile(imgUri)
        } catch (e: Exception) {
            Log.d("addImageToStorage", e.toString())
        }
    }

    override suspend fun getImageUrl(): String {
        val pathReference = storage.child("profileImages/${currentUser?.uid}")
        var imageUrl = ""
        try {

            Tasks.await(
                pathReference.downloadUrl.addOnCompleteListener {
                    if (it.isSuccessful) {
                        imageUrl = it.result.toString()
                        Log.d("getImageUrl", imageUrl)
                    }
                }
                    .addOnFailureListener {
                        Log.d("getImageUrl", it.toString())
                    }
            )

        } catch (e: Exception) {
            Log.d("getImageUrl", e.toString())
        }
        Log.d("getImageUrl", imageUrl)
        return imageUrl
    }

}