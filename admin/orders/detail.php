<?php

header('Content-Type: application/json');
require '../../config/db.php';
require '../guard.php';

$user = require_admin($conn);


/*
|--------------------------------------------------------------------------
| Base URL
|--------------------------------------------------------------------------
*/

function base_url() {

    $host = $_SERVER['HTTP_HOST'];

    $protocol =
        (!empty($_SERVER['HTTPS']) && $_SERVER['HTTPS'] !== 'off')
        ? "https"
        : "http";

    return $protocol . "://" . $host . "/sticker-api/";
}


/*
|--------------------------------------------------------------------------
| Validate order_id
|--------------------------------------------------------------------------
*/

$order_id = intval($_GET['order_id'] ?? 0);

if ($order_id <= 0) {

    echo json_encode([
        'status' => 'error',
        'message' => 'order_id required'
    ]);

    exit;
}


/*
|--------------------------------------------------------------------------
| Get Order Header
|--------------------------------------------------------------------------
*/

$stmt = $conn->prepare("
    SELECT
        o.id,
        o.user_id,
        u.name,
        o.total,
        o.status,
        o.created_at
    FROM orders o
    JOIN users u ON o.user_id = u.id
    WHERE o.id = ?
    LIMIT 1
");

$stmt->bind_param("i", $order_id);
$stmt->execute();

$result = $stmt->get_result();
$order = $result->fetch_assoc();

$stmt->close();

if (!$order) {

    echo json_encode([
        'status' => 'error',
        'message' => 'Order not found'
    ]);

    $conn->close();
    exit;
}


/*
|--------------------------------------------------------------------------
| Cast order data
|--------------------------------------------------------------------------
*/

$order = [
    'id' => (int)$order['id'],
    'user_id' => (int)$order['user_id'],
    'name' => $order['name'],
    'total' => (int)$order['total'],
    'status' => $order['status'],
    'created_at' => $order['created_at']
];


/*
|--------------------------------------------------------------------------
| Get Order Items
|--------------------------------------------------------------------------
*/

$stmt2 = $conn->prepare("
    SELECT
        s.id,
        s.name,
        s.image,
        oi.price,
        oi.qty
    FROM order_items oi
    JOIN stickers s ON oi.sticker_id = s.id
    WHERE oi.order_id = ?
");

$stmt2->bind_param("i", $order_id);
$stmt2->execute();

$result2 = $stmt2->get_result();

$items = [];
$base = base_url();

while ($row = $result2->fetch_assoc()) {

    $items[] = [
        'id' => (int)$row['id'],
        'name' => $row['name'],
        'image' => $base . "uploads/" . $row['image'],
        'price' => (int)$row['price'],
        'qty' => (int)$row['qty']
    ];
}

$stmt2->close();


/*
|--------------------------------------------------------------------------
| Response
|--------------------------------------------------------------------------
*/

echo json_encode([
    'status' => 'success',
    'order'  => $order,
    'items'  => $items
]);

$conn->close();