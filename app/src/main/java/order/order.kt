package order

data class Order(
    val id: Int,
    val user_id: Int,
    val name: String,
    val total: Int,
    val status: String,
    val created_at: String,
    val total_items: Int? = null
)