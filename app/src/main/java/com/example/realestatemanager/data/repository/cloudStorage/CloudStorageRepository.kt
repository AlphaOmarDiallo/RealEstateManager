package com.example.realestatemanager.data.repository.cloudStorage

import android.content.ContentValues.TAG
import android.net.Uri
import android.util.Log
import com.google.firebase.storage.FirebaseStorage
import java.io.File

class CloudStorageRepository {

    private val firebaseStorage: FirebaseStorage = FirebaseStorage.getInstance()
    private var storageReference = firebaseStorage.reference

    /*// Points to "images"
    imagesRef = storageRef.child("images")*/

    fun uploadFileInCloudStorage(path: String) {
        val file = Uri.fromFile(File("path/to/images/rivers.jpg"))
        val propertyPhotoRef = storageReference.child("images/${file.lastPathSegment}")
        val uploadTask = propertyPhotoRef.putFile(file)

// Register observers to listen for when the download is done or if it fails
        uploadTask.addOnFailureListener {
            Log.e(TAG, "uploadFileInCloudStorage: Error")
        }.addOnSuccessListener { taskSnapshot ->

        }
    }

}