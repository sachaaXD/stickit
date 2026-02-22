package Network

import com.google.gson.annotations.SerializedName

data class OrderItem(
    val id: Int,
    val name: String,
    val price: Int,
    val quantity: Int,
    @SerializedName("image_url") val imageUrl: String
)
