package Network

import com.google.gson.annotations.SerializedName

data class OrderListResponse(
    val status: String,
    val message: String,
    @SerializedName("orders") val data: List<Order>? // Look for "orders" in JSON, map to "data"
)
