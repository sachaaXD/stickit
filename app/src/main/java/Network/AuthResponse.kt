package Network

import com.google.gson.annotations.SerializedName

data class AuthResponse(
    val status: String,
    val message: String,
    val role: String?,
    @SerializedName("user_id") val userId: Int?
)
