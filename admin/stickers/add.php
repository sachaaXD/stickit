<?php

header('Content-Type: application/json');
require '../../config/db.php';
require '../guard.php';

$user = require_admin($conn);

$id = intval($_GET['id'] ?? 0);

if ($id <= 0) {

    echo json_encode([
        'status'=>'error',
        'message'=>'id required'
    ]);

    exit;
}


/*
|--------------------------------------------------------------------------
| Check exist
|--------------------------------------------------------------------------
*/

$check = $conn->prepare("SELECT id FROM stickers WHERE id=? LIMIT 1");
$check->bind_param("i", $id);
$check->execute();
$res = $check->get_result();

if ($res->num_rows === 0) {

    echo json_encode([
        'status'=>'error',
        'message'=>'Sticker not found'
    ]);

    exit;
}

$check->close();


/*
|--------------------------------------------------------------------------
| Delete
|--------------------------------------------------------------------------
*/

$stmt = $conn->prepare("DELETE FROM stickers WHERE id=?");
$stmt->bind_param("i", $id);

if ($stmt->execute()) {

    echo json_encode([
        'status'=>'success',
        'message'=>'Sticker deleted'
    ]);

} else {

    echo json_encode([
        'status'=>'error',
        'message'=>'Delete failed'
    ]);
}

$stmt->close();
$conn->close();