package de.abou.foodie.storage

import android.graphics.Bitmap
import android.net.Uri
import android.provider.MediaStore
import android.util.Log
import androidx.core.graphics.drawable.toDrawable
import androidx.lifecycle.LiveData
import com.google.firebase.Timestamp
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.StorageException
import com.google.firebase.storage.UploadTask
import com.google.firebase.storage.ktx.storage
import com.google.type.Date
import com.google.type.DateTime
import kotlinx.coroutines.tasks.await
import java.io.ByteArrayOutputStream
import java.io.File
import java.time.Clock
import java.time.Instant
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class MyFirebaseStorage(private val sort:String,
                        private val userId:String,
                        private val title:String,
                        private val bitmap:Bitmap) {

    companion object{
        const val TAG = "MyFireStorage"
    }
    private val storage = Firebase.storage
    private val storageRef = storage.getReference("$sort//$userId//$title//${Timestamp.now()}.jpeg")

    private lateinit var uploadTask : UploadTask.TaskSnapshot

    private var baos = ByteArrayOutputStream()
    private lateinit var data  : ByteArray

    suspend fun uploadImageInStorage(){

        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos)
        data = baos.toByteArray()

        try {
            uploadTask = storageRef.putBytes(data).await()
        }catch (e:StorageException){
            Log.e(TAG, "Can't upload the Photo", e)
        }
    }


}