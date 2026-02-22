package order

data class OrderListResponse(
    val status: String,
    val orders: List<Order>
)
