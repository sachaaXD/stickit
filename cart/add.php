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

// cek sudah ada di cart
$check = $conn->prepare("SELECT id FROM cart WHERE user_id = ? AND sticker_id = ?");
$check->bind_param("ii", $user_id, $sticker_id);
$check->execute();
$res = $check->get_result();

if ($res->num_rows > 0) {
    $check->close();
    $conn->close();
    echo json_encode(['status' => 'error', 'message' => 'Sticker already in cart']);
    exit;
}
$check->close();

$stmt = $conn->prepare("INSERT INTO cart (user_id, sticker_id) VALUES (?, ?)");
$stmt->bind_param("ii", $user_id, $sticker_id);

if ($stmt->execute()) {
    echo json_encode(['status' => 'success', 'message' => 'Sticker added to cart']);
} else {
    echo json_encode(['status' => 'error', 'message' => 'Failed to add to cart']);
}

$stmt->close();
$conn->close();
