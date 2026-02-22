package Network

import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

interface ApiService {

    @POST("register.php")
    fun register(@Body request: RegisterRequest): Call<AuthResponse>

    @POST("login.php")
    fun login(@Body request: LoginRequest): Call<AuthResponse>

    // Admin Sticker Endpoints
    @GET("admin/stickers/list.php")
    fun adminGetStickers(@Query("user_id") userId: Int): Call<StickerListResponse>

    @POST("admin/stickers/add.php")
    fun adminAddSticker(@Body request: AddStickerRequest): Call<AuthResponse>

    @POST("admin/stickers/update.php")
    fun adminUpdateSticker(@Body request: UpdateStickerRequest): Call<AuthResponse>

    @GET("admin/stickers/delete.php")
    fun adminDeleteSticker(@Query("user_id") userId: Int, @Query("id") id: Int): Call<AuthResponse>

    // Admin Order Endpoints
    @GET("admin/orders/list_all.php")
    fun adminGetOrders(@Query("user_id") userId: Int): Call<OrderListResponse>

    @GET("admin/orders/detail.php")
    fun adminGetOrderDetail(@Query("user_id") userId: Int, @Query("id") id: Int): Call<OrderDetailResponse>

    // Public Sticker Endpoint
    @GET("stickers/list.php")
    fun getStickers(): Call<StickerListResponse>
}