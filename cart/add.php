<?php
header('Content-Type: application/json');

require '../config/db.php';
require '../config/auth.php';

// ambil user dari token (akan auto-exit kalau invalid)
$user = get_user_from_token($conn);
$user_id = $user['id'];

// ambil JSON body
$data = json_decode(file_get_contents("php://input"), true);

if (!is_array($data)) {
    echo json_encode([
        'status' => 'error',
        'message' => 'Invalid JSON'
    ]);
    exit;
}

$sticker_id = isset($data['sticker_id']) ? (int)$data['sticker_id'] : 0;

if ($sticker_id <= 0) {
    echo json_encode([
        'status' => 'error',
        'message' => 'sticker_id required'
    ]);
    exit;
}


// cek sticker ada atau tidak
$checkSticker = $conn->prepare("
    SELECT id 
    FROM stickers 
    WHERE id = ?
");

$checkSticker->bind_param("i", $sticker_id);
$checkSticker->execute();
$checkSticker->store_result();

if ($checkSticker->num_rows === 0) {

    echo json_encode([
        'status' => 'error',
        'message' => 'Sticker not found'
    ]);

    $checkSticker->close();
    $conn->close();
    exit;
}

$checkSticker->close();


// cek sudah ada di cart
$checkCart = $conn->prepare("
    SELECT id 
    FROM cart 
    WHERE user_id = ? AND sticker_id = ?
");

$checkCart->bind_param("ii", $user_id, $sticker_id);
$checkCart->execute();
$checkCart->store_result();

if ($checkCart->num_rows > 0) {

    echo json_encode([
        'status' => 'error',
        'message' => 'Already in cart'
    ]);

    $checkCart->close();
    $conn->close();
    exit;
}

$checkCart->close();


// insert ke cart
$stmt = $conn->prepare("
    INSERT INTO cart (user_id, sticker_id)
    VALUES (?, ?)
");

$stmt->bind_param("ii", $user_id, $sticker_id);

if ($stmt->execute()) {

    echo json_encode([
        'status' => 'success',
        'message' => 'Added to cart'
    ]);

} else {

    echo json_encode([
        'status' => 'error',
        'message' => 'Insert failed'
    ]);
}

$stmt->close();
$conn->close();