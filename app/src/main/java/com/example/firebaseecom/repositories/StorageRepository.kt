package com.example.firebaseecom.repositories

import android.net.Uri
import android.util.Log
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.storage.FirebaseStorage
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

interface StorageRepository {
    suspend fun storeImageAndGetUrl(imageUri: Uri): Flow<String>
}

class StorageRepositoryImp @Inject constructor(
    firebaseStorage: FirebaseStorage,
    firebaseAuth: FirebaseAuth
) : StorageRepository {

    private val storage = firebaseStorage.reference
    private val currentUser = firebaseAuth.currentUser
    private val pathReference = storage.child("profileImages/${currentUser?.uid}")

    override suspend fun storeImageAndGetUrl(imageUri: Uri) = flow {
        try {
            pathReference.putFile(imageUri).await()
            val downloadUrl = pathReference.downloadUrl.await()
            emit(downloadUrl.toString())

        } catch (e: Exception) {
            Log.d("ImageDownloadUrl", e.toString())
        }
    }

}