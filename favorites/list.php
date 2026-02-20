<?php
header('Content-Type: application/json');

require '../config/db.php';
require '../config/auth.php';

// ambil user dari token
$user = get_user_from_token($conn);
$user_id = (int)$user['id'];


// base url
function base_url() {

    $host = $_SERVER['HTTP_HOST'];

    $protocol =
        (!empty($_SERVER['HTTPS']) && $_SERVER['HTTPS'] !== 'off')
        ? "https"
        : "http";

    return $protocol . "://" . $host . "/sticker-api/";
}

$base = base_url();


// query favorites
$stmt = $conn->prepare("
    SELECT
        s.id,
        s.name,
        s.image,
        s.price,
        s.category_id
    FROM favorites f
    JOIN stickers s ON f.sticker_id = s.id
    WHERE f.user_id = ?
    ORDER BY f.id DESC
");

$stmt->bind_param("i", $user_id);
$stmt->execute();

$result = $stmt->get_result();

$favorites = [];

while ($row = $result->fetch_assoc()) {

    $favorites[] = [

        'id' => (int)$row['id'],

        'name' => $row['name'],

        'image' => $base . $row['image'],

        'price' => (int)$row['price'],

        'category_id' => (int)$row['category_id']

    ];
}


echo json_encode([
    'status' => 'success',
    'favorites' => $favorites
]);

$stmt->close();
$conn->close();