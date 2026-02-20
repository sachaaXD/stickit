<?php
header('Content-Type: application/json');

require '../config/db.php';
require '../admin/guard.php'; // ← WAJIB

$user_id = (int)$user['id']; // ← dari token, bukan input

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
    $total += (int)$row['price'];
}
$stmt->close();

if (count($items) === 0) {
    $conn->close();
    echo json_encode([
        'status' => 'error',
        'message' => 'Cart is empty'
    ]);
    exit;
}

$conn->begin_transaction();

try {

    // buat order
    $stmtOrder = $conn->prepare("
        INSERT INTO orders (user_id, total, status)
        VALUES (?, ?, 'pending')
    ");
    $stmtOrder->bind_param("ii", $user_id, $total);
    $stmtOrder->execute();

    $order_id = $conn->insert_id;
    $stmtOrder->close();

    // order items
    $stmtItem = $conn->prepare("
        INSERT INTO order_items (order_id, sticker_id, price, qty)
        VALUES (?, ?, ?, 1)
    ");

    foreach ($items as $it) {

        $sticker_id = (int)$it['sticker_id'];
        $price = (int)$it['price'];

        $stmtItem->bind_param(
            "iii",
            $order_id,
            $sticker_id,
            $price
        );

        $stmtItem->execute();
    }

    $stmtItem->close();

    // kosongkan cart
    $stmtClear = $conn->prepare("
        DELETE FROM cart WHERE user_id = ?
    ");
    $stmtClear->bind_param("i", $user_id);
    $stmtClear->execute();
    $stmtClear->close();

    $conn->commit();

    echo json_encode([
        'status' => 'success',
        'order_id' => (int)$order_id,
        'total' => (int)$total
    ]);

} catch (Exception $e) {

    $conn->rollback();

    echo json_encode([
        'status' => 'error',
        'message' => 'Checkout failed'
    ]);
}

$conn->close();