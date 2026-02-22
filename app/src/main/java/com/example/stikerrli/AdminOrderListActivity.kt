package com.example.stikerrli

import Network.OrderListResponse
import Network.RetrofitClient
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.stikerrli.databinding.ActivityAdminOrderListBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class AdminOrderListActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAdminOrderListBinding
    private lateinit var adapter: AdminOrderAdapter
    private lateinit var session: SessionManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAdminOrderListBinding.inflate(layoutInflater)
        setContentView(binding.root)

        session = SessionManager(this)

        setupRecyclerView()
        fetchOrders()
    }

    private fun setupRecyclerView() {
        adapter = AdminOrderAdapter(emptyList()) { order ->
            // TODO: Handle view order details action
            Toast.makeText(this, "View Details for Order #${order.id}", Toast.LENGTH_SHORT).show()
        }
        binding.rvAdminOrders.layoutManager = LinearLayoutManager(this)
        binding.rvAdminOrders.adapter = adapter
    }

    private fun fetchOrders() {
        val userId = session.getUserId()
        if (userId == 0) {
            Toast.makeText(this, "Error: User not logged in.", Toast.LENGTH_LONG).show()
            return
        }

        RetrofitClient.instance.adminGetOrders(userId).enqueue(object : Callback<OrderListResponse> {
            override fun onResponse(call: Call<OrderListResponse>, response: Response<OrderListResponse>) {
                if (response.isSuccessful) {
                    val orderResponse = response.body()
                    if (orderResponse != null && orderResponse.status == "success") {
                        // Handle case where data might be null (e.g., no orders)
                        adapter.updateData(orderResponse.data ?: emptyList())
                    } else {
                        Toast.makeText(this@AdminOrderListActivity, "Failed to fetch orders: ${orderResponse?.message}", Toast.LENGTH_LONG).show()
                    }
                } else {
                    Toast.makeText(this@AdminOrderListActivity, "Error: ${response.message()}", Toast.LENGTH_LONG).show()
                }
            }

            override fun onFailure(call: Call<OrderListResponse>, t: Throwable) {
                Toast.makeText(this@AdminOrderListActivity, "Network Error: ${t.message}", Toast.LENGTH_LONG).show()
            }
        })
    }
}