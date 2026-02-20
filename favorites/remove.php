<?php
header('Content-Type: application/json');

require '../config/db.php';
require '../config/auth.php';

// ambil user dari token
$user = get_user_from_token($conn);
$user_id = (int)$user['id'];


// ambil JSON
$data = json_decode(file_get_contents('php://input'), true);

if (!is_array($data)) {

    echo json_encode([
        'status' => 'error',
        'message' => 'Invalid JSON'
    ]);

    exit;
}

$sticker_id = (int)($data['sticker_id'] ?? 0);

if ($sticker_id <= 0) {

    echo json_encode([
        'status' => 'error',
        'message' => 'sticker_id required'
    ]);

    exit;
}


// cek ada atau tidak
$check = $conn->prepare("
    SELECT id
    FROM favorites
    WHERE user_id = ? AND sticker_id = ?
");

$check->bind_param("ii", $user_id, $sticker_id);
$check->execute();
$check->store_result();

if ($check->num_rows === 0) {

    echo json_encode([
        'status' => 'error',
        'message' => 'Not in favorites'
    ]);

    $check->close();
    $conn->close();
    exit;
}

$check->close();


// delete
$stmt = $conn->prepare("
    DELETE FROM favorites
    WHERE user_id = ? AND sticker_id = ?
");

$stmt->bind_param("ii", $user_id, $sticker_id);

if ($stmt->execute()) {

    echo json_encode([
        'status' => 'success',
        'message' => 'Removed from favorites'
    ]);

} else {

    echo json_encode([
        'status' => 'error',
        'message' => 'Delete failed'
    ]);
}

$stmt->close();
$conn->close();