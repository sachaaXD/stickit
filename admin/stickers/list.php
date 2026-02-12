<?php
header('Content-Type: application/json');
require '../../config/db.php';
require_once __DIR__ . '/../_guard.php';
require_admin($conn);


$query = "
    SELECT s.id, s.name, s.image, s.price, c.name AS category
    FROM stickers s
    JOIN categories c ON s.category_id = c.id
    ORDER BY s.id DESC
";

$result = $conn->query($query);

$stickers = [];
while ($row = $result->fetch_assoc()) {
    $stickers[] = $row;
}

echo json_encode(['status' => 'success', 'stickers' => $stickers]);
$conn->close();
