<?php

header('Content-Type: application/json');
require '../../config/db.php';
require_once __DIR__ . '/../_guard.php';
require_admin($conn);


function base_url() {
    $host = $_SERVER['HTTP_HOST']; 
    $protocol = (!empty($_SERVER['HTTPS']) && $_SERVER['HTTPS'] !== 'off') ? "https" : "http";
    return $protocol . "://" . $host . "/sticker-api/";
}

$order_id = isset($_GET['order_id']) ? intval($_GET['order_id']) : 0;
if ($order_id <= 0) {
    echo json_encode(['status' => 'error', 'message' => 'order_id required']);
    exit;
}

$stmt = $conn->prepare("
    SELECT o.id, o.user_id, u.name, o.total, o.status, o.created_at
    FROM orders o
    JOIN users u ON o.user_id = u.id
    WHERE o.id = ?
");
$stmt->bind_param("i", $order_id);
$stmt->execute();
$orderRes = $stmt->get_result();
$order = $orderRes->fetch_assoc();
$stmt->close();

if (!$order) {
    echo json_encode(['status' => 'error', 'message' => 'Order not found']);
    $conn->close();
    exit;
}

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
$base = base_url();

while ($row = $result2->fetch_assoc()) {
    $row['image'] = $base . "uploads/" . $row['image'];
    $items[] = $row;
}


echo json_encode(['status' => 'success', 'order' => $order, 'items' => $items]);

$stmt2->close();
$conn->close();
?>
