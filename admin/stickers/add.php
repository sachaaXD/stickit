<?php
header('Content-Type: application/json');
require '../../config/db.php';
require_once __DIR__ . '/../_guard.php';
require_admin($conn);


$data = json_decode(file_get_contents("php://input"), true);

$name = $data['name'] ?? '';
$price = $data['price'] ?? 0;
$category_id = $data['category_id'] ?? 0;
$image = $data['image'] ?? '';

if (!$name || !$price || !$category_id || !$image) {
    echo json_encode(['status'=>'error','message'=>'Incomplete data']);
    exit;
}

$stmt = $conn->prepare("
    INSERT INTO stickers (name, price, category_id, image)
    VALUES (?, ?, ?, ?)
");
$stmt->bind_param("siis", $name, $price, $category_id, $image);

$stmt->execute();
echo json_encode(['status'=>'success','message'=>'Sticker added']);

$stmt->close();
$conn->close();
