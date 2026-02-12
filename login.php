<?php
header('Content-Type: application/json');
require 'config/db.php';

$data = json_decode(file_get_contents('php://input'), true);

if (!is_array($data)) {
    echo json_encode(['status' => 'error', 'message' => 'Invalid JSON']);
    exit;
}

$name = isset($data['name']) ? trim($data['name']) : '';
$raw_password = isset($data['password']) ? $data['password'] : '';

if ($name === '' || $raw_password === '') {
    echo json_encode(['status' => 'error', 'message' => 'Name and password required']);
    exit;
}

$stmt = $conn->prepare("SELECT id, password, role FROM users WHERE name = ?");
$stmt->bind_param("s", $name);
$stmt->execute();
$result = $stmt->get_result();

if ($result->num_rows === 0) {
    $stmt->close();
    $conn->close();
    echo json_encode(['status' => 'error', 'message' => 'User not found']);
    exit;
}

$user = $result->fetch_assoc();

// verif password
if (password_verify($raw_password, $user['password'])) {
    echo json_encode([
        'status' => 'success',
        'message' => 'Login successful',
        'user_id' => (int)$user['id'],
        'role' => $user['role']
    ]);
} else {
    echo json_encode(['status' => 'error', 'message' => 'Wrong password']);
}

$stmt->close();
$conn->close();
