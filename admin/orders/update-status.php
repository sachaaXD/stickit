<?php
header('Content-Type: application/json');

require '../../config/db.php';
require '../../config/auth.php';

/*
=================================
AUTH ADMIN
=================================
*/

$user = get_user_from_token($conn);

if (!$user || $user['role'] !== 'admin') {
    http_response_code(403);
    echo json_encode([
        'status' => 'error',
        'message' => 'Admin access required'
    ]);
    exit;
}

/*
=================================
AMBIL INPUT
=================================
*/

$data = json_decode(file_get_contents('php://input'), true);

$order_id = intval($data['order_id'] ?? 0);
$status = trim($data['status'] ?? '');

if ($order_id <= 0 || $status === '') {
    http_response_code(400);
    echo json_encode([
        'status' => 'error',
        'message' => 'order_id and status required'
    ]);
    exit;
}

/*
=================================
VALIDASI STATUS
=================================
*/

$allowed_status = ['pending', 'completed', 'cancelled'];

if (!in_array($status, $allowed_status)) {
    http_response_code(400);
    echo json_encode([
        'status' => 'error',
        'message' => 'Invalid status value'
    ]);
    exit;
}

/*
=================================
UPDATE STATUS
=================================
*/

$stmt = $conn->prepare("
    UPDATE orders
    SET status=?
    WHERE id=?
");

$stmt->bind_param("si", $status, $order_id);

if ($stmt->execute()) {

    echo json_encode([
        'status' => 'success',
        'message' => 'Order status updated'
    ]);

} else {

    http_response_code(500);
    echo json_encode([
        'status' => 'error',
        'message' => 'Failed to update order'
    ]);
}

$stmt->close();
$conn->close();