package com.example.stikerrli

import Network.Order
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.stikerrli.databinding.ItemAdminOrderBinding

class AdminOrderAdapter(
    private var orders: List<Order>,
    private val onViewDetailsClick: (Order) -> Unit
) : RecyclerView.Adapter<AdminOrderAdapter.OrderViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): OrderViewHolder {
        val binding = ItemAdminOrderBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return OrderViewHolder(binding)
    }

    override fun onBindViewHolder(holder: OrderViewHolder, position: Int) {
        holder.bind(orders[position])
    }

    override fun getItemCount(): Int = orders.size

    fun updateData(newOrders: List<Order>) {
        orders = newOrders
        notifyDataSetChanged()
    }

    inner class OrderViewHolder(private val binding: ItemAdminOrderBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(order: Order) {
            binding.tvOrderIdAdmin.text = "Order #${order.id}"
            binding.tvOrderUserIdAdmin.text = "User ID: ${order.userId}"
            binding.tvOrderTotalPriceAdmin.text = "Total: Rp ${order.totalPrice}"
            binding.tvOrderStatusAdmin.text = "Status: ${order.status}"
            binding.tvOrderDateAdmin.text = "Date: ${order.createdAt}" // Mungkin perlu format tanggal

            binding.btnViewOrderDetail.setOnClickListener { onViewDetailsClick(order) }
        }
    }
}