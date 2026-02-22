package com.example.stikerrli

import Network.Sticker
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.stikerrli.databinding.ItemAdminStickerBinding

class AdminStickerAdapter(
    private var stickers: List<Sticker>,
    private val onEditClick: (Sticker) -> Unit,
    private val onDeleteClick: (Sticker) -> Unit
) : RecyclerView.Adapter<AdminStickerAdapter.StickerViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): StickerViewHolder {
        val binding = ItemAdminStickerBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return StickerViewHolder(binding)
    }

    override fun onBindViewHolder(holder: StickerViewHolder, position: Int) {
        holder.bind(stickers[position])
    }

    override fun getItemCount(): Int = stickers.size

    fun updateData(newStickers: List<Sticker>) {
        stickers = newStickers
        notifyDataSetChanged()
    }

    inner class StickerViewHolder(private val binding: ItemAdminStickerBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(sticker: Sticker) {
            binding.tvStickerNameAdmin.text = sticker.name
            binding.tvStickerPriceAdmin.text = "Rp ${sticker.price}"

            Glide.with(itemView.context)
                .load(sticker.imageUrl)
                .into(binding.imgStickerAdmin)

            binding.btnEditSticker.setOnClickListener { onEditClick(sticker) }
            binding.btnDeleteSticker.setOnClickListener { onDeleteClick(sticker) }
        }
    }
}