package com.example.stikerrli

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.stikerrli.databinding.ActivityLogin2Binding
import Network.AuthResponse
import Network.LoginRequest
import Network.RetrofitClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLogin2Binding
    private lateinit var session: SessionManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLogin2Binding.inflate(layoutInflater)
        setContentView(binding.root)

        session = SessionManager(this)

        // Jika sudah login, langsung ke MainActivity (yang akan jadi router)
        if (session.isLoggedIn()) {
            startActivity(Intent(this, MainActivity::class.java))
            finish()
            return
        }

        binding.btnLogin.setOnClickListener {
            val username = binding.etUsername.text.toString().trim()
            val password = binding.etPassword.text.toString().trim()

            if (username.isEmpty() || password.isEmpty()) {
                Toast.makeText(this, "Username dan password harus diisi", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            // Anda bisa menambahkan input untuk role jika dibutuhkan
            val loginRequest = LoginRequest(username = username, password = password, role = "user")

            RetrofitClient.instance.login(loginRequest).enqueue(object : Callback<AuthResponse> {
                override fun onResponse(call: Call<AuthResponse>, response: Response<AuthResponse>) {
                    if (response.isSuccessful) {
                        val authResponse = response.body()
                        if (authResponse != null && authResponse.status == "success") {
                            val userId = authResponse.userId
                            val role = authResponse.role
                            if (userId != null && role != null) {
                                // Simpan sesi login
                                session.saveLogin(userId, username, role)
                                Toast.makeText(this@LoginActivity, "Login Berhasil", Toast.LENGTH_SHORT).show()

                                // Arahkan ke MainActivity yang akan bertindak sebagai router
                                val intent = Intent(this@LoginActivity, MainActivity::class.java)
                                startActivity(intent)
                                finish()
                            } else {
                                Toast.makeText(this@LoginActivity, "Login Gagal: Data pengguna tidak lengkap", Toast.LENGTH_SHORT).show()
                            }
                        } else {
                             Toast.makeText(this@LoginActivity, "Login Gagal: " + (response.body()?.message ?: "Pesan tidak diketahui"), Toast.LENGTH_SHORT).show()
                        }
                    } else {
                        Toast.makeText(this@LoginActivity, "Login Gagal: " + response.message(), Toast.LENGTH_SHORT).show()
                    }
                }

                override fun onFailure(call: Call<AuthResponse>, t: Throwable) {
                    Toast.makeText(this@LoginActivity, "Error: " + t.message, Toast.LENGTH_SHORT).show()
                }
            })
        }
    }
}
