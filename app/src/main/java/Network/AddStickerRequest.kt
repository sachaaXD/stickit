package Network

import com.google.gson.annotations.SerializedName

data class AddStickerRequest(
    val name: String,
    val price: Int,
    @SerializedName("category_id") val categoryId: Int,
    // Untuk image, kita akan menanganinya secara terpisah, 
    // kemungkinan besar sebagai multipart atau URL setelah di-upload.
    @SerializedName("image_url") val imageUrl: String 
)
