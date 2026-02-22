package com.example.stikerrli

import android.content.Intent
import android.os.AsyncTask
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.stikerrli.databinding.ActivityCategoryBinding
import org.json.JSONArray
import org.json.JSONObject

class CategoryActivity : AppCompatActivity() {

    private lateinit var binding: ActivityCategoryBinding
    private lateinit var session: SessionManager
    private lateinit var rvStickers: RecyclerView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCategoryBinding.inflate(layoutInflater)
        setContentView(binding.root)

        session = SessionManager(this)

        rvStickers = binding.rvStickers

        rvStickers.layoutManager = GridLayoutManager(this, 2)

        // Back button
        binding.btnBack.setOnClickListener { finish() }

        // Category buttons → map ke category_id di DB
        // DB: 1=Cute, 2=Animal, 3=Meme, 4=Anime
        binding.btnCat1.setOnClickListener { loadByCategory(1) } // Cute
        binding.btnCat2.setOnClickListener { loadByCategory(2) } // Animal
        binding.btnCat3.setOnClickListener { loadByCategory(0) } // Astro (all)
        binding.btnCat4.setOnClickListener { loadByCategory(3) } // Meme
        binding.btnCat5.setOnClickListener { loadByCategory(0) } // Spooky (all)
        binding.btnCat6.setOnClickListener { loadByCategory(0) } // Chara (all)
        binding.btnCat7.setOnClickListener { loadByCategory(0) } // Music (all)
        binding.btnCat8.setOnClickListener { loadByCategory(0) } // Movie (all)
        binding.btnCat9.setOnClickListener { loadByCategory(0) } // Quotes (all)

        // Load semua sticker awal
        loadByCategory(0)
    }

    private fun loadByCategory(categoryId: Int) {
        class LoadStickers : AsyncTask<Void, Void, String>() {
            override fun doInBackground(vararg p0: Void?): String {
                val rh = RequestHandler()
                var url = Konfigurasi.URL_STICKER_LIST
                if (categoryId > 0) url += "?category_id=$categoryId"
                return rh.sendGetRequest(url)
            }

            override fun onPostExecute(s: String) {
                try {
                    val json = JSONObject(s)
                    if (json.getString("status") == "success") {
                        val stickers = json.getJSONArray("stickers")
                        if (stickers.length() == 0) {
                            rvStickers.adapter = null
                            Toast.makeText(this@CategoryActivity, "Tidak ada sticker di kategori ini", Toast.LENGTH_SHORT).show()
                        } else {
                            rvStickers.adapter = StickerAdapter(
                                this@CategoryActivity,
                                stickers,
                                onAddCart = { sticker -> addToCart(sticker) },
                                onItemClick = { sticker -> openPopup(sticker) }
                            )
                        }
                    }
                } catch (e: Exception) {
                    Toast.makeText(this@CategoryActivity, "Gagal memuat sticker", Toast.LENGTH_SHORT).show()
                }
            }
        }
        LoadStickers().execute()
    }

    private fun addToCart(sticker: JSONObject) {
        class AddCart : AsyncTask<Void, Void, String>() {
            override fun doInBackground(vararg p0: Void?): String {
                val params = HashMap<String, String>()
                params["user_id"] = session.getUserId().toString()
                params["sticker_id"] = sticker.getInt("id").toString()
                return RequestHandler().sendPostRequest(Konfigurasi.URL_CART_ADD, params)
            }

            override fun onPostExecute(s: String) {
                try {
                    val json = JSONObject(s)
                    Toast.makeText(this@CategoryActivity, json.getString("message"), Toast.LENGTH_SHORT).show()
                } catch (e: Exception) {
                    Toast.makeText(this@CategoryActivity, "Gagal menambah ke cart", Toast.LENGTH_SHORT).show()
                }
            }
        }
        AddCart().execute()
    }

    private fun openPopup(sticker: JSONObject) {
        val intent = Intent(this, HomePage5::class.java)
        intent.putExtra("sticker_id", sticker.getInt("id"))
        intent.putExtra("sticker_name", sticker.getString("name"))
        intent.putExtra("sticker_image", sticker.getString("image"))
        intent.putExtra("sticker_price", sticker.getInt("price"))
        startActivity(intent)
    }
}