package com.example.stikerrli

import Network.RetrofitClient
import Network.Sticker
import Network.StickerListResponse
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.stikerrli.databinding.ActivityAdminStickerListBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class AdminStickerListActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAdminStickerListBinding
    private lateinit var adapter: AdminStickerAdapter
    private lateinit var session: SessionManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAdminStickerListBinding.inflate(layoutInflater)
        setContentView(binding.root)

        session = SessionManager(this)

        setupRecyclerView()
        fetchStickers()
    }

    private fun setupRecyclerView() {
        adapter = AdminStickerAdapter(emptyList(),
            onEditClick = { sticker ->
                // TODO: Handle edit sticker action
                Toast.makeText(this, "Edit: ${sticker.name}", Toast.LENGTH_SHORT).show()
            },
            onDeleteClick = { sticker ->
                // TODO: Handle delete sticker action
                Toast.makeText(this, "Delete: ${sticker.name}", Toast.LENGTH_SHORT).show()
            }
        )
        binding.rvAdminStickers.layoutManager = LinearLayoutManager(this)
        binding.rvAdminStickers.adapter = adapter
    }

    private fun fetchStickers() {
        val userId = session.getUserId()
        if (userId == 0) {
            Toast.makeText(this, "Error: User not logged in.", Toast.LENGTH_LONG).show()
            return
        }

        RetrofitClient.instance.adminGetStickers(userId).enqueue(object : Callback<StickerListResponse> {
            override fun onResponse(call: Call<StickerListResponse>, response: Response<StickerListResponse>) {
                if (response.isSuccessful) {
                    val stickerResponse = response.body()
                    if (stickerResponse != null && stickerResponse.status == "success") {
                        // Handle case where data might be null (e.g., no stickers)
                        adapter.updateData(stickerResponse.data ?: emptyList())
                    } else {
                        Toast.makeText(this@AdminStickerListActivity, "Failed to fetch stickers: ${stickerResponse?.message}", Toast.LENGTH_LONG).show()
                    }
                } else {
                    Toast.makeText(this@AdminStickerListActivity, "Error: ${response.message()}", Toast.LENGTH_LONG).show()
                }
            }

            override fun onFailure(call: Call<StickerListResponse>, t: Throwable) {
                Toast.makeText(this@AdminStickerListActivity, "Network Error: ${t.message}", Toast.LENGTH_LONG).show()
            }
        })
    }
}