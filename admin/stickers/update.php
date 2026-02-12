<?php
header('Content-Type: application/json');
require '../../config/db.php';
require_once __DIR__ . '/../_guard.php';
require_admin($conn);


$data = json_decode(file_get_contents("php://input"), true);

$id = $data['id'] ?? 0;
$name = $data['name'] ?? '';
$price = $data['price'] ?? 0;
$category_id = $data['category_id'] ?? 0;

if (!$id || !$name || !$price || !$category_id) {
    echo json_encode(['status'=>'error','message'=>'Incomplete data']);
    exit;
}

$stmt = $conn->prepare("
    UPDATE stickers
    SET name=?, price=?, category_id=?
    WHERE id=?
");
$stmt->bind_param("siii", $name, $price, $category_id, $id);
$stmt->execute();

echo json_encode(['status'=>'success','message'=>'Sticker updated']);
$stmt->close();
$conn->close();
