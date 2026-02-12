<?php
header('Content-Type: application/json');
require '../config/db.php';

$data = json_decode(file_get_contents("php://input"), true);
if (!is_array($data)) {
    echo json_encode(['status' => 'error', 'message' => 'Invalid JSON']);
    exit;
}

$user_id = isset($data['user_id']) ? intval($data['user_id']) : 0;
if ($user_id <= 0) {
    echo json_encode(['status' => 'error', 'message' => 'user_id required']);
    exit;
}

// ambil isi cart user
$stmt = $conn->prepare("
    SELECT s.id AS sticker_id, s.price
    FROM cart c
    JOIN stickers s ON c.sticker_id = s.id
    WHERE c.user_id = ?
");
$stmt->bind_param("i", $user_id);
$stmt->execute();
$result = $stmt->get_result();

$items = [];
$total = 0;

while ($row = $result->fetch_assoc()) {
    $items[] = $row;
    $total += intval($row['price']); 
}
$stmt->close();

if (count($items) === 0) {
    $conn->close();
    echo json_encode(['status' => 'error', 'message' => 'Cart is empty']);
    exit;
}

// transaksi
$conn->begin_transaction();

try {
    // 1) buat order
    $stmtOrder = $conn->prepare("INSERT INTO orders (user_id, total, status) VALUES (?, ?, 'pending')");
    $stmtOrder->bind_param("ii", $user_id, $total);
    $stmtOrder->execute();
    $order_id = (int)$conn->insert_id;
    $stmtOrder->close();

    // 2) masukkan order items
    $stmtItem = $conn->prepare("INSERT INTO order_items (order_id, sticker_id, price, qty) VALUES (?, ?, ?, 1)");
    foreach ($items as $it) {
        $sticker_id = intval($it['sticker_id']);
        $price = intval($it['price']);
        $stmtItem->bind_param("iii", $order_id, $sticker_id, $price);
        $stmtItem->execute();
    }
    $stmtItem->close();

    // 3) kosongkan cart
    $stmtClear = $conn->prepare("DELETE FROM cart WHERE user_id = ?");
    $stmtClear->bind_param("i", $user_id);
    $stmtClear->execute();
    $stmtClear->close();

    $conn->commit();
    $conn->close();

    echo json_encode([
        'status' => 'success',
        'message' => 'Order created',
        'order_id' => $order_id,
        'total' => (int)$total
    ]);
    exit;

} catch (Exception $e) {
    $conn->rollback();
    $conn->close();
    echo json_encode(['status' => 'error', 'message' => 'Checkout failed']);
    exit;
}
