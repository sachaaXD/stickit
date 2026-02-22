package com.example.stikerrli

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import org.json.JSONArray
import org.json.JSONObject

class CartAdapter(
    private val context: Context,
    private val items: JSONArray,
    private val onRemove: ((JSONObject) -> Unit)? = null
) : RecyclerView.Adapter<CartAdapter.ViewHolder>() {

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val imgSticker: ImageView = view.findViewById(R.id.imgCartSticker)
        val tvName: TextView = view.findViewById(R.id.tvCartName)
        val tvPrice: TextView = view.findViewById(R.id.tvCartPrice)
        val btnRemove: ImageView = view.findViewById(R.id.btnRemoveCart)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.item_cart, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int = items.length()

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = items.getJSONObject(position)
        holder.tvName.text = item.getString("name")
        holder.tvPrice.text = "Rp. ${String.format("%,d", item.getInt("price"))},-"

        Glide.with(context)
            .load(item.getString("image"))
            .placeholder(R.drawable.image_removebg_preview_1_)
            .into(holder.imgSticker)

        holder.btnRemove.setOnClickListener {
            onRemove?.invoke(item)
        }
    }
}
