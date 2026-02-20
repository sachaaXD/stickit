<?php
header('Content-Type: application/json');

require '../config/db.php';
require '../config/auth.php';

// ambil user dari token (auto exit kalau invalid)
$user = get_user_from_token($conn);
$user_id = (int)$user['id'];


// base url function
function base_url() {

    $host = $_SERVER['HTTP_HOST'];

    $protocol =
        (!empty($_SERVER['HTTPS']) && $_SERVER['HTTPS'] !== 'off')
        ? "https"
        : "http";

    return $protocol . "://" . $host . "/sticker-api/";
}

$base = base_url();


// prepare query
$stmt = $conn->prepare("
    SELECT
        s.id,
        s.name,
        s.image,
        s.price,
        s.category_id
    FROM cart c
    JOIN stickers s ON c.sticker_id = s.id
    WHERE c.user_id = ?
    ORDER BY c.id DESC
");

if (!$stmt) {

    echo json_encode([
        'status' => 'error',
        'message' => 'Prepare failed'
    ]);

    $conn->close();
    exit;
}

$stmt->bind_param("i", $user_id);
$stmt->execute();

$result = $stmt->get_result();

$cart = [];

while ($row = $result->fetch_assoc()) {

    $cart[] = [

        'id' => (int)$row['id'],

        'name' => $row['name'],

        'image' => $base . $row['image'],

        'price' => (int)$row['price'],

        'category_id' => (int)$row['category_id']

    ];
}


echo json_encode([
    'status' => 'success',
    'cart' => $cart
]);


$stmt->close();
$conn->close();