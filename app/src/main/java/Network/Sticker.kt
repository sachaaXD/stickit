package Network

import com.google.gson.annotations.SerializedName

data class Sticker(
    val id: Int,
    val name: String,
    val price: Int,
    @SerializedName("image_url") val imageUrl: String,
    @SerializedName("category_id") val categoryId: Int
)
