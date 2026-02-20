<?php

header('Content-Type: application/json');
require '../../config/db.php';
require '../guard.php';

$user = require_admin($conn);

$data = json_decode(file_get_contents("php://input"), true);

if (!is_array($data)) {

    echo json_encode([
        'status'=>'error',
        'message'=>'Invalid JSON'
    ]);

    exit;
}


$id = intval($data['id'] ?? 0);
$name = trim($data['name'] ?? '');
$price = intval($data['price'] ?? 0);
$category_id = intval($data['category_id'] ?? 0);


if ($id <= 0 || $name === '' || $price <= 0 || $category_id <= 0) {

    echo json_encode([
        'status'=>'error',
        'message'=>'Incomplete data'
    ]);

    exit;
}


$stmt = $conn->prepare("
    UPDATE stickers
    SET name=?, price=?, category_id=?
    WHERE id=?
");

$stmt->bind_param("siii", $name, $price, $category_id, $id);

if ($stmt->execute()) {

    echo json_encode([
        'status'=>'success',
        'message'=>'Sticker updated'
    ]);

} else {

    echo json_encode([
        'status'=>'error',
        'message'=>'Update failed'
    ]);
}

$stmt->close();
$conn->close();