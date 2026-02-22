package com.example.stikerrli

import android.content.Intent
import android.os.AsyncTask
import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.example.stikerrli.databinding.ActivityHomepageBinding
import org.json.JSONArray
import org.json.JSONObject

class HomePage : AppCompatActivity() {

    private lateinit var binding: ActivityHomepageBinding
    private lateinit var session: SessionManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHomepageBinding.inflate(layoutInflater)
        setContentView(binding.root)

        session = SessionManager(this)

        // Set username
        binding.tvUsername.text = "Hello, ${session.getUserName()}"

        // Load stickers from API
        loadStickers()

        // Bottom nav
        binding.navHome.setOnClickListener { /* sudah di home */ }
        binding.navCategory.setOnClickListener {
            startActivity(Intent(this, CategoryActivity::class.java))
        }
        binding.navCart.setOnClickListener {
            startActivity(Intent(this, KeranjangActivity::class.java))
        }
        binding.navProfile.setOnClickListener {
            startActivity(Intent(this, ProfileActivity::class.java))
        }
    }

    private fun loadStickers() {
        class LoadStickers : AsyncTask<Void, Void, String>() {
            override fun doInBackground(vararg p0: Void?): String {
                val rh = RequestHandler()
                return rh.sendGetRequest(Konfigurasi.URL_STICKER_LIST)
            }

            override fun onPostExecute(s: String) {
                try {
                    val json = JSONObject(s)
                    if (json.getString("status") == "success") {
                        val stickers = json.getJSONArray("stickers")
                        displayStickers(stickers)
                    }
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
        }
        LoadStickers().execute()
    }

    private fun displayStickers(stickers: JSONArray) {
        // Top Stickers - slot 1
        if (stickers.length() > 0) {
            val s = stickers.getJSONObject(0)
            binding.tvTopName1.text = s.getString("name")
            Glide.with(this).load(s.getString("image")).into(binding.imgTop1)
            binding.cardTop1.setOnClickListener { openStickerPopup(s) }
        }
        // Top Stickers - slot 2
        if (stickers.length() > 1) {
            val s = stickers.getJSONObject(1)
            binding.tvTopName2.text = s.getString("name")
            Glide.with(this).load(s.getString("image")).into(binding.imgTop2)
            binding.cardTop2.setOnClickListener { openStickerPopup(s) }
        }
        // Recommended - slot 1
        if (stickers.length() > 2) {
            val s = stickers.getJSONObject(2)
            binding.tvRecName1.text = s.getString("name")
            Glide.with(this).load(s.getString("image")).into(binding.imgRec1)
            binding.cardRec1.setOnClickListener { openStickerPopup(s) }
        }
        // Recommended - slot 2
        if (stickers.length() > 3) {
            val s = stickers.getJSONObject(3)
            binding.tvRecName2.text = s.getString("name")
            Glide.with(this).load(s.getString("image")).into(binding.imgRec2)
            binding.cardRec2.setOnClickListener { openStickerPopup(s) }
        }
    }

    private fun openStickerPopup(sticker: JSONObject) {
        val intent = Intent(this, HomePage5::class.java)
        intent.putExtra("sticker_id", sticker.getInt("id"))
        intent.putExtra("sticker_name", sticker.getString("name"))
        intent.putExtra("sticker_image", sticker.getString("image"))
        intent.putExtra("sticker_price", sticker.getInt("price"))
        startActivity(intent)
    }
}