package com.catedium.catedium

import android.app.AlertDialog
import android.app.ProgressDialog
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import com.catedium.catedium.databinding.ActivityHomeScreenBinding
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.File

class HomeScreenActivity : AppCompatActivity() {

    private lateinit var binding: ActivityHomeScreenBinding
    private lateinit var photoFile: File
    private lateinit var progressDialog: ProgressDialog

    private val requestCameraPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted: Boolean ->
        if (isGranted) {
            openCamera()
        } else {
            Toast.makeText(this, "Camera permission is required to take photos", Toast.LENGTH_SHORT).show()
        }
    }

    private val pickImageLauncher = registerForActivityResult(
        ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        uri?.let { handleSelectedImage(it) }
    }


    private val takePictureLauncher = registerForActivityResult(
        ActivityResultContracts.TakePicture()
    ) { success ->
        if (success) {
            Log.d("HomeScreenActivity", "Photo captured successfully: ${photoFile.absolutePath}")
            sendPhotoToServer(photoFile)
        } else {
            Log.e("HomeScreenActivity", "Photo capture failed")
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHomeScreenBinding.inflate(layoutInflater)
        setContentView(binding.root)

        progressDialog = ProgressDialog(this).apply {
            setMessage("Uploading photo, please wait...")
            setCancelable(false)
        }

        binding.camera.setOnClickListener {
            showImageSourceDialog()
        }

        binding.book.setOnClickListener {
            val intent = Intent(this, PustakaActivity::class.java)
            startActivity(intent)
        }

        binding.callCenter.setOnClickListener {
            val intent = Intent(this, ContactActivity::class.java)
            startActivity(intent)
        }

        binding.next.setOnClickListener{
            val intent = Intent(this, ComingActivity::class.java)
            startActivity(intent)
        }
    }

    private fun showImageSourceDialog() {
        val options = arrayOf("Take Photo", "Upload from Gallery")
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Select Option")
        builder.setItems(options) { _, which ->
            when (which) {
                0 -> checkCameraPermissionAndOpenCamera()
                1 -> pickImageFromGallery()
            }
        }
        builder.show()
    }

    private fun checkCameraPermissionAndOpenCamera() {
        when {
            ContextCompat.checkSelfPermission(
                this,
                android.Manifest.permission.CAMERA
            ) == PackageManager.PERMISSION_GRANTED -> {
                openCamera()
            }
            else -> {
                requestCameraPermissionLauncher.launch(android.Manifest.permission.CAMERA)
            }
        }
    }

    private fun openCamera() {
        val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        photoFile = createImageFile()
        val fileProvider = FileProvider.getUriForFile(this, "${applicationContext.packageName}.fileprovider", photoFile)
        takePictureLauncher.launch(fileProvider)
    }

    private fun createImageFile(): File {
        val storageDir = getExternalFilesDir(null)
        return File.createTempFile("photo_", ".jpg", storageDir)
    }

    private fun pickImageFromGallery() {
        pickImageLauncher.launch("image/*")
    }

    private fun handleSelectedImage(uri: Uri) {
        val filePathColumn = arrayOf(MediaStore.Images.Media.DATA)
        val cursor = contentResolver.query(uri, filePathColumn, null, null, null)
        cursor?.moveToFirst()
        val columnIndex = cursor?.getColumnIndex(filePathColumn[0])
        val picturePath = columnIndex?.let { cursor.getString(it) }
        cursor?.close()
        picturePath?.let {
            photoFile = File(it)
            Log.d("HomeScreenActivity", "Photo selected from gallery: ${photoFile.absolutePath}")
            sendPhotoToServer(photoFile)
        } ?: run {
            Log.e("HomeScreenActivity", "Failed to get photo path from gallery")
        }
    }

    private fun handleSelectedImages(uris: List<Uri>) {
        uris.forEach { uri ->
            handleSelectedImage(uri)
        }
    }

    private fun sendPhotoToServer(photoFile: File) {
        progressDialog.show()

        val requestFile = RequestBody.create("image/jpeg".toMediaTypeOrNull(), photoFile)
        val body = MultipartBody.Part.createFormData("file", photoFile.name, requestFile)

        val apiService = CatediumApi.retrofitService
        val call = apiService.uploadPhoto(body)

        call.enqueue(object : Callback<PhotoApiResponse> {
            override fun onResponse(call: Call<PhotoApiResponse>, response: Response<PhotoApiResponse>) {
                progressDialog.dismiss()
                if (response.isSuccessful) {
                    val animalClass = response.body()?.data?.`class`
                    if (animalClass != null) {
                        showSuccessNotification(animalClass)
                        val intent = Intent(this@HomeScreenActivity, PustakaActivity::class.java)
                        intent.putExtra("animal_class", animalClass)
                        startActivity(intent)
                    } else {
                        showFailureNotification()
                    }
                } else {
                    showFailureNotification()
                }
            }

            override fun onFailure(call: Call<PhotoApiResponse>, t: Throwable) {
                progressDialog.dismiss()
                showFailureNotification()
            }
        })
    }

    private fun showSuccessNotification(animalClass: String) {
        Toast.makeText(this, "Animal class: $animalClass", Toast.LENGTH_LONG).show()
    }

    private fun showFailureNotification() {
        Toast.makeText(this, "Gagal memeriksa", Toast.LENGTH_SHORT).show()
    }

    companion object {
        const val REQUEST_IMAGE_CAPTURE = 1
        const val REQUEST_STORAGE_PERMISSIONS = 2
    }
}
