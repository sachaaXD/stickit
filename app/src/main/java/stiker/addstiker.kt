package stiker

data class AddStickerRequest(
    val name: String,
    val price: Int,
    val category_id: Int,
    val image: String
)