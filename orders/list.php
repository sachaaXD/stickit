<?php
header('Content-Type: application/json');
require '../config/db.php';

$user_id = isset($_GET['user_id']) ? intval($_GET['user_id']) : 0;
if ($user_id <= 0) {
    echo json_encode(['status' => 'error', 'message' => 'user_id required']);
    exit;
}

$stmt = $conn->prepare("
    SELECT 
        o.id, o.user_id, o.total, o.status, o.created_at,
        COALESCE((SELECT SUM(oi.qty) FROM order_items oi WHERE oi.order_id = o.id), 0) AS total_items
    FROM orders o
    WHERE o.user_id = ?
    ORDER BY o.id DESC
");
$stmt->bind_param("i", $user_id);
$stmt->execute();
$result = $stmt->get_result();

$orders = [];
while ($row = $result->fetch_assoc()) {
    $row['id'] = (int)$row['id'];
    $row['user_id'] = (int)$row['user_id'];
    $row['total'] = (int)$row['total'];
    $row['total_items'] = (int)$row['total_items'];
    $orders[] = $row;
}

echo json_encode(['status' => 'success', 'orders' => $orders]);

$stmt->close();
$conn->close();
