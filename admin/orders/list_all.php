<?php

header('Content-Type: application/json');
require '../../config/db.php';
require '../guard.php';

$user = require_admin($conn);

/*
|--------------------------------------------------------------------------
| Get All Orders
|--------------------------------------------------------------------------
*/

$stmt = $conn->prepare("
    SELECT 
        o.id,
        o.user_id,
        u.name,
        o.total,
        o.status,
        o.created_at,
        COALESCE(
            (SELECT SUM(oi.qty)
             FROM order_items oi
             WHERE oi.order_id = o.id),
            0
        ) AS total_items
    FROM orders o
    JOIN users u ON o.user_id = u.id
    ORDER BY o.id DESC
");

$stmt->execute();
$result = $stmt->get_result();

$orders = [];

while ($row = $result->fetch_assoc()) {

    $orders[] = [
        'id' => (int)$row['id'],
        'user_id' => (int)$row['user_id'],
        'name' => $row['name'],
        'total' => (int)$row['total'],
        'status' => $row['status'],
        'created_at' => $row['created_at'],
        'total_items' => (int)$row['total_items']
    ];
}


/*
|--------------------------------------------------------------------------
| Response
|--------------------------------------------------------------------------
*/

echo json_encode([
    'status' => 'success',
    'orders' => $orders
]);

$stmt->close();
$conn->close();