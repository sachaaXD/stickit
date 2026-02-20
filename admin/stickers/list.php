<?php

header('Content-Type: application/json');
require '../../config/db.php';
require '../guard.php';

$user = require_admin($conn);

function base_url() {

    $host = $_SERVER['HTTP_HOST'];

    $protocol =
        (!empty($_SERVER['HTTPS']) && $_SERVER['HTTPS'] !== 'off')
        ? "https"
        : "http";

    return $protocol . "://" . $host . "/sticker-api/";
}


$query = "
    SELECT
        s.id,
        s.name,
        s.image,
        s.price,
        c.name AS category
    FROM stickers s
    JOIN categories c ON s.category_id = c.id
    ORDER BY s.id DESC
";

$result = $conn->query($query);

$base = base_url();

$stickers = [];

while ($row = $result->fetch_assoc()) {

    $stickers[] = [

        'id' => (int)$row['id'],
        'name' => $row['name'],
        'image' => $base . $row['image'],
        'price' => (int)$row['price'],
        'category' => $row['category']
    ];
}


echo json_encode([
    'status' => 'success',
    'stickers' => $stickers
]);

$conn->close();