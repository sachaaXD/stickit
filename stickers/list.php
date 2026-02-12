<?php
header('Content-Type: application/json');
require '../config/db.php';

function base_url() {
    $host = $_SERVER['HTTP_HOST'];
    $protocol = (!empty($_SERVER['HTTPS']) && $_SERVER['HTTPS'] !== 'off') ? "https" : "http";
    return $protocol . "://" . $host . "/sticker-api/";
}

$base = base_url();

// kalau ada → filter, kalau kosong → tampil semua
$category_id = isset($_GET['category_id']) ? intval($_GET['category_id']) : 0;

if ($category_id > 0) {
    $stmt = $conn->prepare("SELECT id, name, image, price, category_id FROM stickers WHERE category_id = ? ORDER BY id DESC");
    $stmt->bind_param("i", $category_id);
} else {
    $stmt = $conn->prepare("SELECT id, name, image, price, category_id FROM stickers ORDER BY id DESC");
}

$stmt->execute();
$result = $stmt->get_result();

$stickers = [];
while ($row = $result->fetch_assoc()) {
 
    $row['image'] = $base . "uploads/" . $row['image'];
    $stickers[] = $row;
}

echo json_encode(['status' => 'success', 'stickers' => $stickers]);

$stmt->close();
$conn->close();
