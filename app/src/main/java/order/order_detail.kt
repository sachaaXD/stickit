package order

data class OrderDetailResponse(
    val status: String,
    val order: Order?,
//    val items: List<orderItem>?,
    val message: String?
)
