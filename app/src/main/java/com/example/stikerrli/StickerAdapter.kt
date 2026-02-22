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

class StickerAdapter(
    private val context: Context,
    private val stickers: JSONArray,
    private val onAddCart: ((JSONObject) -> Unit)? = null,
    private val onItemClick: ((JSONObject) -> Unit)? = null
) : RecyclerView.Adapter<StickerAdapter.ViewHolder>() {

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val imgSticker: ImageView = view.findViewById(R.id.imgSticker)
        val tvName: TextView = view.findViewById(R.id.tvStickerName)
        val tvPrice: TextView = view.findViewById(R.id.tvStickerPrice)
        val btnAdd: ImageView = view.findViewById(R.id.btnAddToCart)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.item_sticker, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int = stickers.length()

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val sticker = stickers.getJSONObject(position)
        holder.tvName.text = sticker.getString("name")
        holder.tvPrice.text = "Rp. ${String.format("%,d", sticker.getInt("price"))},-"

        Glide.with(context)
            .load(sticker.getString("image"))
            .placeholder(R.drawable.image_removebg_preview_1_)
            .into(holder.imgSticker)

        holder.btnAdd.setOnClickListener {
            onAddCart?.invoke(sticker)
        }

        holder.itemView.setOnClickListener {
            onItemClick?.invoke(sticker)
        }
    }
}
