package com.example.stikerrli

import Network.AddStickerRequest
import Network.AuthResponse
import Network.RetrofitClient
import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import com.example.stikerrli.databinding.ActivityAdminAddStickerBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class AdminAddStickerActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAdminAddStickerBinding
    private var selectedImageUri: Uri? = null

    private val imagePickerLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
        result ->
        if (result.resultCode == Activity.RESULT_OK) {
            result.data?.data?.let {
                uri ->
                selectedImageUri = uri
                binding.imgPreviewAddSticker.setImageURI(uri)
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAdminAddStickerBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnSelectImage.setOnClickListener {
            val intent = Intent(Intent.ACTION_GET_CONTENT)
            intent.type = "image/*"
            imagePickerLauncher.launch(intent)
        }

        binding.btnSaveSticker.setOnClickListener {
            saveSticker()
        }
    }

    private fun saveSticker() {
        val name = binding.etAddStickerName.text.toString().trim()
        val priceStr = binding.etAddStickerPrice.text.toString().trim()
        val categoryIdStr = binding.etAddStickerCategoryId.text.toString().trim()

        if (name.isEmpty() || priceStr.isEmpty() || categoryIdStr.isEmpty()) {
            Toast.makeText(this, "Please fill all fields", Toast.LENGTH_SHORT).show()
            return
        }

        if (selectedImageUri == null) {
            Toast.makeText(this, "Please select an image", Toast.LENGTH_SHORT).show()
            return
        }

        // TODO: Implement image upload to server to get a URL.
        // For now, we use a placeholder URL.
        val imageUrl = "http://example.com/path/to/uploaded/image.jpg"

        val request = AddStickerRequest(
            name = name,
            price = priceStr.toInt(),
            categoryId = categoryIdStr.toInt(),
            imageUrl = imageUrl // This should be the URL from your server after upload
        )

        RetrofitClient.instance.adminAddSticker(request).enqueue(object : Callback<AuthResponse> {
            override fun onResponse(call: Call<AuthResponse>, response: Response<AuthResponse>) {
                if (response.isSuccessful && response.body()?.status == "success") {
                    Toast.makeText(this@AdminAddStickerActivity, "Sticker added successfully!", Toast.LENGTH_LONG).show()
                    finish() // Go back to the dashboard
                } else {
                    Toast.makeText(this@AdminAddStickerActivity, "Failed to add sticker: ${response.body()?.message}", Toast.LENGTH_LONG).show()
                }
            }

            override fun onFailure(call: Call<AuthResponse>, t: Throwable) {
                Toast.makeText(this@AdminAddStickerActivity, "Network Error: ${t.message}", Toast.LENGTH_LONG).show()
            }
        })
    }
}