<?php
header('Content-Type: application/json');
require '../../config/db.php';
require '../guard.php';

$user = require_admin($conn);

// Validasi ID
$id = isset($_GET['id']) ? (int)$_GET['id'] : 0;

if ($id <= 0) {
    echo json_encode([
        'status' => 'error',
        'message' => 'Invalid id'
    ]);
    exit;
}

// Cek apakah sticker ada
$check = $conn->prepare("
    SELECT id 
    FROM stickers 
    WHERE id = ?
");

$check->bind_param("i", $id);
$check->execute();
$check->store_result();

if ($check->num_rows === 0) {

    echo json_encode([
        'status' => 'error',
        'message' => 'Sticker not found'
    ]);

    $check->close();
    $conn->close();
    exit;
}

$check->close();


// Hapus data
$stmt = $conn->prepare("
    DELETE FROM stickers 
    WHERE id = ?
");

$stmt->bind_param("i", $id);

if ($stmt->execute()) {

    echo json_encode([
        'status' => 'success',
        'message' => 'Sticker deleted'
    ]);

} else {

    echo json_encode([
        'status' => 'error',
        'message' => 'Failed to delete sticker'
    ]);
}

$stmt->close();
$conn->close();