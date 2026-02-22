package com.example.stikerrli

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.stikerrli.databinding.ActivityAdminDashboardBinding

class AdminDashboardActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAdminDashboardBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAdminDashboardBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnAdminListStickers.setOnClickListener {
            val intent = Intent(this, AdminStickerListActivity::class.java)
            startActivity(intent)
        }

        binding.btnAdminListOrders.setOnClickListener {
            val intent = Intent(this, AdminOrderListActivity::class.java)
            startActivity(intent)
        }

        binding.btnAdminAddSticker.setOnClickListener {
            val intent = Intent(this, AdminAddStickerActivity::class.java)
            startActivity(intent)
        }

        binding.btnAdminProfile.setOnClickListener {
            val intent = Intent(this, ProfileActivity::class.java)
            startActivity(intent)
        }
    }
}