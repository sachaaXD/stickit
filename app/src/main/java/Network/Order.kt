package Network

import com.google.gson.annotations.SerializedName

data class Order(
    val id: Int,
    @SerializedName("user_id") val userId: Int,
    @SerializedName("name") val name: String?, // User name from JOIN
    @SerializedName("total") val totalPrice: Int, // Changed from total_price
    val status: String,
    @SerializedName("created_at") val createdAt: String,
    @SerializedName("total_items") val totalItems: Int? // Total items in order
)
