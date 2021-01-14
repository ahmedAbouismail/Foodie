package de.abou.foodie.storage

import android.graphics.Bitmap
import android.net.Uri
import android.provider.Contacts
import android.provider.MediaStore
import android.util.Log
import androidx.annotation.RestrictTo
import androidx.core.graphics.drawable.toDrawable
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.google.android.gms.tasks.Task
import com.google.firebase.Timestamp
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.StorageException
import com.google.firebase.storage.StorageReference
import com.google.firebase.storage.UploadTask
import com.google.firebase.storage.ktx.storage
import com.google.firebase.storage.ktx.storageMetadata
import com.google.type.Date
import com.google.type.DateTime
import de.abou.foodie.database.MyFirestore
import de.abou.foodie.screens.regestration.signUp.SignUpViewModel
import kotlinx.coroutines.*
import kotlinx.coroutines.tasks.await
import java.io.ByteArrayOutputStream
import java.io.File
import java.net.URL
import java.time.Clock
import java.time.Instant
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import kotlin.coroutines.suspendCoroutine


class MyFirebaseStorage(private val ref :String = "") {


    companion object{
        const val TAG = "MyFireStorage"
    }

    val storage = Firebase.storage


    private lateinit var uploadTask : UploadTask.TaskSnapshot
    lateinit var storageRef : StorageReference

    var metadata = storageMetadata { contentType = "image/jpeg" }
//    private var baos = ByteArrayOutputStream()




//    fun convertToBytes(bitmap:Bitmap): ByteArray{
//
//        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos)
//        return baos.toByteArray()
//    }

    suspend fun uploadImageOnStorage(data:ByteArray){
//        storageRef = storage.getReference("$sort//$userId//$title//${Timestamp.now()}.jpeg")
        storageRef = storage.getReference(ref)
        withContext(Dispatchers.IO){
            storageRef.putBytes(data,metadata).await()
        }
    }

    suspend fun deleteImageFromStorage(imageRef:String){
        withContext(Dispatchers.IO){
            var ref = storage.getReference(imageRef)
            ref.delete().await()
        }
    }

    suspend fun getImageUri(): String {
        return try {
            withContext(Dispatchers.IO){

                storageRef.downloadUrl.await().toString()
            }.toString()
        }catch (e:StorageException){
            throw e
        }

    }

}