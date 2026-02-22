package com.example.stikerrli

import android.os.AsyncTask
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.example.stikerrli.databinding.ActivityHomePage5Binding
import org.json.JSONObject

class HomePage5 : AppCompatActivity() {

    private lateinit var binding: ActivityHomePage5Binding
    private lateinit var session: SessionManager

    private var stickerId = 0
    private var stickerName = ""
    private var stickerImage = ""
    private var stickerPrice = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHomePage5Binding.inflate(layoutInflater)
        setContentView(binding.root)

        session = SessionManager(this)

        // Ambil data dari intent
        stickerId = intent.getIntExtra("sticker_id", 0)
        stickerName = intent.getStringExtra("sticker_name") ?: ""
        stickerImage = intent.getStringExtra("sticker_image") ?: ""
        stickerPrice = intent.getIntExtra("sticker_price", 0)

        // Tampilkan di popup
        if (stickerImage.isNotEmpty()) {
            Glide.with(this).load(stickerImage).into(binding.imgPopupSticker)
        }

        // Close popup
        binding.btnClose.setOnClickListener { finish() }

        // Hide popup on overlay tap
        binding.popupOverlay.setOnClickListener { finish() }

        // Add to cart button (icon kiri di popup action bar)
        binding.btnPopupCart.setOnClickListener { addToCart() }

        // Add to favorites button (icon kanan di popup action bar)
        binding.btnPopupFav.setOnClickListener { addToFavorite() }
    }

    private fun addToCart() {
        class AddCart : AsyncTask<Void, Void, String>() {
            override fun doInBackground(vararg p0: Void?): String {
                val params = HashMap<String, String>()
                params["user_id"] = session.getUserId().toString()
                params["sticker_id"] = stickerId.toString()
                return RequestHandler().sendPostRequest(Konfigurasi.URL_CART_ADD, params)
            }

            override fun onPostExecute(s: String) {
                try {
                    val json = JSONObject(s)
                    Toast.makeText(this@HomePage5, json.getString("message"), Toast.LENGTH_SHORT).show()
                } catch (e: Exception) {
                    Toast.makeText(this@HomePage5, "Gagal menambah ke cart", Toast.LENGTH_SHORT).show()
                }
            }
        }
        AddCart().execute()
    }

    private fun addToFavorite() {
        class AddFav : AsyncTask<Void, Void, String>() {
            override fun doInBackground(vararg p0: Void?): String {
                val params = HashMap<String, String>()
                params["user_id"] = session.getUserId().toString()
                params["sticker_id"] = stickerId.toString()
                return RequestHandler().sendPostRequest(Konfigurasi.URL_FAVORITE_ADD, params)
            }

            override fun onPostExecute(s: String) {
                try {
                    val json = JSONObject(s)
                    Toast.makeText(this@HomePage5, json.getString("message"), Toast.LENGTH_SHORT).show()
                } catch (e: Exception) {
                    Toast.makeText(this@HomePage5, "Gagal menambah ke favorit", Toast.LENGTH_SHORT).show()
                }
            }
        }
        AddFav().execute()
    }
}