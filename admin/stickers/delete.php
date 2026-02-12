<?php
header('Content-Type: application/json');
require '../../config/db.php';
require_once __DIR__ . '/../_guard.php';
require_admin($conn);


$id = $_GET['id'] ?? 0;
if (!$id) {
    echo json_encode(['status'=>'error','message'=>'id required']);
    exit;
}

$stmt = $conn->prepare("DELETE FROM stickers WHERE id=?");
$stmt->bind_param("i", $id);
$stmt->execute();

echo json_encode(['status'=>'success','message'=>'Sticker deleted']);
$stmt->close();
$conn->close();
