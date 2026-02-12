<?php
header('Content-Type: application/json');
require '../config/db.php';

$data = json_decode(file_get_contents("php://input"), true);
if (!is_array($data)) {
    echo json_encode(['status' => 'error', 'message' => 'Invalid JSON']);
    exit;
}

$user_id    = isset($data['user_id']) ? intval($data['user_id']) : 0;
$sticker_id = isset($data['sticker_id']) ? intval($data['sticker_id']) : 0;

if ($user_id <= 0 || $sticker_id <= 0) {
    echo json_encode(['status' => 'error', 'message' => 'user_id and sticker_id required']);
    exit;
}

$stmt = $conn->prepare("DELETE FROM cart WHERE user_id = ? AND sticker_id = ?");
$stmt->bind_param("ii", $user_id, $sticker_id);

if ($stmt->execute()) {
    echo json_encode(['status' => 'success', 'message' => 'Sticker removed from cart']);
} else {
    echo json_encode(['status' => 'error', 'message' => 'Failed to remove from cart']);
}

$stmt->close();
$conn->close();
