package Network

data class OrderDetailResponse(
    val success: Boolean,
    val message: String,
    val data: OrderDetails // Detail dari satu order
)

data class OrderDetails(
    val id: Int,
    val status: String,
    val total_price: Int,
    val created_at: String,
    val items: List<OrderItem> // Daftar barang dalam order
)
