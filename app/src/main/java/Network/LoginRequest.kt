package Network

import com.google.gson.annotations.SerializedName

data class LoginRequest(
    @SerializedName("id") val id: Int? = null,
    @SerializedName("name") val username: String? = null,
    @SerializedName("password") val password: String,
    @SerializedName("role") val role: String
)
