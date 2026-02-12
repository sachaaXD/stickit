<?php
header('Content-Type: application/json');
require '../config/db.php';

function base_url() {
    $host = $_SERVER['HTTP_HOST'];
    $protocol = (!empty($_SERVER['HTTPS']) && $_SERVER['HTTPS'] !== 'off') ? "https" : "http";
    return $protocol . "://" . $host . "/sticker-api/";
}

$order_id = isset($_GET['order_id']) ? intval($_GET['order_id']) : 0;
$user_id  = isset($_GET['user_id']) ? intval($_GET['user_id']) : 0;

if ($order_id <= 0 || $user_id <= 0) {
    echo json_encode(['status' => 'error', 'message' => 'order_id and user_id required']);
    exit;
}

// header order (milik user)
$stmt = $conn->prepare("
    SELECT id, user_id, total, status, created_at
    FROM orders
    WHERE id = ? AND user_id = ?
");
$stmt->bind_param("ii", $order_id, $user_id);
$stmt->execute();
$res = $stmt->get_result();
$order = $res->fetch_assoc();
$stmt->close();

if (!$order) {
    $conn->close();
    echo json_encode(['status' => 'error', 'message' => 'Order not found']);
    exit;
}

// casting angka biar rapi
$order['id'] = (int)$order['id'];
$order['user_id'] = (int)$order['user_id'];
$order['total'] = (int)$order['total'];

$base = base_url();

$stmt2 = $conn->prepare("
    SELECT s.id, s.name, s.image, oi.price, oi.qty
    FROM order_items oi
    JOIN stickers s ON oi.sticker_id = s.id
    WHERE oi.order_id = ?
");
$stmt2->bind_param("i", $order_id);
$stmt2->execute();
$result2 = $stmt2->get_result();

$items = [];
while ($row = $result2->fetch_assoc()) {
    $row['id'] = (int)$row['id'];
    $row['price'] = (int)$row['price'];
    $row['qty'] = (int)$row['qty'];
    $row['image'] = $base . "uploads/" . $row['image'];
    $items[] = $row;
}

echo json_encode(['status' => 'success', 'order' => $order, 'items' => $items]);

$stmt2->close();
$conn->close();
