package com.example.stikerrli

import Network.AuthResponse
import Network.RegisterRequest
import Network.RetrofitClient
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.stikerrli.databinding.ActivityRegisterBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class RegisterActivity : AppCompatActivity() {

    private lateinit var binding: ActivityRegisterBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.TextRegis.setOnClickListener {
            finish()
        }

        binding.ButtonRegis.setOnClickListener {
            val user = binding.UsernameRegis.text.toString().trim()
            val pass = binding.PasswordRegis.text.toString().trim()

            if (user.isEmpty() || pass.isEmpty()) {
                Toast.makeText(this, "Lengkapi Username dan Password!", Toast.LENGTH_SHORT).show()
            } else {
                registerUser(user, pass)
            }
        }
    }

    private fun registerUser(user: String, pass: String) {
        val request = RegisterRequest(name = user, password = pass)
        RetrofitClient.instance.register(request).enqueue(object : Callback<AuthResponse> {
            override fun onResponse(call: Call<AuthResponse>, response: Response<AuthResponse>) {
                if (response.isSuccessful && response.body()?.status == "success") {
                    Toast.makeText(this@RegisterActivity, "Registration successful!", Toast.LENGTH_LONG).show()
                    finish() // Kembali ke halaman login setelah berhasil
                } else {
                    val errorMessage = response.body()?.message ?: "Unknown error"
                    Toast.makeText(this@RegisterActivity, "Registration failed: $errorMessage", Toast.LENGTH_LONG).show()
                }
            }

            override fun onFailure(call: Call<AuthResponse>, t: Throwable) {
                Toast.makeText(this@RegisterActivity, "Network Error: ${t.message}", Toast.LENGTH_LONG).show()
            }
        })
    }
}