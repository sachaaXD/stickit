package com.example.stikerrli

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.stikerrli.databinding.ActivityProfileBinding

class ProfileActivity : AppCompatActivity() {

    private lateinit var binding: ActivityProfileBinding
    private lateinit var session: SessionManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityProfileBinding.inflate(layoutInflater)
        setContentView(binding.root)

        session = SessionManager(this)

        // Display user info
        binding.etName.setText(session.getUserName())
        binding.etUsername.setText(session.getUserName())

        // Logout on long press password field
        binding.btnLogout.setOnClickListener {
            session.logout()
            val intent = Intent(this, MainActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(intent)
            finish()
        }

        // Bottom nav
        binding.navHome.setOnClickListener {
            startActivity(Intent(this, HomePage::class.java))
            finish()
        }
        binding.navCategory.setOnClickListener {
            startActivity(Intent(this, CategoryActivity::class.java))
        }
        binding.navCart.setOnClickListener {
            startActivity(Intent(this, KeranjangActivity::class.java))
        }
        binding.navProfile.setOnClickListener { /* sudah di profile */ }
    }
}
