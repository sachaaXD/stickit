package com.example.stikerrli

import android.content.Intent
import android.os.AsyncTask
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.stikerrli.databinding.ActivityKeranjangBinding
import org.json.JSONArray
import org.json.JSONObject

class KeranjangActivity : AppCompatActivity() {

    private lateinit var binding: ActivityKeranjangBinding
    private lateinit var session: SessionManager
    private lateinit var rvCart: RecyclerView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityKeranjangBinding.inflate(layoutInflater)
        setContentView(binding.root)

        session = SessionManager(this)
        rvCart = binding.rvCartItems
        rvCart.layoutManager = GridLayoutManager(this, 2)

        // Checkout button
        binding.btnCheckout.setOnClickListener { checkout() }

        // Bottom nav
        binding.navHome.setOnClickListener {
            startActivity(Intent(this, HomePage::class.java))
            finish()
        }
        binding.navCategory.setOnClickListener {
            startActivity(Intent(this, CategoryActivity::class.java))
        }
        binding.navCart.setOnClickListener { /* sudah di cart */ }
        binding.navProfile.setOnClickListener {
            startActivity(Intent(this, ProfileActivity::class.java))
        }

        loadCart()
    }

    override fun onResume() {
        super.onResume()
        loadCart()
    }

    private fun loadCart() {
        class LoadCart : AsyncTask<Void, Void, String>() {
            override fun doInBackground(vararg p0: Void?): String {
                val rh = RequestHandler()
                return rh.sendGetRequest(Konfigurasi.URL_CART_LIST + "?user_id=${session.getUserId()}")
            }

            override fun onPostExecute(s: String) {
                try {
                    val json = JSONObject(s)
                    if (json.getString("status") == "success") {
                        val cart = json.getJSONArray("cart")
                        updateUI(cart)
                    }
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
        }
        LoadCart().execute()
    }

    private fun updateUI(cart: JSONArray) {
        // Calculate total
        var total = 0
        for (i in 0 until cart.length()) {
            total += cart.getJSONObject(i).getInt("price")
        }

        binding.tvTotalLabel.text = "Total (${cart.length()} Produk)"
        binding.tvTotalPrice.text = "Rp. ${String.format("%,d", total)},-"

        rvCart.adapter = CartAdapter(this, cart, onRemove = { item ->
            removeFromCart(item)
        })
    }

    private fun removeFromCart(item: JSONObject) {
        class RemoveCart : AsyncTask<Void, Void, String>() {
            override fun doInBackground(vararg p0: Void?): String {
                val params = HashMap<String, String>()
                params["user_id"] = session.getUserId().toString()
                params["sticker_id"] = item.getInt("id").toString()
                return RequestHandler().sendPostRequest(Konfigurasi.URL_CART_REMOVE, params)
            }

            override fun onPostExecute(s: String) {
                try {
                    val json = JSONObject(s)
                    Toast.makeText(this@KeranjangActivity, json.getString("message"), Toast.LENGTH_SHORT).show()
                    loadCart()
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
        }
        RemoveCart().execute()
    }

    private fun checkout() {
        class Checkout : AsyncTask<Void, Void, String>() {
            override fun doInBackground(vararg p0: Void?): String {
                val params = HashMap<String, String>()
                params["user_id"] = session.getUserId().toString()
                return RequestHandler().sendPostRequest(Konfigurasi.URL_ORDER_CHECKOUT, params)
            }

            override fun onPostExecute(s: String) {
                try {
                    val json = JSONObject(s)
                    Toast.makeText(this@KeranjangActivity, json.getString("message"), Toast.LENGTH_SHORT).show()
                    if (json.getString("status") == "success") {
                        loadCart() // refresh cart (sekarang kosong)
                    }
                } catch (e: Exception) {
                    Toast.makeText(this@KeranjangActivity, "Checkout gagal", Toast.LENGTH_SHORT).show()
                }
            }
        }
        Checkout().execute()
    }
}
