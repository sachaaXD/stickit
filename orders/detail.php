<?php
header('Content-Type: application/json');

require '../config/db.php';
require '../admin/guard.php';

function base_url() {
    $host = $_SERVER['HTTP_HOST'];
    $protocol =
        (!empty($_SERVER['HTTPS']) &&
        $_SERVER['HTTPS'] !== 'off')
        ? "https" : "http";

    return $protocol . "://" . $host . "/sticker-api/";
}

$user_id = (int)$user['id'];

$order_id =
    isset($_GET['order_id'])
    ? (int)$_GET['order_id']
    : 0;

if ($order_id <= 0) {

    echo json_encode([
        'status' => 'error',
        'message' => 'order_id required'
    ]);

    exit;
}

$stmt = $conn->prepare("
    SELECT id, user_id, total, status, created_at
    FROM orders
    WHERE id = ? AND user_id = ?
");

$stmt->bind_param(
    "ii",
    $order_id,
    $user_id
);

$stmt->execute();

$order = $stmt->get_result()->fetch_assoc();

$stmt->close();

if (!$order) {

    echo json_encode([
        'status' => 'error',
        'message' => 'Order not found'
    ]);

    exit;
}

$base = base_url();

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

$res2 = $stmt2->get_result();

$items = [];

while ($row = $res2->fetch_assoc()) {

    $items[] = [
        'id' => (int)$row['id'],
        'name' => $row['name'],
        'image' => $base . "uploads/" . $row['image'],
        'price' => (int)$row['price'],
        'qty' => (int)$row['qty']
    ];
}

echo json_encode([
    'status' => 'success',
    'order' => [
        'id' => (int)$order['id'],
        'user_id' => (int)$order['user_id'],
        'total' => (int)$order['total'],
        'status' => $order['status'],
        'created_at' => $order['created_at']
    ],
    'items' => $items
]);

$stmt2->close();
$conn->close();