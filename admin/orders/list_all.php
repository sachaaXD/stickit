<?php
header('Content-Type: application/json');
require '../../config/db.php';

require_once __DIR__ . '/../_guard.php';
require_admin($conn);

$stmt = $conn->prepare("
    SELECT 
        o.id, o.user_id, u.name, o.total, o.status, o.created_at,
        COALESCE((SELECT SUM(oi.qty) FROM order_items oi WHERE oi.order_id = o.id), 0) AS total_items
    FROM orders o
    JOIN users u ON o.user_id = u.id
    ORDER BY o.id DESC
");
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
