package com.example.stikerrli

object Konfigurasi {
    private const val BASE_URL = "http://10.0.2.2:8000/"

    // --- AUTH ---
    const val URL_LOGIN = BASE_URL + "login.php"
    const val URL_REGISTER = BASE_URL + "register.php"

    // --- STICKERS ---
    const val URL_STICKER_LIST = BASE_URL + "stickers/list.php"

    // --- CATEGORIES ---
    const val URL_CATEGORY_LIST = BASE_URL + "categories/list.php"

    // --- CART ---
    const val URL_CART_ADD = BASE_URL + "cart/add.php"
    const val URL_CART_LIST = BASE_URL + "cart/list.php"
    const val URL_CART_REMOVE = BASE_URL + "cart/remove.php"

    // --- ORDERS ---
    const val URL_ORDER_CHECKOUT = BASE_URL + "orders/checkout.php"
    const val URL_ORDER_LIST = BASE_URL + "orders/list.php"
    const val URL_ORDER_DETAIL = BASE_URL + "orders/detail.php"

    // --- FAVORITES ---
    const val URL_FAVORITE_ADD = BASE_URL + "favorites/add.php"
    const val URL_FAVORITE_LIST = BASE_URL + "favorites/list.php"
    const val URL_FAVORITE_REMOVE = BASE_URL + "favorites/remove.php"

    // --- ADMIN ---
    const val URL_ADMIN_STICKER_LIST = BASE_URL + "admin/stickers/list.php"
    const val URL_ADMIN_STICKER_ADD = BASE_URL + "admin/stickers/add.php"
    const val URL_ADMIN_STICKER_UPDATE = BASE_URL + "admin/stickers/update.php"
    const val URL_ADMIN_STICKER_DELETE = BASE_URL + "admin/stickers/delete.php"
    const val URL_ADMIN_ORDER_LIST = BASE_URL + "admin/orders/list_all.php"
    const val URL_ADMIN_ORDER_DETAIL = BASE_URL + "admin/orders/detail.php"
}