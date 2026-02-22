package com.example.stikerrli

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {

    private lateinit var session: SessionManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        session = SessionManager(this)

        // MainActivity sekarang hanya bertugas sebagai router
        // Cek status login, lalu arahkan ke activity yang sesuai
        if (session.isLoggedIn()) {
            // Jika sudah login, cek role dan arahkan ke dashboard yang sesuai
            val intent = if (session.getRole() == "admin") {
                Intent(this, AdminDashboardActivity::class.java)
            } else {
                Intent(this, HomePage::class.java)
            }
            startActivity(intent)
        } else {
            // Jika belum login, arahkan ke LoginActivity
            startActivity(Intent(this, LoginActivity::class.java))
        }

        // Pastikan untuk selalu memanggil finish() agar MainActivity tidak menumpuk di back stack
        finish()
    }
}
