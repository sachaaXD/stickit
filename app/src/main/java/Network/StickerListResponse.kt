package Network

data class StickerListResponse(
    val status: String,
    val message: String,
    val data: List<Sticker>? // Made nullable
)
