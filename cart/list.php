<?php
header('Content-Type: application/json');
require '../config/db.php';

function base_url() {
    $host = $_SERVER['HTTP_HOST'];
    $protocol = (!empty($_SERVER['HTTPS']) && $_SERVER['HTTPS'] !== 'off') ? "https" : "http";
    return $protocol . "://" . $host . "/sticker-api/";
}

$user_id = isset($_GET['user_id']) ? intval($_GET['user_id']) : 0;
if ($user_id <= 0) {
    echo json_encode(['status' => 'error', 'message' => 'user_id required']);
    exit;
}

$stmt = $conn->prepare("
    SELECT s.id, s.name, s.image, s.price, s.category_id
    FROM cart c
    JOIN stickers s ON c.sticker_id = s.id
    WHERE c.user_id = ?
    ORDER BY s.id DESC
");
$stmt->bind_param("i", $user_id);
$stmt->execute();
$result = $stmt->get_result();

$base = base_url();

$cart = [];
while ($row = $result->fetch_assoc()) {
    $row['id'] = (int)$row['id'];
    $row['price'] = (int)$row['price'];
    $row['category_id'] = (int)$row['category_id'];
    $row['image'] = $base . "uploads/" . $row['image'];
    $cart[] = $row;
}

echo json_encode(['status' => 'success', 'cart' => $cart]);

$stmt->close();
$conn->close();
