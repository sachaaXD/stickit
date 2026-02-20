<?php
header('Content-Type: application/json');

require '../config/db.php';
require '../admin/guard.php';

$user_id = (int)$user['id'];

$stmt = $conn->prepare("
    SELECT 
        o.id,
        o.user_id,
        o.total,
        o.status,
        o.created_at,
        COALESCE((
            SELECT SUM(oi.qty)
            FROM order_items oi
            WHERE oi.order_id = o.id
        ),0) AS total_items
    FROM orders o
    WHERE o.user_id = ?
    ORDER BY o.id DESC
");

$stmt->bind_param("i", $user_id);
$stmt->execute();

$result = $stmt->get_result();

$orders = [];

while ($row = $result->fetch_assoc()) {

    $orders[] = [
        'id' => (int)$row['id'],
        'user_id' => (int)$row['user_id'],
        'total' => (int)$row['total'],
        'status' => $row['status'],
        'created_at' => $row['created_at'],
        'total_items' => (int)$row['total_items']
    ];
}

echo json_encode([
    'status' => 'success',
    'orders' => $orders
]);

$stmt->close();
$conn->close();